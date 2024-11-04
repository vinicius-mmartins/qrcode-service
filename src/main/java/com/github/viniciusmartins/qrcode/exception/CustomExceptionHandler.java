package com.github.viniciusmartins.qrcode.exception;

import com.github.viniciusmartins.qrcode.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler({SQLException.class, DataAccessException.class, NullPointerException.class})
    public ResponseEntity<List<Error>> handleUnexpectedException(Exception e) {
        log.error("ExceptionHandler: Unexpected exception", e);
        return buildErrorsResponse(List.of(ErrorDTO.builder()
                        .code(ErrorCodeEnum.INTERNAL_SERVER_ERROR.name())
                        .message("internal.server.error")
                        .build()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Error>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorDTO> errors = e.getFieldErrors().stream()
                .map(fieldError -> ErrorDTO.builder()
                        .code(ErrorCodeEnum.REQUIRED_FIELD.name())
                        .message(fieldError.getDefaultMessage())
                        .msgArg(fieldError.getField())
                        .build())
                .toList();
        return buildErrorsResponse(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<List<Error>> handleCustomException(CustomException e) {
        return buildErrorsResponse(List.of(e.getErrorDTO()), e.getHttpStatus());
    }

    private ResponseEntity<List<Error>> buildErrorsResponse(List<ErrorDTO> errors, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(
                errors.stream().map(error -> new Error(error.code(), buildErrorMessage(error))).toList()
        );
    }

    private String buildErrorMessage(ErrorDTO error) {
        return messageSource.getMessage(error.message(), new Object[]{error.msgArg()}, Locale.getDefault());
    }

    public record Error(
            String code,
            String message
    ) {}

}
