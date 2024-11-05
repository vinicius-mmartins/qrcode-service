package com.github.viniciusmartins.qrcode.dto;

import lombok.Builder;

@Builder
public record QRCodeResponse(
        String txid,
        String value,
        String description,
        String status
) {
}
