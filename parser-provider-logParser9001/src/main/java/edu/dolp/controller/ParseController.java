package edu.dolp.controller;

import cn.hutool.json.JSONObject;
import edu.dolp.entity.LogEntity;
import edu.dolp.entity.LogMesEntity;
import edu.dolp.entity.TemplateEntity;
import edu.dolp.service.ManageService;
import edu.dolp.service.TemplateService;
import edu.dolp.service.impl.ManageServiceImpl;
import edu.dolp.service.impl.Template;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ParseController {
    private ManageService service = new ManageServiceImpl();

    @RequestMapping("/provider/parse")
    public String parse(@RequestBody LogEntity message){
        JSONObject obj = new JSONObject();
        List<Template> templates;
        List<LogMesEntity> logs = service.transLog(message);
        //对LogEntity检查合法性
        //检查缓存模块是否有该源的模板
        //检查数据持久化模块是否有该源的模板
        //执行解析工作
        templates = new ArrayList<>();
        service.setTemplates(templates);
        for(LogMesEntity log : logs){
            service.inferTemplate(log);
        }
        //将结果写回缓存或者持久化模块
        obj.put("status", 200);               //处理成功，返回状态码200
        obj.put("namespace", message.getNamespace());
        List<String> ts = new ArrayList<>();
        for(Template t : service.getTemplates()){
            ts.add(t.getWords());
        }
        obj.put("templates", ts);
        return obj.toStringPretty();
    }

    /**
    * @Description: 预留接口，提供NSGA解析日志的功能
    * @Param:
    * @return:
    * @Author: Liu ZhiTian
    * @Date: 2022/4/3
    */
    @RequestMapping("/provider/parseBsaeOnNSGA")
    public String parse(){
        return null;
    }
}
