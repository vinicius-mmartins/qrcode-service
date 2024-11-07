package com.github.viniciusmartins.qrcode.validation;

import com.github.viniciusmartins.qrcode.dto.ErrorDTO;
import com.github.viniciusmartins.qrcode.exception.BadRequestException;
import com.github.viniciusmartins.qrcode.exception.ErrorCodeEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@UtilityClass
@Slf4j
public class NumericValueValidation {

    public static void validateValueFormat(String value, String fieldName) {
        try {
            new BigDecimal(value);
        } catch (NumberFormatException e) {
            log.error("Invalid request value format: {}", value);
            throw new BadRequestException(
                    ErrorDTO.builder()
                            .code(ErrorCodeEnum.INVALID_FIELD)
                            .message("invalid.field.numeric.format")
                            .msgArg(fieldName)
                            .build()
            );
        }
    }

    public static void validateValueGreaterThenZero(String requestValue, String fieldName) {
        var value = new BigDecimal(requestValue);
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            log.error("Invalid request value, less then zero : {}", requestValue);
            throw new BadRequestException(
                    ErrorDTO.builder()
                            .code(ErrorCodeEnum.INVALID_FIELD)
                            .message("invalid.field.numeric.range")
                            .msgArg("value")
                            .build()
            );
        }
    }

}
