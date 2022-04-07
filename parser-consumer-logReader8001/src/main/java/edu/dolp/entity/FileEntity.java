package edu.dolp.entity;

import org.springframework.stereotype.Component;

public class FileEntity {
    private String namespace;
    private String fileName;
    private String reg;
    public FileEntity(){}

    public FileEntity(String namespace, String fileName){
        this(namespace, fileName, null);
    }

    public FileEntity(String namespace, String fileName, String reg) {
        this.namespace = namespace;
        this.fileName = fileName;
        this.reg = reg;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }
}
