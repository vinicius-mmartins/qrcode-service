package com.github.viniciusmartins.qrcode.validation;

import com.github.viniciusmartins.qrcode.dto.ErrorDTO;
import com.github.viniciusmartins.qrcode.exception.BadRequestException;
import com.github.viniciusmartins.qrcode.exception.ErrorCodeEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@UtilityClass
@Slf4j
public class DateValidation {

    public static void validateDateFormat(String date, String fieldName) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            log.error("Invalid request date format: {}", date);
            throw new BadRequestException(
                    ErrorDTO.builder()
                            .code(ErrorCodeEnum.INVALID_FIELD)
                            .message("invalid.field.date.format")
                            .msgArg(fieldName)
                            .build()
            );
        }
    }

    public static void validateFutureDate(String requestDate, String fieldName) {
        var date = LocalDate.parse(requestDate);
        if (date.isBefore(LocalDate.now())) {
            log.error("Invalid request date, not in the future: {}", requestDate);
            throw new BadRequestException(
                    ErrorDTO.builder()
                            .code(ErrorCodeEnum.INVALID_FIELD)
                            .message("invalid.field.date.range")
                            .msgArg(fieldName)
                            .build()
            );
        }
    }

}
