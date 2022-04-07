package edu.dolp.service;

import edu.dolp.entity.LogEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(value = "parser-provider-logParser")
public interface ParserService {

    @PostMapping("/provider/parse")
    String parse(@RequestBody LogEntity logs);
}
