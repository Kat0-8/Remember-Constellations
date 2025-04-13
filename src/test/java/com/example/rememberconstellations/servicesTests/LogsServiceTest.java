package com.example.rememberconstellations.servicesTests;

import com.example.rememberconstellations.exceptions.InvalidInputException;
import com.example.rememberconstellations.exceptions.LoggingException;
import com.example.rememberconstellations.exceptions.ResourceNotFoundException;
import com.example.rememberconstellations.mappers.LogRequestMapper;
import com.example.rememberconstellations.services.LogsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LogsServiceTest {

    @TempDir
    private Path tempDir;
    private LogsService logsService;
    private Path logFilePath;

    @BeforeEach
    void setUp() throws IOException {
        logsService = new TestLogsService(new LogRequestMapper());
        logFilePath = tempDir.resolve("app.log");
        Files.createDirectories(tempDir);
    }

    // Helper class to override log file location
    private class TestLogsService extends LogsService {
        public TestLogsService(LogRequestMapper logRequestMapper) {
            super(logRequestMapper);
        }

        @Override
        public Path getLogFilePath() {
            return logFilePath;
        }
    }

    @Test
    void getLogFileForDate_ValidLogs_ReturnsFilteredResource() throws IOException {
        // Arrange
        String testDate = "01.01.2024";
        String logContent = """
                2024-01-01 [INFO] System start
                2024-01-01 [DEBUG] Initialization complete
                2024-01-02 [ERROR] Connection failed
                """;
        Files.writeString(logFilePath, logContent);

        // Act
        Resource resource = logsService.getLogFileForDate(testDate);
        String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        // Assert
        assertAll(
                () -> assertTrue(content.contains("System start")),
                () -> assertTrue(content.contains("Initialization complete")),
                () -> assertFalse(content.contains("Connection failed")),
                () -> assertEquals(2, content.split(System.lineSeparator()).length)
        );
    }

    @Test
    void getLogFileForDate_MissingFile_ThrowsResourceNotFound() {
        assertThrows(ResourceNotFoundException.class, () ->
                logsService.getLogFileForDate("01.01.2024")
        );
    }

    @Test
    void getLogFileForDate_NoMatchingEntries_ThrowsResourceNotFound() throws IOException {
        Files.writeString(logFilePath, "2024-01-02 [INFO] Test message\n");
        assertThrows(ResourceNotFoundException.class, () ->
                logsService.getLogFileForDate("01.01.2024")
        );
    }

    @Test
    void getLogFileForDate_ReadError_ThrowsLoggingException() throws IOException {
        // Create directory instead of file to force read error
        Files.createDirectory(logFilePath);
        assertThrows(LoggingException.class, () ->
                logsService.getLogFileForDate("01.01.2024")
        );
    }

    @Test
    void getFileName_ValidDate_ReturnsFormattedName() {
        String fileName = logsService.getFileName("31.12.2024");
        assertEquals("app-2024-12-31.log", fileName);
    }

    @Test
    void getFileName_InvalidDate_ThrowsException() {
        assertThrows(InvalidInputException.class, () ->
                logsService.getFileName("2024-12-31")
        );
    }

    @Test
    void parseDate_InvalidFormat_ThrowsException() {
        assertThrows(InvalidInputException.class, () ->
                logsService.getFileName("2024/07/15")
        );
    }

    @Test
    void parseDate_InvalidDate_ThrowsException() {
        assertThrows(InvalidInputException.class, () ->
                logsService.getLogFileForDate("32.13.2024")
        );
    }
}
