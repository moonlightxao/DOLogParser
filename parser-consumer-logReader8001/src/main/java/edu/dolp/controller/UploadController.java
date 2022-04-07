package edu.dolp.controller;

import edu.dolp.entity.FileEntity;
import edu.dolp.entity.FileTransQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;


@RestController
public class UploadController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /*文件上传，以及获取请求中的命名空间和正则表达式。之后将文件保存到本地目录*/
    @PostMapping("/consumer/upload")
    public String upload(@RequestParam("namespace") String namespace,
                         @RequestParam("reg") String reg,
                         @RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            return "file is empty";
        }
        String fileName = file.getOriginalFilename();
        File dest = new File(uploadDir + fileName);
        try {
            //上传文件到特定路径
            file.transferTo(dest);
            //生产者-消费者模式的简单实现：将日志的上传和解析工作解耦
            FileTransQueue.offer(new FileEntity(namespace, fileName, reg));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "upload success!!";
    }


}
