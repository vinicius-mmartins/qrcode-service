package com.github.viniciusmartins.qrcode.service;

import com.github.viniciusmartins.qrcode.dto.*;
import com.github.viniciusmartins.qrcode.entity.QRCode;
import com.github.viniciusmartins.qrcode.entity.enums.StatusEnum;
import com.github.viniciusmartins.qrcode.exception.ErrorCodeEnum;
import com.github.viniciusmartins.qrcode.exception.UnprocessableEntityException;
import com.github.viniciusmartins.qrcode.mapper.QRCodeMapper;
import com.github.viniciusmartins.qrcode.repository.QRCodeRepository;
import com.github.viniciusmartins.qrcode.validation.QRCodeRequestValidation;
import com.github.viniciusmartins.qrcode.validation.QRCodeRequestValidationImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
public class QRCodeServiceImpl implements QRCodeService {

    private final QRCodeRequestValidation requestValidation;
    private final QRCodeRepository qrCodeRepository;
    private final Integer expirationDays;

    public QRCodeServiceImpl(QRCodeRequestValidationImpl requestValidation,
                             QRCodeRepository qrCodeRepository,
                             @Value("${qrcode.immediate.expiration-days}")
                             Integer expirationDays) {
        this.requestValidation = requestValidation;
        this.qrCodeRepository = qrCodeRepository;
        this.expirationDays = expirationDays;
    }

    @Override
    public QRCodeResponse register(QRCodeRequest request) {
        validateQrCode(request);
        QRCode qrcode = QRCodeMapper.toEntity(request);
        checkIfPresent(qrcode);
        qrcode = setDefaultAndSave(qrcode);
        return QRCodeMapper.toResponse(qrcode);
    }

    @Override
    public QRCodeWithDueDateResponse registerWithDueDate(QRCodeWithDueDateRequest request) {
        validateQrCodeWithDueDate(request);
        QRCode qrcode = QRCodeMapper.toEntity(request);
        checkIfPresent(qrcode);
        qrcode = setDefaultAndSave(qrcode);
        return QRCodeMapper.toDueDateResponse(qrcode);
    }

    private void validateQrCode(IQRCodeRequest request) {
        requestValidation.validateValueFormat(request);
        requestValidation.validateValueGreaterThenZero(request);
        requestValidation.validadeStatus(request);
    }

    private void validateQrCodeWithDueDate(IQRCodeRequest request) {
        validateQrCode(request);
        requestValidation.validateDateFormat(request);
        requestValidation.validateFutureDate(request, "dueDate");
    }

    private void checkIfPresent(QRCode qrcode) {
        qrCodeRepository.findByTxid(qrcode.getTxid())
                .ifPresent(qr -> {
                    log.error("QRCode already exists with txid: {}", qr.getTxid());
                    throw new UnprocessableEntityException(
                            ErrorDTO.builder()
                                    .code(ErrorCodeEnum.ALREADY_REGISTERED.name())
                                    .message("entity.already.registered")
                                    .msgArg("QRCode")
                                    .build()
                    );
                });
    }

    private QRCode setDefaultAndSave(QRCode qrCode) {
        qrCode.setUpdatedAt(LocalDateTime.now());
        if (qrCode.getStatus() == null) {
            qrCode.setStatus(StatusEnum.OPEN);
        }
        setExpirationDate(qrCode);
        return qrCodeRepository.save(qrCode);
    }

    private void setExpirationDate(QRCode qrCode) {
        if (qrCode.getExpirationDate() == null) {
            if (qrCode.getDueDate() != null) {
                qrCode.setExpirationDate(qrCode.getDueDate().plusDays(expirationDays));
            } else {
                qrCode.setExpirationDate(LocalDate.now().plusDays(expirationDays));
            }
        }
    }

}
