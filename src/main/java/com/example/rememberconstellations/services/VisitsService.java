package com.example.rememberconstellations.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class VisitsService {
    private final Map<String, AtomicInteger> urlVisitsCounters = new ConcurrentHashMap<>();

    @Transactional
    public int getCounter(String url) {
        log.info("Getting counter for url: {}", url);
        return urlVisitsCounters.getOrDefault(url, new AtomicInteger(0)).get();
    }

    @Transactional
    public Map<String, Integer> getAllCounters() {
        log.info("Getting all counters");
        return urlVisitsCounters.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        c -> c.getValue().get()));
    }

    @Transactional
    public void incrementCount(String url) {
        log.info("Incrementing counter for url: {}", url);
        urlVisitsCounters.computeIfAbsent(url, u -> new AtomicInteger(0)).getAndIncrement();
    }

    @Transactional
    public void resetCounters() {
        log.info("Resetting counters");
        urlVisitsCounters.clear();
    }
}
