package edu.dolp.controller;

import edu.dolp.entity.MyMessage;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/data/MQTest")
    public String test(@RequestParam("topic") String topic, @RequestParam("mes") String mes){
        MyMessage message = new MyMessage(1, mes);
        rocketMQTemplate.convertAndSend(topic, message);
        return "发送消息成功";
    }

}
