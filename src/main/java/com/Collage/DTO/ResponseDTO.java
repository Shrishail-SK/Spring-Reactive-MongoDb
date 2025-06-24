package com.Collage.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseDTO {

    private int statusCode;
    private String msg;
    private boolean success;
    private Object data;

    public ResponseDTO(int statusCode, boolean success, String msg) {
        this.statusCode = statusCode;
        this.success = success;
        this.msg = msg;
    }

    public ResponseDTO(int statusCode, boolean success, Object data) {
        this.statusCode = statusCode;
        this.success = success;
        this.data = data;
    }
    public ResponseDTO(int statusCode, boolean success, String msg,Object data) {
        this.statusCode = statusCode;
        this.success = success;
        this.msg = msg;
        this.data = data;
    }
}
