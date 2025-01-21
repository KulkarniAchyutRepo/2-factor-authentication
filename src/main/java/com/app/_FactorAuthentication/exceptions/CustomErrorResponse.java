package com.app._FactorAuthentication.exceptions;

import java.time.LocalDateTime;

public class CustomErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "CustomErrorResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
