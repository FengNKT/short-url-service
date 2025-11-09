package io.github.fengnkt.shorturlresolve.service.impl;

import io.github.fengnkt.shorturlresolve.entity.ResolveRecord;
import io.github.fengnkt.shorturlresolve.service.IResolveRecordService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ResolveRecordConsumer {
    @Resource
    ResolveRecordQueue resolveRecordQueue;
    @Resource
    ThreadPoolTaskExecutor recordExecutor;
    @Resource
    IResolveRecordService resolveRecordService;

    private volatile boolean running = true;
    private static final int CONSUMERS = 2;

    @PostConstruct
    public void startConsumers() {
        for (int i = 0; i < CONSUMERS; i++) {
            recordExecutor.execute(() -> {
                while (running) {
                    List<ResolveRecord> batch = new ArrayList<>(100);
                    ResolveRecord firstResolve = null;
                    try {
                        firstResolve = resolveRecordQueue.poll();
                    } catch (InterruptedException e) {
                        log.error("从队列获取记录失败", e);
                    }
                    if (firstResolve != null) {
                        batch.add(firstResolve);
                        resolveRecordQueue.drainTo(batch, 99); // 一次性取出剩余的
                    }
                    if (!batch.isEmpty()) {
                        boolean success = resolveRecordService.saveBatch(batch);
                        if (!success) {
                            log.error("保存解析记录失败，共 {} 条", batch.size());
                        }
                    }
                }
            });
        }
    }

    @PreDestroy
    public void stopConsumer() {
        log.info("应用即将关闭，正在保存剩余解析记录...");
        running = false;
        List<ResolveRecord> remaining = new ArrayList<>();
        resolveRecordQueue.drainTo(remaining);
        if (!remaining.isEmpty()) {
            boolean success = resolveRecordService.saveBatch(remaining);
            if (success) {
                log.info("剩余 {} 条解析记录已保存", remaining.size());
            } else {
                log.error("保存剩余解析记录失败，共 {} 条", remaining.size());
            }
        }
    }
}
