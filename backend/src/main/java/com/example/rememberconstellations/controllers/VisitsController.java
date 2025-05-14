package com.example.rememberconstellations.controllers;

import com.example.rememberconstellations.services.VisitsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Visits API", description = "Get visits counter by URL")
@RestController
@RequestMapping("/api/visits")
public class VisitsController {
    private final VisitsService visitsService;

    public VisitsController(VisitsService visitsService) {
        this.visitsService = visitsService;
    }

    @Operation(summary = "Get counter for URL")
    @GetMapping("/{url}")
    public Long getCounter(@PathVariable String url) {
        String fullPath = String.join("/", "", url);
        return visitsService.getCounter(fullPath);
    }

    @Operation(summary = "Get counters for all URLs")
    @GetMapping("")
    public Map<String, Long> getAllCounters() {
        return visitsService.getAllCounters();
    }

    @Operation(summary = "Reset all counters")
    @DeleteMapping("")
    public void resetCounters() {
        visitsService.resetCounters();
    }
}
