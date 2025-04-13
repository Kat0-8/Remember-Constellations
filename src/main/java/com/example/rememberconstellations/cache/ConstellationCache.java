package com.example.rememberconstellations.cache;

import com.example.rememberconstellations.dtos.ConstellationDto;
import org.springframework.stereotype.Component;

@Component
public class ConstellationCache extends InMemoryCache<Integer, ConstellationDto> {
    public ConstellationCache() {
        super();
    }
}
