package edu.dolp.controller;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.json.JSONObject;
import edu.dolp.entity.FileEntity;
import edu.dolp.entity.FileTransQueue;
import edu.dolp.entity.LogEntity;
import edu.dolp.service.DataService;
import edu.dolp.service.MQService;
import edu.dolp.service.ParserService;
import edu.dolp.util.ParseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
public class ReaderController {

    @Resource
    private MQService mqService;

    @Resource
    private DataService dataService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @RequestMapping("/consumer/read")
    public String read(@RequestParam("method") String method){
        if(FileTransQueue.isEmpty()){
           return "there is no new log file!";
        }
        FileEntity entity = FileTransQueue.poll();
        FileReader reader = new FileReader(uploadDir + entity.getFileName());
        List<String> fileList = reader.readLines();
        String[] files = new String[fileList.size()];
        if(entity.getReg() == null||entity.getReg().length() == 0){
            for(int i = 0;i < files.length;i++){
                files[i] = ParseUtil.parse(fileList.get(i));
            }
        }else{
            for(int i = 0;i < files.length;i++){
                String[] tmp = fileList.get(i).split(entity.getReg());
                files[i] = tmp[tmp.length - 1];
            }
        }
        return mqService.uploadMessage(new LogEntity(entity.getNamespace(), method, files));
    }

    @RequestMapping("/consumer/testAxios")
    public String testAxios(@RequestParam("namespace") String namespace){
        JSONObject obj = new JSONObject();
        if(namespace != null){
            obj.put("status", 200);
        }else{
            obj.put("status", 500);
        }
        obj.put("message", namespace);
        return obj.toStringPretty();
    }

    /**
    * @Description:  获取所有命名空间的接口
    * @Param:
    * @return:
    * @Author: Liu ZhiTian
    * @Date: 2022/4/30
    */
    @GetMapping("/consumer/loadNamespaces")
    public String loadNamespaces(){
        return dataService.loadNamespaces();
    }

    /**
    * @Description: 根据命名空间获取日志模板信息
    * @Param:  namespace: 命名空间
    * @return:  templates: namespace对应的所有日志模板列表
    * @Author: Liu ZhiTian
    * @Date: 2022/4/30
    */
    @GetMapping("/consumer/getTemplatesByNamespace")
    public String getTemplatesByNamespace(@RequestParam("namespace") String namespace, @RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer size){
        return dataService.getTemplatesByNamespace(namespace, currentPage, size);
    }

}
