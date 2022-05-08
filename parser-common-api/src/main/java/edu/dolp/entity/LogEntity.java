package edu.dolp.entity;


import java.io.Serializable;
import java.util.Arrays;

public class LogEntity implements Serializable {

    //约定namespace结构: (host.port.type)
    //host和port唯一确定一个应用，type为该应用产生的日志类型
    private String namespace;
    private String[] logMessage;
    private String method;
    public LogEntity(){

    }

    public LogEntity(String namespace, String method, String...logMessage){
        this.namespace = namespace;
        this.method = method;
        this.logMessage = logMessage;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String[] getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String[] logMessage) {
        this.logMessage = logMessage;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "LogEntity{" +
                "namespace='" + namespace + '\'' +
                ", logMessage=" + Arrays.toString(logMessage) +
                ", method='" + method + '\'' +
                '}';
    }
}