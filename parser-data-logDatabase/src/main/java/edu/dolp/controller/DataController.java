package edu.dolp.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dolp.entity.Template;
import edu.dolp.mapper.TemplateMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DataController {
    @Resource
    private TemplateMapper templateMapper;

    @GetMapping("/data/queryById/{id}")
    public String queryById(@PathVariable("id") Integer id){
        JSONObject obj = new JSONObject();
        Template template = templateMapper.selectById(id);
        obj.put("result", template);
        return obj.toStringPretty();
    }


    @GetMapping("/data/queryByNamespace")
    public String queryByNamespace(@RequestParam("namespace") String namespace){
        JSONObject obj = new JSONObject();
        List<Template> templates = templateMapper.selectList(new QueryWrapper<Template>().eq("namespace", namespace));
        obj.put("templates", templates);
        return obj.toStringPretty();
    }
}
