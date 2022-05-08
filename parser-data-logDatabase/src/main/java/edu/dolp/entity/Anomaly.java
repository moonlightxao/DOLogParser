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
@TableName(value = "anomaly")
public class Anomaly {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String namespace;

    private String label;

    public Anomaly(String namespace, String label){
        this.namespace = namespace;
        this.label = label;
    }
}
