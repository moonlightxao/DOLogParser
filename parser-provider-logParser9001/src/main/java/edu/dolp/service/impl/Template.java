package edu.dolp.service.impl;

import edu.dolp.entity.LogMesEntity;
import edu.dolp.entity.TemplateEntity;
import edu.dolp.service.TemplateService;
import edu.dolp.util.VectorUtil;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


public class Template extends TemplateEntity implements TemplateService {
    //wordLens属性保存日志模板的单词长度向量
    private RealVector vc;
    public Template(LogMesEntity message, Integer id){
        this.id = id;
        this.namespace = message.getNamespace();
        this.words = message.getMes();
        this.splitTemplate = words.split(" ");
        double[] wordLens = new double[splitTemplate.length];
        for(int i = 0;i < wordLens.length;i++){
            wordLens[i] = splitTemplate[i].length();
        }
        vc = VectorUtil.arrayToVector(wordLens, true);
    }

    public Template(LogMesEntity entity){
        this(entity, null);
    }


    @Override
    public double getSimilarityScore(LogMesEntity message) {
        String[] splitNewMessage = message.getLogStr();
        if(!splitTemplate[0].equals(splitNewMessage[0])){
            return 0.0d;
        }
        float accuracyScore = getAccuracyScore(message);
        if(Float.compare(accuracyScore, 1.0f) == 0){
            return 1.0d;
        }
        //这里存在一个技术难点，Tc和Tp动态配置如何同步更新到每一个Template上
        if(countSameWordPositions(message) < 3){
            return 0;
        }
        double cosineScore = getSimilarityScoreCosine(message);
        return cosineScore;
    }

    /**
     * @Description: 计算日志模板与新的日志消息之间单词长度向量的余弦相似度
     * @Param:  newMessage: 新的日志消息
     * @return:  cosine: 余弦相似度
     * @Author: Liu ZhiTian
     * @Date: 2022/4/1
     */
    @Override
    public double getSimilarityScoreCosine(LogMesEntity message) {
        RealVector v = message.getV();
        return vc.cosine(v);
    }

    /**
     * @Description:  计算日志模板与新的日志消息分类的准确率(通配符*可以匹配任意的单词)
     * @Param:  newMessage: 新的日志消息样本
     * @return:  准确率
     * @Author: Liu ZhiTian
     * @Date: 2022/4/1
     */
    @Override
    public float getAccuracyScore(LogMesEntity message) {
        String[] splitNewMessage = message.getLogStr();
        int cnt = 0, n = splitTemplate.length;
        //计算准确率
        for(int i = 0;i < n;i++){
            if(splitTemplate[i].equals("*")||splitTemplate[i].equals(splitNewMessage[i])){
                cnt++;
            }
        }
        return (1.0f * cnt) / (1.0f * n);
    }

    /**
    * @Description:  统计日志模板与日志消息相同token的数量
    * @Param:
    * @return:
    * @Author: Liu ZhiTian
    * @Date: 2022/4/1
    */
    @Override
    public int countSameWordPositions(LogMesEntity message) {
        int c = 0;
        String[] splitNewMessage = message.getLogStr();
        for(int i = 0;i < splitTemplate.length;i++){
            if(splitTemplate[i].equals(splitNewMessage[i])){
                c++;
            }
        }
        return c;
    }

    /**
    * @Description: 更新日志模板
    * @Param:
    * @return:
    * @Author: Liu ZhiTian
    * @Date: 2022/4/1
    */
    @Override
    public void update(LogMesEntity message) {
        StringBuilder sb = new StringBuilder();
        String[] splitNewMessage = message.getLogStr();
        int n = splitNewMessage.length;
        for(int i = 0;i < n;i++) {
            if (!splitTemplate[i].equals(splitNewMessage[i])) {
                splitTemplate[i] = new String("*");
            }
            if(i == n - 1){
                sb.append(splitTemplate[i]);
            }else{
                sb.append(splitTemplate[i] + " ");
            }
        }
        this.words = sb.toString();
    }
}
