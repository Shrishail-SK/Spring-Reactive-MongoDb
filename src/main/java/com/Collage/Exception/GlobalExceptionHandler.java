package com.Collage.Exception;


import com.Collage.DTO.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ResponseDTO>> handleGlobalException(Exception ex) {
        ResponseDTO errorResponse = new ResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                false,
                ex.getMessage()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<ResponseDTO>> handleResourceNotFound(ResourceNotFoundException ex) {
        ResponseDTO errorResponse = new ResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                false,
                ex.getMessage()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse));
    }
}
