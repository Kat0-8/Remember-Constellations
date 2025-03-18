package com.example.rememberconstellations.cache;

import com.example.rememberconstellations.dto.StarDto;
import org.springframework.stereotype.Component;

@Component
public class StarCache extends InMemoryCache<Integer, StarDto> {
    public StarCache() {
        super();
    }
}
