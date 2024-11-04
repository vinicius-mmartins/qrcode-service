package com.github.viniciusmartins.qrcode.dto;

import jakarta.validation.constraints.NotBlank;

public record QRCodeWithDueDateRequest(
        @NotBlank(message = "required.field")
        String txid,
        @NotBlank(message = "required.field")
        String value,
        @NotBlank(message = "required.field")
        String dueDate,
        String description,
        String status
) {
}
