package com.github.viniciusmartins.qrcode.exception;

import com.github.viniciusmartins.qrcode.dto.ErrorDTO;
import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {
    public BadRequestException(ErrorDTO errorDTO) {
        super(HttpStatus.BAD_REQUEST, errorDTO);
    }
}
