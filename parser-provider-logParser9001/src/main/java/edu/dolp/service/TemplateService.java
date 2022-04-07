package edu.dolp.service;

import edu.dolp.entity.LogMesEntity;
import org.springframework.stereotype.Service;

@Service
public interface TemplateService {
    double getSimilarityScore(LogMesEntity message);
    double getSimilarityScoreCosine(LogMesEntity message);
    float getAccuracyScore(LogMesEntity message);
    int countSameWordPositions(LogMesEntity message);
    void update(LogMesEntity message);
}
