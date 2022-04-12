package edu.dolp.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@TableName(value = "templates")
public class Template {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String namespace;
    private String words;

    public Template(String namespace, String words){
        this.namespace = namespace;
        this.words = words;
    }
}
