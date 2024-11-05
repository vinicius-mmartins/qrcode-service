package com.github.viniciusmartins.qrcode.service;

import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateRequest;
import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateResponse;
import com.github.viniciusmartins.qrcode.entity.QRCode;
import com.github.viniciusmartins.qrcode.entity.enums.StatusEnum;
import com.github.viniciusmartins.qrcode.mapper.QRCodeMapper;
import com.github.viniciusmartins.qrcode.repository.QRCodeRepository;
import com.github.viniciusmartins.qrcode.validation.QRCodeRequestValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@Slf4j
public class QRCodeService {

    private final QRCodeRequestValidation requestValidation;
    private final QRCodeRepository qrCodeRepository;
    private final Integer expirationDays;

    public QRCodeService(QRCodeRequestValidation requestValidation,
                         QRCodeRepository qrCodeRepository,
                         @Value("${qrcode.immediate.expiration-days}")
                         Integer expirationDays) {
        this.requestValidation = requestValidation;
        this.qrCodeRepository = qrCodeRepository;
        this.expirationDays = expirationDays;
    }

    public QRCodeWithDueDateResponse registerWithDueDate(QRCodeWithDueDateRequest request) {
        validateQrCode(request);
        QRCode qrcode = QRCodeMapper.toEntity(request);
        qrcode = save(qrcode);
        return QRCodeMapper.toDueDateResponse(qrcode);
    }

    private void validateQrCode(QRCodeWithDueDateRequest request) {
        requestValidation.validateValueFormat(request);
        requestValidation.validateValueGreaterThenZero(request);
        requestValidation.validateDateFormat(request);
        requestValidation.validateFutureDate(request, "dueDate");
    }

    private QRCode save(QRCode qrCode) {
        qrCode.setUpdatedAt(LocalDateTime.now());
        if (qrCode.getStatus() == null) {
            qrCode.setStatus(StatusEnum.OPEN);
        }
        qrCode.setExpirationDate(qrCode.getDueDate().plusDays(expirationDays));
        return qrCodeRepository.save(qrCode);
    }

    //todo: h2 properties

}
