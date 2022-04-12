package edu.dolp.service.impl;

import edu.dolp.entity.LogEntity;
import edu.dolp.entity.LogMesEntity;
import edu.dolp.entity.TemplateEntity;
import edu.dolp.service.ManageService;
import javafx.util.Pair;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

@Component
public class ManageServiceImpl implements ManageService {
    private List<Template> templates;

    /**
    * @Description: 根据日志消息进行解析，得到其对应的模板
    * @Param:  日志消息
    * @return:  日志模板
    * @Author: Liu ZhiTian
    * @Date: 2022/4/3
    */
    @Override
    public void inferTemplate(LogMesEntity log) {
        int len = log.getLogStr().length;
        Queue<Pair<Template, Double>> candidates = new PriorityQueue<>((o1, o2) -> Double.compare(o2.getValue(), o1.getValue()));
        for(Template template: templates){
            if(template.getSplitTemplate().length != len){
                continue;
            }
            double score = template.getSimilarityScore(log);
            if(Double.compare(score, 0.9d) < 0) continue;
            candidates.offer(new Pair(template, score));
        }
        if(!candidates.isEmpty()){
            Template template = candidates.peek().getKey();
            template.update(log);
        }else{
            Template nTemplate = new Template(log);
            templates.add(nTemplate);
        }
    }


    @Override
    public List<LogMesEntity> transLog(LogEntity logs) {
        List<LogMesEntity> list = new ArrayList<>();
        for(int i = 0;i < logs.getLogMessage().length;i++){
            list.add(new LogMesEntity(logs.getLogMessage()[i], logs.getNamespace()));
        }
        return list;
    }

    public void setTemplates(List<Template> templates){
        this.templates = templates;
    }

    public List<Template> getTemplates(){
        return this.templates;
    }
}
