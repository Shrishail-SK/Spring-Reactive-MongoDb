package com.Collage.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    private Integer statusCode;
    private String param;
    private String value;
    private String msg;

    public ErrorDto(String param, String value, String msg) {
        this.param = param;
        this.value = value;
        this.msg = msg;
    }

    public ErrorDto(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.msg = message;
    }
}
