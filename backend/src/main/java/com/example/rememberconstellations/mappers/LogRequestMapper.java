package com.example.rememberconstellations.mappers;

import com.example.rememberconstellations.dtos.LogRequestDto;
import com.example.rememberconstellations.models.LogRequest;
import org.springframework.stereotype.Component;

@Component
public class LogRequestMapper {

    public LogRequestDto mapToDto(LogRequest logRequest) {
        return new LogRequestDto(
                logRequest.getId(),
                logRequest.getDate(),
                logRequest.getStatus(),
                logRequest.getErrorMessage());
    }

    public LogRequest mapToEntity(LogRequestDto logRequestDto) {
        return new LogRequest(
                logRequestDto.getId(),
                logRequestDto.getDate(),
                logRequestDto.getStatus(),
                null,
                logRequestDto.getErrorMessage());
    }
}
