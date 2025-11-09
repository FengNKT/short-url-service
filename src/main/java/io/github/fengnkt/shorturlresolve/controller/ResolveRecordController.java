package io.github.fengnkt.shorturlresolve.controller;

import io.github.fengnkt.shorturlresolve.dto.Result;
import io.github.fengnkt.shorturlresolve.service.IResolveRecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class ResolveRecordController {
    @Resource
    IResolveRecordService resolveRecordService;

    @GetMapping("/stats-by-short-code/{short_code}")
    public Result statsByShortCode(@PathVariable String short_code) {
        return resolveRecordService.statsByShortCode(short_code);
    }
}
