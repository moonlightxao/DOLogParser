package edu.dolp.entity;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "visit")
public class Visit {
    @TableId
    private String namespace;
    @OrderBy(isDesc = true)
    private Integer count;
}
