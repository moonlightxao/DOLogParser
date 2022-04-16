package edu.dolp.controller;

import cn.hutool.core.io.file.FileReader;
import edu.dolp.entity.FileEntity;
import edu.dolp.entity.FileTransQueue;
import edu.dolp.entity.LogEntity;
import edu.dolp.service.MQService;
import edu.dolp.service.ParserService;
import edu.dolp.util.ParseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;


@RestController
public class ReaderController {

    @Resource
    private MQService mqService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @RequestMapping("/consumer/read")
    public String read(){
        if(FileTransQueue.isEmpty()){
           return "there is no new log file!";
        }
        FileEntity entity = FileTransQueue.poll();
        FileReader reader = new FileReader(uploadDir + entity.getFileName());
        List<String> fileList = reader.readLines();
        String[] files = new String[fileList.size()];
        if(entity.getReg() == null){
            for(int i = 0;i < files.length;i++){
                files[i] = ParseUtil.parse(fileList.get(i));
            }
        }else{
            for(int i = 0;i < files.length;i++){
                String[] tmp = fileList.get(i).split(entity.getReg());
                files[i] = tmp[tmp.length - 1];
            }
        }
        return mqService.uploadMessage(new LogEntity(entity.getNamespace(), files));
    }

}
