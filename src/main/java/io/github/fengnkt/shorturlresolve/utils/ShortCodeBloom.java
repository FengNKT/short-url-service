package io.github.fengnkt.shorturlresolve.utils;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class ShortCodeBloom {
    private final RBloomFilter<String> bloomFilter;

    public ShortCodeBloom(RedissonClient redissonClient) {
        bloomFilter = redissonClient.getBloomFilter("shortCodeBloom");
        bloomFilter.tryInit(1_000_000L, 0.01);
    }

    public void add(String key) {
        bloomFilter.add(key);
    }

    public boolean contains(String key) {
        return bloomFilter.contains(key);
    }

}
