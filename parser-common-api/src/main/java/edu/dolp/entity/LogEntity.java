package edu.dolp.entity;


import java.io.Serializable;
import java.util.Arrays;

public class LogEntity implements Serializable {

    private String namespace;
    private String[] logMessage;
    public LogEntity(){

    }


    public LogEntity(String namespace, String...logMessage){
        this.namespace = namespace;
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

    @Override
    public String toString() {
        return "LogEntity{" +
                "namespace='" + namespace + '\'' +
                ", logMessage=" + Arrays.toString(logMessage) +
                '}';
    }
}