package edu.dolp.service;

import edu.dolp.entity.LogEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(value = "parser-data-mq")
public interface MQService {
    @RequestMapping("/data/update")
    String updateTemplates(@RequestBody String arg);

    @RequestMapping("/data/sendAnomalousLogs")
    String sendAnomalousLogs(@RequestBody LogEntity entity);
}
