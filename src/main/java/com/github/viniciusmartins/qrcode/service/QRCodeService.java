package com.github.viniciusmartins.qrcode.service;

import com.github.viniciusmartins.qrcode.dto.QRCodeRequest;
import com.github.viniciusmartins.qrcode.dto.QRCodeResponse;
import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateRequest;
import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateResponse;

public interface QRCodeService {
    QRCodeResponse register(QRCodeRequest request);
    QRCodeWithDueDateResponse registerWithDueDate(QRCodeWithDueDateRequest request);
}
