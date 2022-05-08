package edu.dolp.listener;

import edu.dolp.entity.Anomaly;
import edu.dolp.entity.LogEntity;
import edu.dolp.mapper.AnomalyMapper;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RocketMQMessageListener(topic = "detect-result-topic", consumerGroup = "result-group")
public class AnomalyListener implements RocketMQListener<LogEntity> {

    @Resource
    private AnomalyMapper anomalyMapper;

    @Override
    public void onMessage(LogEntity message) {
        System.out.println("日志源 " + message.getNamespace() + " 准备更新到数据库");
        String namespace = message.getNamespace();
        String[] abnormalLogs = message.getLogMessage();
        int count = 0;
        for (String log : abnormalLogs) {
            count += anomalyMapper.insert(new Anomaly(namespace, log));
        }
        if(count != abnormalLogs.length){
            System.out.println("理论插入数量 " + abnormalLogs.length + " , 实际数量" + count);
        }
    }
}
