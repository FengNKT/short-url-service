package io.github.fengnkt.shorturlresolve.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("url_map")
public class UrlMap {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String shortCode;

    private String longUrl;

    private String description;

    private String urlStatus;

    private LocalDateTime createTime;

    private String creator;
}
