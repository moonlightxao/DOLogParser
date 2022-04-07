package edu.dolp.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName(value = "templates")
public class Template {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String namespace;
    private String words;
}
