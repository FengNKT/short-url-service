package io.github.fengnkt.shorturlresolve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("resolve_record")
public class ResolveRecord {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String shortCode;

    private String clientIp;

    private LocalDateTime recordTime;

    public ResolveRecord(String shortCode, String clientIp, LocalDateTime recordTime) {
        this.shortCode = shortCode;
        this.clientIp = clientIp;
        this.recordTime = recordTime;
    }
}
