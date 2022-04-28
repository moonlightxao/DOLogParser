package edu.dolp.listener;

import cn.hutool.json.JSONObject;
import edu.dolp.entity.LogEntity;
import edu.dolp.service.MQService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@RocketMQMessageListener(topic = "MoLFI-topic", consumerGroup = "MoLFI-group")
public class MessageListener implements RocketMQListener<LogEntity> {
    @Resource
    private MQService mqService;

    @Value("${logfile.absolutePath}")
    private String logFilePath;

    @Value("${logfile.column}")
    private String column;

    @Value("${logfile.pythonFilePath}")
    private String pythonFilePath;

    @Override
    public void onMessage(LogEntity message) {
        JSONObject obj = new JSONObject();
        List<String> templates = new ArrayList<>();
        String namespace = message.getNamespace();
        //这里需要额外的措施确保每次产生的都是不同的日志文件
        String localLogFilePath = logFilePath + namespace + ".log";
        //日志文件写入部署机器的本地目录
        try(FileWriter fileWriter = new FileWriter(localLogFilePath,true)){
            for(int i = 0;i < message.getLogMessage().length;i++){
                fileWriter.append(message.getLogMessage()[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //调用python脚本
        Process proc;
        try{
            String[] args1 = new String[]{"python",
                                    pythonFilePath,
                                              "-l",
                                  localLogFilePath,
                                              "-s",
                                         "\"\\n\"",
                                              "-c",
                                               column};
            proc = Runtime.getRuntime().exec(args1);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while((line = in.readLine()) != null){
                System.out.println(line);
                templates.add(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //将日志模板异步发送到消息队列
//        obj.put("status", 200);
//        obj.put("namespace", namespace);
//        List<JSONObject> ts = new ArrayList<>();
//        for(String t : templates){
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id", null);
//            jsonObject.put("words", t);
//            ts.add(jsonObject);
//        }
//        obj.put("templates", ts);
//        mqService.updateTemplates(obj.toStringPretty());
    }
}
