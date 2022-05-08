package edu.dolp.service;

import edu.dolp.entity.LogEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "parser-data-mq")
public interface MQService {
    @RequestMapping("/data/sendAnomalousLogs")
    String sendAnomalousLogs(@RequestBody LogEntity entity);

    @RequestMapping("/data/saveAnomalousLogs")
    String saveAnomalousLogs(@RequestBody LogEntity entity);
}
