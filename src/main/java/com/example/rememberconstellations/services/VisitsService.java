package com.example.rememberconstellations.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class VisitsService {
    private final Map<String, LongAdder> urlVisitsCounters = new ConcurrentHashMap<>();

    public long getCounter(String url) {
        log.info("Getting counter for url: {}", url);
        return urlVisitsCounters.getOrDefault(url, new LongAdder()).sum();
    }

    public Map<String, Long> getAllCounters() {
        log.info("Getting all counters");
        return urlVisitsCounters.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        c -> c.getValue().sum()));
    }

    public void incrementCount(String url) {
        log.info("Incrementing counter for url: {}", url);
        urlVisitsCounters.computeIfAbsent(url, u -> new LongAdder()).increment();
    }

    @Transactional
    public void resetCounters() {
        log.info("Resetting counters");
        urlVisitsCounters.clear();
    }
}
