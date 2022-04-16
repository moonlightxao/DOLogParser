package edu.dolp.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "parser-data-logDatabase")
public interface DataService {
    @RequestMapping("/data/updateTemplates")
    String updateTemplates(@RequestBody String arg);
}
