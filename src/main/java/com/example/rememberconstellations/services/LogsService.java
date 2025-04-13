package com.example.rememberconstellations.services;

import com.example.rememberconstellations.dtos.LogRequestDto;
import com.example.rememberconstellations.exceptions.InvalidInputException;
import com.example.rememberconstellations.exceptions.LoggingException;
import com.example.rememberconstellations.exceptions.ResourceNotFoundException;
import com.example.rememberconstellations.mappers.LogRequestMapper;
import com.example.rememberconstellations.models.LogRequest;
import com.example.rememberconstellations.utilities.enums.RequestStatus;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogsService {
    private static final String LOGS_DIR = "logs";
    private static final String LOGS_FILE_NAME = "app.log";
    private static final DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter LOG_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Map<String, LogRequest> logRequestMap = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final LogRequestMapper logRequestMapper;

    @Autowired
    public LogsService(LogRequestMapper logRequestMapper) {
        this.logRequestMapper = logRequestMapper;
    }

    /* READ */

    public Resource getLogFileForDate(String date) {
        LocalDate parsedDate = parseDate(date);
        String formattedDate = parsedDate.format(LOG_DATE_FORMATTER);
        Path logFilePath = getLogFilePath();
        if (!Files.exists(logFilePath)) {
            throw new ResourceNotFoundException("Log file " + logFilePath + " was not found");
        }
        List<String> requiredLogs = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(logFilePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(formattedDate)) {
                    requiredLogs.add(line);
                }
            }
        } catch (IOException exception) {
            throw new LoggingException("Error reading log file");
        }
        if (requiredLogs.isEmpty()) {
            throw new ResourceNotFoundException("No logs were found for date: " + date);
        }
        String contentOfFile = String.join(System.lineSeparator(), requiredLogs);
        return new ByteArrayResource(contentOfFile.getBytes(StandardCharsets.UTF_8));
    }

    public String requestLogFileForDate(String date) {
        log.info("Creating request for log file for date: {}", date);
        String requestId = UUID.randomUUID().toString();
        LogRequest logRequest = new LogRequest(requestId, date, RequestStatus.IN_PROGRESS);
        logRequestMap.put(requestId, logRequest);
        log.info("Log request with id {} created for date: {}", requestId, date);
        executor.submit(() -> {
            try {
                Resource logFile = getLogFileForDate(date);
                logRequest.setResource(logFile);
                logRequest.setStatus(RequestStatus.COMPLETED);
                log.info("Log request with id {} for date {} was successful", requestId, date);
            } catch (Exception exception) {
                logRequest.setStatus(RequestStatus.FAILED);
                logRequest.setErrorMessage(exception.getMessage());
                log.error("Log request with id {} for date {} failed: {}", requestId, date, exception.getMessage());
            }
        });
        return requestId;
    }

    public LogRequestDto getLogRequestById(String requestId) {
        LogRequest logRequest = logRequestMap.get(requestId);
        if (logRequest == null) {
            throw new ResourceNotFoundException("Log request with id " + requestId + " was not found");
        }
        return logRequestMapper.mapToDto(logRequest);
    }

    public Resource getLogFileByRequestId(String requestId) {
        LogRequest logRequest = logRequestMap.get(requestId);
        if (logRequest == null) {
            throw new ResourceNotFoundException("Log request with id " + requestId + " was not found");
        }
        switch (logRequest.getStatus()) {
            case IN_PROGRESS:
                throw new LoggingException("Log request with id " + requestId + " is in progress");
            case COMPLETED:
                break;
            case FAILED:
                throw new LoggingException("Log request with id " + requestId + " failed");
            default:
                throw new LoggingException("Log request with id " + requestId + " has unknown status");
        }
        return logRequest.getResource();
    }

    public String getFileName(String date) {
        LocalDate parsedDate = parseDate(date);
        return String.format("app-%s.log", parsedDate.format(LOG_DATE_FORMATTER));
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, INPUT_DATE_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new InvalidInputException("Invalid date format. Valid format is dd.MM.yyyy");
        }

    }

    public Path getLogFilePath() {
        return Paths.get(LOGS_DIR, LOGS_FILE_NAME);
    }
}
