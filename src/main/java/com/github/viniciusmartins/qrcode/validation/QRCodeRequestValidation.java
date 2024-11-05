package com.github.viniciusmartins.qrcode.validation;

import com.github.viniciusmartins.qrcode.dto.IQRCodeRequest;

public interface QRCodeRequestValidation {

    void validateValueFormat(IQRCodeRequest request);

    void validateValueGreaterThenZero(IQRCodeRequest request);

    void validateDateFormat(IQRCodeRequest request);

    void validateFutureDate(IQRCodeRequest request, String fieldName);

    void validadeStatus(IQRCodeRequest request);
}
