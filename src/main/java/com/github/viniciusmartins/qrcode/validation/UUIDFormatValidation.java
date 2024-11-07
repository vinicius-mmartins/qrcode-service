package com.github.viniciusmartins.qrcode.validation;

import com.github.viniciusmartins.qrcode.dto.ErrorDTO;
import com.github.viniciusmartins.qrcode.exception.BadRequestException;
import com.github.viniciusmartins.qrcode.exception.ErrorCodeEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@UtilityClass
@Slf4j
public class UUIDFormatValidation {

    public static void validateUUIDFormat(String uuid, String fieldName) {
        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            log.error("Campo {} não é um UUID válido", fieldName);
            throw new BadRequestException(
                    ErrorDTO.builder()
                            .code(ErrorCodeEnum.INVALID_FIELD)
                            .message("invalid.uuid.format")
                            .msgArg(fieldName)
                            .build()
            );
        }
    }
}
