package io.github.fengnkt.shorturlresolve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.fengnkt.shorturlresolve.entity.SegmentGenerator;

public interface ISegmentGenerator extends IService<SegmentGenerator> {
    long nextSegment(long step);
}
