package edu.dolp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigController {

    @Value("${myconfig.val}")
    private boolean myConfig;

    @RequestMapping("/provider/changeConfig")
    public boolean refreshChange(){
        return myConfig;
    }
}
