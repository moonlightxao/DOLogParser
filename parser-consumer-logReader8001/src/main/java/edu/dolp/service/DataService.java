package edu.dolp.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "parser-data-logDatabase")
public interface DataService {
    @GetMapping("/data/loadNamespaces")
    String loadNamespaces();


    @GetMapping("/data/getTemplatesByNamespace")
    String getTemplatesByNamespace(@RequestParam("namespace") String namespace,
                                   @RequestParam("currentPage") Integer currentPage,
                                   @RequestParam("pageSize") Integer pageSize);

    @RequestMapping("/data/getAnomalyByNamespace")
    String getAnomalyByNamespace(@RequestParam("namespace") String namespace,
                                        @RequestParam("currentPage") Integer currentPage,
                                        @RequestParam("pageSize") Integer pageSize);
}
