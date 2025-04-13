package com.example.rememberconstellations.dtos;

import com.example.rememberconstellations.utilities.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class LogRequestDto {
    private String id;
    private String date;
    private RequestStatus status;
    private String errorMessage;
}
