package io.github.fengnkt.shorturlresolve.controller;

import io.github.fengnkt.shorturlresolve.dto.Result;
import io.github.fengnkt.shorturlresolve.dto.UrlMapFormDTO;
import io.github.fengnkt.shorturlresolve.entity.ResolveRecord;
import io.github.fengnkt.shorturlresolve.service.IUrlMapService;
import io.github.fengnkt.shorturlresolve.service.impl.ResolveRecordQueue;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@RestController
@RequestMapping("/url-map")
public class UrlMapController {
    @Resource
    IUrlMapService urlMapService;
    @Resource
    ResolveRecordQueue resolveRecordQueue;

    @PostMapping("/add-map")
    public Result addMap(@RequestBody UrlMapFormDTO urlMapFormDTO) {
        return urlMapService.addUrl(urlMapFormDTO);
    }

    @GetMapping("/remove-map")
    public Result removeMap(@RequestParam("short_code") String shortCode) {
        return urlMapService.removeMap(shortCode);
    }

    @GetMapping("/bloom-filter-load")
    public void bloomFilterLoad() {
        urlMapService.bloomFilterLoad();
    }

    @GetMapping("/resolve/{short_code}")
    public void redirect(@PathVariable("short_code") String shortCode, HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        String longUrl = urlMapService.getLongUrl(shortCode);
        if (longUrl == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found.");
        }
        ResolveRecord resolveRecord = new ResolveRecord(shortCode, request.getRemoteAddr(), LocalDateTime.now(ZoneOffset.UTC));
        resolveRecordQueue.add(resolveRecord);
        response.sendRedirect(longUrl);
    }
}
