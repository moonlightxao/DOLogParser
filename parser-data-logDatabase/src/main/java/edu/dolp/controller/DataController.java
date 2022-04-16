package edu.dolp.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.dolp.entity.Template;
import edu.dolp.entity.Visit;
import edu.dolp.mapper.TemplateMapper;
import edu.dolp.mapper.VisitMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DataController {
    @Resource
    private TemplateMapper templateMapper;

    @Resource
    private VisitMapper visitMapper;

    @GetMapping("/data/queryByNamespace")
    public String queryByNamespace(@RequestParam("namespace") String namespace){
        JSONObject obj = new JSONObject();
        List<Template> templates = templateMapper.selectList(new QueryWrapper<Template>().eq("namespace", namespace));
        if(templates != null&&templates.size() > 0){
            //正常情况
            obj.put("status", 200);
            obj.put("templates", templates);
        }else{
            //异常情况
            obj.put("status", 500);
            obj.put("templates", null);
        }
        return obj.toStringPretty();
    }

    /**
    * @Description:  更新日志模板的接口
    * @Param:  JSON格式的字符串
    * @return:  JSON格式的更新结果
    * @Author: Liu ZhiTian
    * @Date: 2022/4/7
    */
    @RequestMapping("/data/updateTemplates")
    public String updateTemplates(@RequestBody String arg){
        JSONObject obj = JSONUtil.parseObj(arg);
        JSONObject result = new JSONObject();
        String namespace = obj.getStr("namespace");
        if(!("200".equals(obj.getStr("status")))){
            result.put("status", 500);
            result.put("message", "传入的数据异常");
        }else{
            JSONArray array = obj.getJSONArray("templates");
            Integer rows = 0;
            for(int i = 0;i < array.size();i++){
                JSONObject t = array.getJSONObject(i);
                Integer id = t.getInt("id");
                String words = t.getStr("words");
                if(id == null){
                    rows += templateMapper.insert(new Template(namespace, words));
                }else{
                    rows += templateMapper.update(null, new UpdateWrapper<Template>().eq("id", id).set("words", words));
                }
            }
            if(rows.intValue() == array.size()){
                result.put("status", 200);
                result.put("message", "更新日志模板数据成功！");
            }
        }
        return result.toStringPretty();
    }

    @GetMapping("/data/updateVisit")
    public String updateVisit(@RequestParam("namespace") String namespace){
        JSONObject obj = new JSONObject();
        Visit visit = visitMapper.selectById(namespace);
        obj.put("visit", visit);
        Integer rows = visitMapper.update(null, new UpdateWrapper<Visit>().eq("namespace", namespace).set("count", visit.getCount() + 1));
        if(rows > 0){
            obj.put("status", 200);
        }else{
            obj.put("status", 500);
        }
        return obj.toStringPretty();
    }

}
