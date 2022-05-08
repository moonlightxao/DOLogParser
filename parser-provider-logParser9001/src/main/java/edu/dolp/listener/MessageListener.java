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
        System.out.println("日志解析模块获取到预处理的日志，准备进行解析");
        JSONObject obj = new JSONObject();
        JSONObject templatesFromDB;
        List<Template> templates = new ArrayList<>();
        List<LogMesEntity> logs = service.transLog(message);
        //对LogEntity检查合法性
        //检查缓存模块是否有该源的模板
        //检查数据持久化模块是否有该源的模板
        System.out.println("正在请求数据持久化模块查询是否存在该日志源的模板");
        String data = dataService.queryByNamespace(message.getNamespace());
        templatesFromDB = JSONUtil.parseObj(data);
        if("200".equals(templatesFromDB.getStr("status"))){
            System.out.println("成功查询到日志源:" + message.getNamespace() + " 的日志模板");
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
        System.out.println("日志源 " + message.getNamespace() + " 准备进行解析");
        service.setTemplates(templates);
        for(LogMesEntity log : logs){
            service.inferTemplate(log);
        }
        System.out.println("日志源 " + message.getNamespace() + " 解析完成");
        obj.put("status", 200);               //处理成功，返回状态码200
        obj.put("namespace", message.getNamespace());
        List<JSONObject> ts = new ArrayList<>();
        String[] results = new String[service.getTemplates().size()];
        int cnt = 0;
        for(Template t : service.getTemplates()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", t.getId());
            jsonObject.put("words", t.getWords());
            results[cnt] = t.getWords();
            ts.add(jsonObject);
        }
        message.setLogMessage(results);
        obj.put("templates", ts);
        System.out.println("日志源 " + message.getNamespace() + " 准备将模板发送到消息队列以进行模板更新");
        mqService.updateTemplates(obj.toStringPretty());
        //将解析的结果发送进消息队列，以便异常检测模块获取到异常结果
        System.out.println("日志源 " + message.getNamespace() + " 准备将模板发送到消息队列以进行异常检测");
        mqService.sendAnomalousLogs(message);
    }
}