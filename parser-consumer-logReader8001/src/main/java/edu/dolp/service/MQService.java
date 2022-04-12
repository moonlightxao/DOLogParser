package edu.dolp.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "parser-data-mq")
public interface MQService {
    @GetMapping("/data/MQTest")
    String test(@RequestParam("topic") String topic, @RequestParam("mes") String mes);
}
