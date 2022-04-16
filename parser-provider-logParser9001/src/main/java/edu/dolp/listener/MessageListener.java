package edu.dolp.listener;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import edu.dolp.entity.LogEntity;
import edu.dolp.entity.LogMesEntity;
import edu.dolp.service.DataService;
import edu.dolp.service.MQService;
import edu.dolp.service.ManageService;
import edu.dolp.service.impl.ManageServiceImpl;
import edu.dolp.service.impl.Template;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@RocketMQMessageListener(topic = "upload-topic", consumerGroup = "upload-group")
public class MessageListener implements RocketMQListener<LogEntity> {
    private ManageService service = new ManageServiceImpl();
    @Resource
    private DataService dataService;

    @Resource
    private MQService mqService;

    @Override
    public void onMessage(LogEntity message) {
        JSONObject obj = new JSONObject();
        JSONObject templatesFromDB;
        List<Template> templates = new ArrayList<>();
        List<LogMesEntity> logs = service.transLog(message);
        //对LogEntity检查合法性
        //检查缓存模块是否有该源的模板
        //检查数据持久化模块是否有该源的模板
        String data = dataService.queryByNamespace(message.getNamespace());
        templatesFromDB = JSONUtil.parseObj(data);
        if("200".equals(templatesFromDB.getStr("status"))){
            JSONArray array = templatesFromDB.getJSONArray("templates");
            for(int i = 0;i < array.size();i++){
                JSONObject t = array.getJSONObject(i);
                Integer id = t.getInt("id");
                String words = t.getStr("words");
                String namespace = t.getStr("namespace");
                templates.add(new Template(new LogMesEntity(words, namespace), id));
            }
        }
        //执行解析工作
        service.setTemplates(templates);
        for(LogMesEntity log : logs){
            service.inferTemplate(log);
        }
        /****将结果写回缓存或者持久化模块*******/

        /**********************************/
        obj.put("status", 200);               //处理成功，返回状态码200
        obj.put("namespace", message.getNamespace());
        List<JSONObject> ts = new ArrayList<>();
        for(Template t : service.getTemplates()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", t.getId());
            jsonObject.put("words", t.getWords());
            ts.add(jsonObject);
        }
        obj.put("templates", ts);
        mqService.updateTemplates(obj.toStringPretty());
    }
}