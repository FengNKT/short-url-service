package io.github.fengnkt.shorturlresolve.service.impl;

import io.github.fengnkt.shorturlresolve.entity.ResolveRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ResolveRecordQueue {
    private static final int QUEUE_CAPACITY = 10000;
    private final BlockingQueue<ResolveRecord> queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);

    public void add(ResolveRecord resolveRecord) {
        if (!queue.offer(resolveRecord)) {
            log.warn("ResolveRecordQueue已满，丢弃记录: {}", resolveRecord);
        }
    }

    public ResolveRecord poll() throws InterruptedException {
        return queue.poll(100, TimeUnit.MILLISECONDS);
    }

    public void drainTo(List<ResolveRecord> batch, int maxElements) {
        queue.drainTo(batch, maxElements);
    }

    public void drainTo(List<ResolveRecord> batch) {
        queue.drainTo(batch);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
