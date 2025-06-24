package com.Collage.Exception;

import com.Collage.DTO.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationException {

    @org.springframework.web.bind.annotation.ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ResponseDTO>> handleValidationException(WebExchangeBindException ex) {
        String errorMessages = ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ResponseDTO response = new ResponseDTO(HttpStatus.BAD_REQUEST.value(), false, errorMessages);
        return Mono.just(ResponseEntity.badRequest().body(response));
    }
}
