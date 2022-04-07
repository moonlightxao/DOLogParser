package edu.dolp.entity;


import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.springframework.stereotype.Component;

public class LogMesEntity {
    //日志所属的命名空间
    private String namespace;
    //原始的日志消息
    private String mes;
    //将日志消息按空格划分成一个个字符串，方便算法处理
    private String[] logStr;
    //日志消息的单词长度向量
    private RealVector v;

    public LogMesEntity(String mes, String namespace){
        this.mes = mes;
        this.namespace = namespace;
        this.logStr = mes.split(" ");
        double[] wordsLen = new double[logStr.length];
        for(int i = 0;i < logStr.length;i++){
            wordsLen[i] = logStr[i].length();
        }
        //设置copyArray = true，复制一个新的double数组
        this.v = new ArrayRealVector(wordsLen, true);
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String[] getLogStr() {
        return logStr;
    }

    public void setLogStr(String[] logStr) {
        this.logStr = logStr;
    }

    public RealVector getV() {
        return v;
    }

    public void setV(RealVector v) {
        this.v = v;
    }
}
