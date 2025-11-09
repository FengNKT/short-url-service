package io.github.fengnkt.shorturlresolve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.fengnkt.shorturlresolve.dto.Result;
import io.github.fengnkt.shorturlresolve.dto.UrlMapFormDTO;
import io.github.fengnkt.shorturlresolve.entity.UrlMap;

public interface IUrlMapService extends IService<UrlMap> {
    String getLongUrl(String shortUrl);

    Result addUrl(UrlMapFormDTO urlMapFormDTO);

    Result removeMap(String shortCode);

    void bloomFilterLoad();
}
