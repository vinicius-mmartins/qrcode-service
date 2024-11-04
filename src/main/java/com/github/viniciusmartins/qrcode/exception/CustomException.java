package com.github.viniciusmartins.qrcode.exception;

import com.github.viniciusmartins.qrcode.dto.ErrorDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    protected final HttpStatus httpStatus;
    protected final ErrorDTO errorDTO;

    public CustomException(HttpStatus httpStatus, ErrorDTO errorDTO) {
        this.httpStatus = httpStatus;
        this.errorDTO = errorDTO;
    }
}
