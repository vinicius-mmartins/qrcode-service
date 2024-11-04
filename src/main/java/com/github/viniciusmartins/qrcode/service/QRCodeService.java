package com.github.viniciusmartins.qrcode.service;

import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateRequest;
import com.github.viniciusmartins.qrcode.entity.QRCode;
import com.github.viniciusmartins.qrcode.repository.QRCodeRepository;
import com.github.viniciusmartins.qrcode.validation.QRCodeRequestValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class QRCodeService {

    private final QRCodeRequestValidation requestValidation;
    private final QRCodeRepository qrCodeRepository;

    public void registerWithDueDate(QRCodeWithDueDateRequest request) {

        // validar
        validateQrCode(request);

        // salvar

    }

    private void validateQrCode(QRCodeWithDueDateRequest request) {
        requestValidation.validateValueFormat(request);
        requestValidation.validateValueGreaterThenZero(request);
        requestValidation.validateDateFormat(request);
        requestValidation.validateFutureDate(request, "dueDate");
    }

    private QRCode save() {

        // set updated_at
        // set status if null

        return null;
    }

}
