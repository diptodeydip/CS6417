package com.example.OnlineMarketPlace.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    private final Cache<String, Integer> cache;

    public CacheService(Cache<String, Integer> cache) {
        this.cache = cache;
    }

    public void putData(String key, Integer value) {
        cache.put(key, value);
    }

    public Integer getData(String key) {
        return cache.getIfPresent(key);  // Returns null if not present
    }

    public void evictData(String key) {
        cache.invalidate(key);
    }
}
