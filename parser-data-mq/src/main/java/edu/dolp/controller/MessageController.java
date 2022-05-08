package edu.dolp.controller;

import cn.hutool.json.JSONObject;
import edu.dolp.entity.LogEntity;
import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MessageController {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.name-server}")
    private String nameSrvAddr;

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
        System.out.println("uploadMessage准备进行异步发送");
        obj.put("status", 300);
        String topic = ("MoLFI".equals(entity.getMethod())) ? "MoLFI-topic" : uploadTopic;
        rocketMQTemplate.asyncSend(topic, entity, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                obj.put("status", 200);
                System.out.println("异步发送预处理日志至消息队列成功");
            }

            @Override
            public void onException(Throwable throwable) {
                obj.put("status", 500);
                System.out.println("异步发送预处理日志至消息队列失败");
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
        System.out.println("准备更新日志模板");
        JSONObject obj = new JSONObject();
        obj.put("status", 300);
        rocketMQTemplate.asyncSend(updateTopic, arg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                obj.put("status", 200);
                System.out.println("异步发送更新数据库请求成功");
            }

            @Override
            public void onException(Throwable throwable) {
                obj.put("status", 500);
                System.out.println("异步发送更新数据库请求失败");
                throwable.printStackTrace();
            }
        });
        return obj.toStringPretty();
    }

    /**
    * @Description: 该接口接收日志异常检测模块检测出来的异常日志label，并将异常日志异步发送到消息队列
    * @Param:
    * @return:
    * @Author: Liu ZhiTian
    * @Date: 2022/5/6
    */
    @RequestMapping("/data/sendAnomalousLogs")
    public String sendAnomalousLogs(@RequestBody LogEntity entity){
        System.out.println("准备将消息发送到异常检测模块");
        JSONObject obj = new JSONObject();
        final int[] status = {300};
        //将type作为tag保存到detect-result这个topic
        String topic = "Detect-topic";
        rocketMQTemplate.asyncSend(topic, entity, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("异步发送异常检测结果请求成功");
                status[0] = 200;
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("异步发送异常检测结果请求失败");
                status[0] = 500;
                throwable.printStackTrace();
            }
        });
        obj.put("status", status[0]);
        return obj.toStringPretty();
    }

    /**
    * @Description: 该接口将异常检测模块的结果发送到消息队列，之后由数据持久化模块获取并保存到数据库
    * @Param:
    * @return:
    * @Author: Liu ZhiTian
    * @Date: 2022/5/8
    */
    @RequestMapping("/data/saveAnomalousLogs")
    public String saveAnomalousLogs(@RequestBody LogEntity entity){
        JSONObject obj = new JSONObject();
        final int[] status = {300};
        //将type作为tag保存到detect-result-topic这个topic
        String topic = "detect-result-topic";
        rocketMQTemplate.asyncSend(topic, entity, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("异步发送异常检测结果请求成功！！！！");
                status[0] = 200;
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("异步发送异常检测结果请求失败！！！！");
                status[0] = 500;
                throwable.printStackTrace();
            }
        });
        obj.put("status", status[0]);
        return obj.toStringPretty();
    }
}
