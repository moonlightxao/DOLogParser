package edu.dolp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.dolp.entity.Anomaly;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnomalyMapper extends BaseMapper<Anomaly> {
}
