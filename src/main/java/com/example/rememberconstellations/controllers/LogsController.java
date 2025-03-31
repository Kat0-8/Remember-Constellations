package com.example.rememberconstellations.controllers;

import com.example.rememberconstellations.services.LogsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Log API", description = "Get logs")
@RestController
@RequestMapping("logs")
public class LogsController {
    private final LogsService logsService;

    @Autowired
    public LogsController(LogsService logsService) {
        this.logsService = logsService;
    }

    @Operation(summary = "Download log file for specific date")
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadLogFileForDate(@RequestParam String date) {
        Resource resource = logsService.getLogFileForDate(date);
        String fileName = logsService.getFileName(date);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
