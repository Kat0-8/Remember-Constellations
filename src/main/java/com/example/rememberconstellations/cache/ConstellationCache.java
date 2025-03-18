package com.example.rememberconstellations.cache;

import com.example.rememberconstellations.dto.ConstellationDto;
import org.springframework.stereotype.Component;

@Component
public class ConstellationCache extends InMemoryCache<Integer, ConstellationDto> {
    public ConstellationCache() {
        super();
    }
}
