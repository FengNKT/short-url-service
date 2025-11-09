package io.github.fengnkt.shorturlresolve.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.fengnkt.shorturlresolve.dto.Result;
import io.github.fengnkt.shorturlresolve.dto.UrlMapFormDTO;
import io.github.fengnkt.shorturlresolve.entity.UrlMap;
import io.github.fengnkt.shorturlresolve.generator.ShortCodeGenerator;
import io.github.fengnkt.shorturlresolve.mapper.UrlMapMapper;
import io.github.fengnkt.shorturlresolve.service.IUrlMapService;
import io.github.fengnkt.shorturlresolve.utils.ShortCodeBloom;
import io.github.fengnkt.shorturlresolve.utils.ShortCodeValidator;
import io.github.fengnkt.shorturlresolve.utils.UrlValidator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static io.github.fengnkt.shorturlresolve.utils.RedisConstants.*;

@Slf4j
@Service
public class UrlMapServiceImpl extends ServiceImpl<UrlMapMapper, UrlMap> implements IUrlMapService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ShortCodeBloom shortCodeBloom;
    @Resource
    @Qualifier("autoIncrementShortCodeGenerator")
    private ShortCodeGenerator shortCodeGenerator;

    @Override
    @Cacheable(value = "urlMapCache", key = "#shortCode")
    public String getLongUrl(String shortCode) {
        // 1.检验短链码合法性
        if (!ShortCodeValidator.isValidShortCode(shortCode)) {
            return null;
        }

        // 2.查询布隆过滤器
        if (!shortCodeBloom.contains(shortCode)) {
            return null;
        }

        // 3.查询缓存
        String key = URL_MAP_KEY_PREFIX + shortCode;
        String longUrl = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(longUrl)) {
            return longUrl;
        }

        // 4.查询数据库
        UrlMap urlMap = query().eq("short_code", shortCode).one();
        if (urlMap == null) {
            stringRedisTemplate.opsForValue().set(key, "", URL_MAP_EMPTY_CACHE_DURATION);
            return null;
        }
        longUrl = urlMap.getLongUrl();

        // 5.更新缓存
        stringRedisTemplate.opsForValue().set(key, longUrl, URL_MAP_CACHE_DURATION);

        return longUrl;
    }

    @Override
    public Result addUrl(UrlMapFormDTO urlMapFormDTO) {
        // 1.检查数据是否合法
        String longUrl = urlMapFormDTO.getLongUrl();
        if (!UrlValidator.isValidUrl(longUrl)) {
            return Result.fail("链接不合法");
        }

        // 2.生成新短链
        String shortCode = shortCodeGenerator.generate();

        // 3.写入数据库
        UrlMap newUrlMap = new UrlMap();
        newUrlMap.setShortCode(shortCode);
        newUrlMap.setLongUrl(longUrl);
        newUrlMap.setUrlStatus("1");
        newUrlMap.setCreateTime(LocalDateTime.now(ZoneOffset.UTC));
        newUrlMap.setCreator(urlMapFormDTO.getCreator());
        newUrlMap.setDescription(urlMapFormDTO.getDescription());

        boolean success = save(newUrlMap);
        if (!success) {
            return Result.fail("保存失败");
        }

        // 4.写入布隆过滤器
        shortCodeBloom.add(shortCode);

        // 5.预热缓存
        String key = URL_MAP_KEY_PREFIX + shortCode;
        stringRedisTemplate.opsForValue().set(key, longUrl, URL_MAP_CACHE_DURATION);

        return Result.ok(shortCode);
    }

    @Override
    public Result removeMap(String shortCode) {
        UrlMap urlMap = query().eq("short_code", shortCode).one();
        if (urlMap == null) {
            return Result.fail("映射不存在");
        }
        removeById(urlMap.getId());
        String key = URL_MAP_KEY_PREFIX + shortCode;
        stringRedisTemplate.delete(key);
        return Result.ok("已删除映射");
    }

    @Override
    public void bloomFilterLoad() {
        List<UrlMap> urlMapList = query().list();
        for (UrlMap urlMap : urlMapList) {
            shortCodeBloom.add(urlMap.getShortCode());
        }
    }
}
