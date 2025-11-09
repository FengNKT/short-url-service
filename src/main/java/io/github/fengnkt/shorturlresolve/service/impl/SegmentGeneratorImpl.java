package io.github.fengnkt.shorturlresolve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.fengnkt.shorturlresolve.entity.SegmentGenerator;
import io.github.fengnkt.shorturlresolve.mapper.SegmentGeneratorMapper;
import io.github.fengnkt.shorturlresolve.service.ISegmentGenerator;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SegmentGeneratorImpl extends ServiceImpl<SegmentGeneratorMapper, SegmentGenerator> implements ISegmentGenerator {
    @Resource
    SegmentGeneratorMapper segmentGeneratorMapper;

    @Transactional
    @Override
    public long nextSegment(long segmentSize) {
        segmentGeneratorMapper.updateMaxId(segmentSize);
        return segmentGeneratorMapper.getMaxId();
    }
}
