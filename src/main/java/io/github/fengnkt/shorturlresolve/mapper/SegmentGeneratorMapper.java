package io.github.fengnkt.shorturlresolve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.fengnkt.shorturlresolve.entity.SegmentGenerator;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface SegmentGeneratorMapper extends BaseMapper<SegmentGenerator> {
    @Update("UPDATE segment_generator SET max_id = max_id + #{segmentSize} WHERE name='short_code'")
    void updateMaxId(@Param("segmentSize") long segmentSize);

    @Select("SELECT max_id FROM segment_generator WHERE name='short_code'")
    long getMaxId();
}
