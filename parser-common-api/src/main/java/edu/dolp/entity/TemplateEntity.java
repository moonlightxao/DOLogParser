package edu.dolp.entity;

import java.io.Serializable;

public class TemplateEntity implements Serializable {
    protected Integer id;
    protected String namespace;           //index属性保存该日志模板的索引，用于快速从所有日志模板中定位特定的日志模板
    protected String words;              //words属性保存该日志模板
    protected String[] splitTemplate;    //splitTemplate属性保存将改日志信息按照空格划分出的一个个字符串，方便算法处理

    public TemplateEntity(){}

    public TemplateEntity(int id, String namespace, String words, String[] splitTemplate) {
        this.id = id;
        this.namespace = namespace;
        this.words = words;
        this.splitTemplate = splitTemplate;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String[] getSplitTemplate() {
        return splitTemplate;
    }

    public void setSplitTemplate(String[] splitTemplate) {
        this.splitTemplate = splitTemplate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
