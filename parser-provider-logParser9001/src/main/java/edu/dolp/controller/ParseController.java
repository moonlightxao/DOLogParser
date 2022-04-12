package edu.dolp.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import edu.dolp.entity.LogEntity;
import edu.dolp.entity.LogMesEntity;
import edu.dolp.entity.TemplateEntity;
import edu.dolp.service.DataService;
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
    @Resource
    private DataService dataService;

    @RequestMapping("/provider/parse")
    public String parse(@RequestBody LogEntity message){
        JSONObject obj = new JSONObject();
        JSONObject templatesFromDB;
        List<Template> templates = new ArrayList<>();
        List<LogMesEntity> logs = service.transLog(message);
        //对LogEntity检查合法性
        //检查缓存模块是否有该源的模板
        //检查数据持久化模块是否有该源的模板
        String data = dataService.queryByNamespace(message.getNamespace());
        templatesFromDB = JSONUtil.parseObj(data);
        if("200".equals(templatesFromDB.getStr("status"))){
            JSONArray array = templatesFromDB.getJSONArray("templates");
            for(int i = 0;i < array.size();i++){
                JSONObject t = array.getJSONObject(i);
                Integer id = t.getInt("id");
                String words = t.getStr("words");
                String namespace = t.getStr("namespace");
                templates.add(new Template(new LogMesEntity(words, namespace), id));
            }
        }
        //执行解析工作
        service.setTemplates(templates);
        for(LogMesEntity log : logs){
            service.inferTemplate(log);
        }
        /****将结果写回缓存或者持久化模块*******/

        /**********************************/
        obj.put("status", 200);               //处理成功，返回状态码200
        obj.put("namespace", message.getNamespace());
        List<JSONObject> ts = new ArrayList<>();
        for(Template t : service.getTemplates()){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", t.getId());
            jsonObject.put("words", t.getWords());
            ts.add(jsonObject);
        }
        obj.put("templates", ts);
        return dataService.updateTemplates(obj.toStringPretty());
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
