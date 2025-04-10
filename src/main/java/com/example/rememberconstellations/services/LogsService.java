package com.example.rememberconstellations.services;

import com.example.rememberconstellations.exception.InvalidInputException;
import com.example.rememberconstellations.exception.LoggingException;
import com.example.rememberconstellations.exception.ResourceNotFoundException;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class LogsService {
    private static final String LOGS_DIR = "logs";
    private static final String LOGS_FILE_NAME = "app.log";
    private static final DateTimeFormatter INPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter LOG_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
