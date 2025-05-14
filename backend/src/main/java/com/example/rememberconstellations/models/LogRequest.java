package com.example.rememberconstellations.models;

import com.example.rememberconstellations.utilities.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogRequest {
    private String id;
    private String date;
    private volatile RequestStatus status;
    private Resource resource;
    private String errorMessage;

    public LogRequest(String id, String date, RequestStatus status) {
        this.id = id;
        this.date = date;
        this.status = status;
    }
}
