package io.github.fengnkt.shorturlresolve.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("id_generator")
public class SegmentGenerator {
    @TableId
    private String name;

    private Long maxId;
}
