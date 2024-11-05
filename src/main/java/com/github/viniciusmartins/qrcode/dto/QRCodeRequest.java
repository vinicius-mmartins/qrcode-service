package com.github.viniciusmartins.qrcode.dto;

import jakarta.validation.constraints.NotBlank;

public record QRCodeRequest(
        @NotBlank(message = "required.field")
        String txid,
        @NotBlank(message = "required.field")
        String value,
        String description,
        String status
) implements IQRCodeRequest {
}
