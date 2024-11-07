package com.github.viniciusmartins.qrcode.dto;

import lombok.Builder;

@Builder
public record QRCodeWithDueDateResponse(
        String txid,
        String value,
        String dueDate,
        String description,
        String status
) {
}
