package com.github.viniciusmartins.qrcode.dto;

import lombok.Builder;

@Builder
public record ErrorDTO(
        String code,
        String message,
        String arg
) {
}
