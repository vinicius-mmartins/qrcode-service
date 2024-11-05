package com.github.viniciusmartins.qrcode.validation;

import com.github.viniciusmartins.qrcode.dto.ErrorDTO;
import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateRequest;
import com.github.viniciusmartins.qrcode.entity.enums.StatusEnum;
import com.github.viniciusmartins.qrcode.exception.BadRequestException;
import com.github.viniciusmartins.qrcode.exception.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
@Slf4j
public class QRCodeRequestValidationImpl implements QRCodeRequestValidation {

    @Override
    public void validateValueFormat(QRCodeWithDueDateRequest request) {
        try {
            new BigDecimal(request.value());
        } catch (NumberFormatException e) {
            log.error("Invalid request value format: {}", request.value());
            throw new BadRequestException(
                    ErrorDTO.builder()
                            .code(ErrorCodeEnum.INVALID_FIELD.name())
                            .message("invalid.field.numeric.format")
                            .msgArg("value")
                            .build()
            );
        }
    }

    @Override
    public void validateValueGreaterThenZero(QRCodeWithDueDateRequest request) {
        var value = new BigDecimal(request.value());
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            log.error("Invalid request value, less then zero : {}", request.value());
            throw new BadRequestException(
                    ErrorDTO.builder()
                            .code(ErrorCodeEnum.INVALID_FIELD.name())
                            .message("invalid.field.numeric.range")
                            .msgArg("value")
                            .build()
            );
        }
    }

    @Override
    public void validateDateFormat(QRCodeWithDueDateRequest request) {
        try {
            LocalDate.parse(request.dueDate());
        } catch (DateTimeParseException e) {
            log.error("Invalid request date format: {}", request.value());
            throw new BadRequestException(
                    ErrorDTO.builder()
                            .code(ErrorCodeEnum.INVALID_FIELD.name())
                            .message("invalid.field.date.format")
                            .msgArg("value")
                            .build()
            );
        }
    }

    @Override
    public void validateFutureDate(QRCodeWithDueDateRequest request, String fieldName) {
        var date = LocalDate.parse(request.dueDate());
        if (date.isBefore(LocalDate.now())) {
            log.error("Invalid request date, not in the future: {}", request.dueDate());
            throw new BadRequestException(
                    ErrorDTO.builder()
                            .code(ErrorCodeEnum.INVALID_FIELD.name())
                            .message("invalid.field.date.range")
                            .msgArg(fieldName)
                            .build()
            );
        }
    }

    @Override
    public void validadeStatus(QRCodeWithDueDateRequest request) {
        if (request.status() != null) {
            try {
                StatusEnum.valueOf(request.status());
            } catch (IllegalArgumentException e) {
                log.error("Invalid request status: {}", request.status());
                throw new BadRequestException(
                        ErrorDTO.builder()
                                .code(ErrorCodeEnum.INVALID_FIELD.name())
                                .message("invalid.field.status")
                                .build()
                );
            }
        }
    }

}
