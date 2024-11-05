package com.github.viniciusmartins.qrcode.validation;

import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateRequest;

public interface QRCodeRequestValidation {

    void validateValueFormat(QRCodeWithDueDateRequest request);

    void validateValueGreaterThenZero(QRCodeWithDueDateRequest request);

    void validateDateFormat(QRCodeWithDueDateRequest request);

    void validateFutureDate(QRCodeWithDueDateRequest request, String fieldName);

    void validadeStatus(QRCodeWithDueDateRequest request);
}
