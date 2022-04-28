package edu.dolp.controller;

import cn.hutool.json.JSONObject;
import edu.dolp.entity.LogEntity;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MessageController {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    private static final String uploadTopic = "upload-topic";
    private static final String updateTopic = "update-topic";
    
    /** 
    * @Description: 接收日志读取模块上传预处理日志的接口 
    * @Param:  
    * @return:  
    * @Author: Liu ZhiTian 
    * @Date: 2022/4/12 
    */ 
    @RequestMapping("/data/upload")
    public String uploadMessage(@RequestBody LogEntity entity){
        JSONObject obj = new JSONObject();
        System.out.println("准备进行异步发送");
        obj.put("status", 300);
        String topic = ("MoLFI".equals(entity.getMethod())) ? "MoLFI-topic" : uploadTopic;
        rocketMQTemplate.asyncSend(topic, entity, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                obj.put("status", 200);
                System.out.println("异步发送成功！！！！");
            }

            @Override
            public void onException(Throwable throwable) {
                obj.put("status", 500);
                System.out.println("异步发送失败！！！！");
                throwable.printStackTrace();
            }
        });
        return obj.toStringPretty();
    }

    /**
    * @Description:  接收日志解析模块产生的日志模板，并传送给持久化模块进行数据库记录更新的接口
    * @Param:
    * @return:
    * @Author: Liu ZhiTian
    * @Date: 2022/4/12
    */
    @RequestMapping("/data/update")
    public String updateTemplates(@RequestBody String arg){
        JSONObject obj = new JSONObject();
        obj.put("status", 300);
        rocketMQTemplate.asyncSend(updateTopic, arg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                obj.put("status", 200);
                System.out.println("异步发送更新数据库请求成功！！！！");
            }

            @Override
            public void onException(Throwable throwable) {
                obj.put("status", 500);
                System.out.println("异步发送更新数据库请求失败！！！！");
                throwable.printStackTrace();
            }
        });
        return obj.toStringPretty();
    }
}
