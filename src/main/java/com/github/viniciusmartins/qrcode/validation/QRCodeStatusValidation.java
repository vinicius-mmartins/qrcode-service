package com.github.viniciusmartins.qrcode.validation;

import com.github.viniciusmartins.qrcode.dto.ErrorDTO;
import com.github.viniciusmartins.qrcode.entity.enums.StatusEnum;
import com.github.viniciusmartins.qrcode.exception.BadRequestException;
import com.github.viniciusmartins.qrcode.exception.ErrorCodeEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class QRCodeStatusValidation {

    public static void validateStatus(String status) {
        if (status != null) {
            try {
                StatusEnum.valueOf(status);
            } catch (IllegalArgumentException e) {
                log.error("Invalid request status: {}", status);
                throw new BadRequestException(
                        ErrorDTO.builder()
                                .code(ErrorCodeEnum.INVALID_FIELD)
                                .message("invalid.field.status")
                                .build()
                );
            }
        }
    }

    public static void validateInitialStatus(String status) {
        if (status != null && !StatusEnum.OPEN.name().equals(status)) {
            log.error("Invalid request initial status: {}", status);
            throw new BadRequestException(
                    ErrorDTO.builder()
                            .code(ErrorCodeEnum.INVALID_FIELD)
                            .message("invalid.field.initial.status")
                            .msgArg(status)
                            .build()
            );
        }
    }
}
