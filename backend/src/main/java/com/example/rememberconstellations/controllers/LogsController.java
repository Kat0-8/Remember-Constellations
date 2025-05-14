package com.example.rememberconstellations.controllers;

import com.example.rememberconstellations.dtos.LogRequestDto;
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
@RequestMapping("/api/logs")
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

    @Operation(summary = "Request log file for specific date")
    @GetMapping("/request")
    public ResponseEntity<String> requestLogFileForDate(@RequestParam String date) {
        return ResponseEntity.ok(logsService.requestLogFileForDate(date));
    }

    @Operation(summary = "Check request's status by id")
    @GetMapping("/status")
    public ResponseEntity<LogRequestDto> checkRequestStatus(@RequestParam String requestId) {
        return ResponseEntity.ok(logsService.getLogRequestById(requestId));
    }

    @Operation(summary = "Download requested log file by request's id")
    @GetMapping("/download-requested")
    public ResponseEntity<Resource> downloadRequestedLogFile(@RequestParam String requestId) {
        Resource resource = logsService.getLogFileByRequestId(requestId);
        String fileName = logsService.getFileName(logsService.getLogRequestById(requestId).getDate());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
