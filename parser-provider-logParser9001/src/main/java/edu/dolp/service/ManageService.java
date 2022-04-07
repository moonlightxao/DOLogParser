package edu.dolp.service;

import edu.dolp.entity.LogEntity;
import edu.dolp.entity.LogMesEntity;
import edu.dolp.entity.TemplateEntity;
import edu.dolp.service.impl.Template;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ManageService {
    void inferTemplate(LogMesEntity message);
    List<LogMesEntity> transLog(LogEntity logs);
    void setTemplates(List<Template> templates);
    List<Template> getTemplates();
}
