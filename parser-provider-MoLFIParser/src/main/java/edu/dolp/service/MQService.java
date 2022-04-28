package edu.dolp.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "parser-data-mq")
public interface MQService {
    @RequestMapping("/data/update")
    String updateTemplates(@RequestBody String arg);
}
