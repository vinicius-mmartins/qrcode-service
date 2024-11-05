package com.github.viniciusmartins.qrcode.dto;

import com.github.viniciusmartins.qrcode.exception.ErrorCodeEnum;
import lombok.Builder;

@Builder
public record ErrorDTO(
        ErrorCodeEnum code,
        String message,
        String msgArg
) {
}
