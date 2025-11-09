package io.github.fengnkt.shorturlresolve.generator.impl;

import io.github.fengnkt.shorturlresolve.generator.ShortCodeGenerator;
import io.github.fengnkt.shorturlresolve.service.ISegmentGenerator;
import io.github.fengnkt.shorturlresolve.utils.Base62Encoder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component("autoIncrementShortCodeGenerator")
public class AutoIncrementShortCodeGenerator implements ShortCodeGenerator {
    @Resource
    private ISegmentGenerator segmentGenerator;

    private final AtomicLong current = new AtomicLong(0);
    private volatile long maxId = 0;

    private static final long SEGMENT_SIZE = 10000L;

    private static final long MOD = 1L << 32;
    private static final long MULTIPLIER = 1580030173L;
    private static final long INCREMENT = 59260789L;

    @Override
    public String generate() {
        long id = current.getAndIncrement();
        if (id >= maxId) {
            synchronized (this) {
                if (current.get() >= maxId) {
                    long newMax = segmentGenerator.nextSegment(SEGMENT_SIZE);
                    current.set(newMax - SEGMENT_SIZE);
                    maxId = newMax;
                }
                id = current.getAndIncrement();
            }
        }
        id = (id * MULTIPLIER + INCREMENT) % MOD;
        return Base62Encoder.encode(id);
    }

}
