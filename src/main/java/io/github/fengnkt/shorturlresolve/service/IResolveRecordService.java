package io.github.fengnkt.shorturlresolve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.fengnkt.shorturlresolve.dto.Result;
import io.github.fengnkt.shorturlresolve.entity.ResolveRecord;

public interface IResolveRecordService extends IService<ResolveRecord> {
    Result statsByShortCode(String shortCode);
}
