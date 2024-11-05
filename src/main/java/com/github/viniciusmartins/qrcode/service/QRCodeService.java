package com.github.viniciusmartins.qrcode.service;

import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateRequest;
import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateResponse;

public interface QRCodeService {
    QRCodeWithDueDateResponse registerWithDueDate(QRCodeWithDueDateRequest request);
}
