package edu.dolp.listener;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.dolp.entity.Template;
import edu.dolp.entity.Visit;
import edu.dolp.mapper.TemplateMapper;
import edu.dolp.mapper.VisitMapper;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RocketMQMessageListener(topic = "update-topic", consumerGroup = "update-group")
public class TemplatesListener implements RocketMQListener<String> {
    @Resource
    private TemplateMapper templateMapper;

    @Resource
    private VisitMapper visitMapper;

    @Override
    public void onMessage(String arg) {
        System.out.println(arg);
        JSONObject obj = JSONUtil.parseObj(arg);
        JSONObject result = new JSONObject();
        String namespace = obj.getStr("namespace");
        if(!("200".equals(obj.getStr("status")))){
            result.put("status", 500);
            result.put("message", "传入的数据异常");
            System.out.println(result);
        }else{
            JSONArray array = obj.getJSONArray("templates");
            Integer rows = 0;
            for(int i = 0;i < array.size();i++){
                JSONObject t = array.getJSONObject(i);
                Integer id = t.getInt("id");
                String words = t.getStr("words");
                if(id == null){
                    rows += templateMapper.insert(new Template(namespace, words));
                }else{
                    rows += templateMapper.update(null, new UpdateWrapper<Template>().eq("id", id).set("words", words));
                }
            }
            if(rows.intValue() == array.size()){
                result.put("status", 200);
                result.put("message", "更新日志模板数据成功！");
            }else{
                result.put("status", 500);
                result.put("message", "更新日志模板数据失败");
            }
        }
        Visit visit = visitMapper.selectById(namespace);
        int vr = 0;
        if(visit == null){
            vr += visitMapper.insert(new Visit(namespace, 1));
        }else{
            vr += visitMapper.update(null, new UpdateWrapper<Visit>().
                    eq("namespace", namespace).
                    set("count", visit.getCount() + 1));
        }
        if(vr > 0){
            result.put("status", 200);
            result.put("visitMessage", "更新visit数据成功！");
        }else{
            result.put("status", 500);
            result.put("visitMessage", "更新visit数据s失败！");
        }
    }
}
