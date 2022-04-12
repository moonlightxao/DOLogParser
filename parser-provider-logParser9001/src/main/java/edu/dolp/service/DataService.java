package edu.dolp.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "parser-data-logDatabase")
public interface DataService {
    @GetMapping("/data/queryByNamespace")
    String queryByNamespace(@RequestParam("namespace") String namespace);

    @GetMapping("/data/updateTemplates")
    String updateTemplates(@RequestParam("templates") String arg);
}
