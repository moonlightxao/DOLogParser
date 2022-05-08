package edu.dolp.listener;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import edu.dolp.entity.LogEntity;
import edu.dolp.service.MQService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@RocketMQMessageListener(topic = "Detect-topic", consumerGroup = "Detect-group")
public class MessageListener implements RocketMQListener<LogEntity> {
    @Resource
    private MQService mqService;

    @Value("${python-config.module-name}")
    private String moduleName;

    @Value("${python-config.exp-id}")
    private String expId;

    @Override
    public void onMessage(LogEntity message) {
        System.out.println("日志源 " + message.getNamespace() + " 准备进行异常检测");
        String[] ms = message.getNamespace().split("\\.");
        if(ms == null||ms.length == 0){
            System.out.println("数组为空或者数组大小为0错误！");
            return;
        }
        String logsType = ms[ms.length - 1];
        //调用LogClass进行日志异常检测
        try{
            String[] args1 = new String[]{"python",
                    "-m",
                    moduleName,
                    "--logs_type",
                    logsType,
                    "--id",
                    expId,
                    "--train"};
            Runtime.getRuntime().exec(args1);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("日志源 " + message.getNamespace() + " 异常检测完成，准备读取结果");
        //读取检测完成产生的txt文本
        String fileDir = "D:\\programming\\LogClass-master\\output\\logclass_" +
                      logsType + "_20220505\\results\\result.txt";
        List<String> list = new ArrayList<>();
        File file = new File(fileDir);
        if(file.isFile()&&file.exists()){
            try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))){
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    list.add(line);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        String[] words = new String[list.size()];
        for(int i = 0;i < list.size();i++){
            words[i] = list.get(i);
        }
        message.setLogMessage(words);
        System.out.println("日志源 " + message.getNamespace() + " 读取结果完成，准备将消息发送到消息队列");
        String res = mqService.saveAnomalousLogs(message);
        JSONObject obj = JSONUtil.parseObj(res);
        Integer status = obj.get("status", Integer.class);
        switch (status){
            case 200:
                System.out.println("检测结果上传成功");
                break;
            case 500:
                System.out.println("检测结果上传失败");
                break;
            default:
                System.out.println("正在上传");
        }
    }
}
