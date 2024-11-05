package com.github.viniciusmartins.qrcode.mapper;

import com.github.viniciusmartins.qrcode.dto.QRCodeRequest;
import com.github.viniciusmartins.qrcode.dto.QRCodeResponse;
import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateRequest;
import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateResponse;
import com.github.viniciusmartins.qrcode.entity.QRCode;
import com.github.viniciusmartins.qrcode.entity.enums.StatusEnum;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;

@UtilityClass
public class QRCodeMapper {

    public static QRCode toEntity(QRCodeRequest request) {
        StatusEnum status = null;
        if (request.status() != null) {
            status = StatusEnum.valueOf(request.status());
        }
        return QRCode.builder()
                .txid(request.txid())
                .value(new BigDecimal(request.value()))
                .description(request.description())
                .status(status)
                .build();
    }

    public static QRCode toEntity(QRCodeWithDueDateRequest request) {
        StatusEnum status = null;
        if (request.status() != null) {
            status = StatusEnum.valueOf(request.status());
        }
        return QRCode.builder()
                .txid(request.txid())
                .value(new BigDecimal(request.value()))
                .dueDate(LocalDate.parse(request.dueDate()))
                .description(request.description())
                .status(status)
                .build();
    }

    public QRCodeResponse toResponse(QRCode qrCode) {
        return QRCodeResponse.builder()
                .txid(qrCode.getTxid())
                .value(qrCode.getValue().toString())
                .description(qrCode.getDescription())
                .status(qrCode.getStatus().name())
                .build();
    }

    public QRCodeWithDueDateResponse toDueDateResponse(QRCode qrCode) {
        return QRCodeWithDueDateResponse.builder()
                .txid(qrCode.getTxid())
                .value(qrCode.getValue().toString())
                .dueDate(qrCode.getDueDate().toString())
                .description(qrCode.getDescription())
                .status(qrCode.getStatus().name())
                .build();
    }

}
