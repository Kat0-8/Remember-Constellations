package com.example.rememberconstellations.cache;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import lombok.Getter;

public class InMemoryCache<K, V> {

    private static final Duration TTL = Duration.ofHours(1);
    private static final Duration CLEANER_INTERVAL = Duration.ofMinutes(10);
    private static final int MAX_CACHE_SIZE = 40;

    private final LinkedHashMap<K, CacheEntry<V>> cache;

    public InMemoryCache() {
        this.cache = new LinkedHashMap<>(MAX_CACHE_SIZE, 0.75f, true);
        startCacheCleaner();
    }

    public InMemoryCache(Integer maxCacheSize) {
        int cacheSize;
        cacheSize = Objects.requireNonNullElse(maxCacheSize, MAX_CACHE_SIZE);
        this.cache = new LinkedHashMap<>(cacheSize, 0.75f, true);
        startCacheCleaner();
    }

    @Getter
    private static class CacheEntry<V> {
        private V value;
        private Instant timestamp;

        public CacheEntry(V value) {
            this.value = value;
            this.timestamp = Instant.now();
        }

        public void updateTimestamp() {
            this.timestamp = Instant.now();
        }
    }

    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry != null) {
            entry.updateTimestamp();
            return entry.getValue();
        } else {
            return null;
        }
    }

    public void put(K key, V value) {
        if (cache.size() >= MAX_CACHE_SIZE) {
            evictOldestEntry();
        }
        cache.put(key, new CacheEntry<>(value));
    }

    public boolean contains(K key) {
        return cache.containsKey(key) && !isExpired(cache.get(key));
    }

    public void clear() {
        cache.clear();
    }

    public void remove(K key) {
        cache.remove(key);
    }

    private boolean isExpired(CacheEntry<V> entry) {
        return Duration.between(entry.getTimestamp(), Instant.now()).compareTo(TTL) > 0;
    }

    private void cleanExpiredEntries() {
        cache.entrySet().removeIf(entry -> isExpired(entry.getValue()));
    }

    private void startCacheCleaner() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                cleanExpiredEntries();
            }
        }, 0, CLEANER_INTERVAL.toMillis());
    }

    private void evictOldestEntry() {
        Iterator<Map.Entry<K, CacheEntry<V>>> iterator = cache.entrySet().iterator();
        if (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }
}

