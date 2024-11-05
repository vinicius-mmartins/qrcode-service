package com.github.viniciusmartins.qrcode.exception;

import com.github.viniciusmartins.qrcode.dto.ErrorDTO;
import org.springframework.http.HttpStatus;

public class UnprocessableEntityException extends CustomException {
    public UnprocessableEntityException(ErrorDTO errorDTO) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, errorDTO);
    }
}
