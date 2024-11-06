package com.github.viniciusmartins.qrcode.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record QRCodeWithDueDateRequest(
        @Schema(example = "613c1b05-accc-4792-aa80-48dc51c294a1",
                description = "Unique identifier of the QRCode"
        )
        @NotBlank(message = "required.field")
        String txid,
        @Schema(example = "5.00")
        @NotBlank(message = "required.field")
        String value,
        @Schema(example = "2024-12-31")
        @NotBlank(message = "required.field")
        String dueDate,
        @Schema(example = "Pagamento Pão de Queijo")
        String description,
        @Schema(example = "OPEN")
        String status
) implements IQRCodeRequest {
}
