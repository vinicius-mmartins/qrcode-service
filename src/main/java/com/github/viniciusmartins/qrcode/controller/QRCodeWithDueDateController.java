package com.github.viniciusmartins.qrcode.controller;

import com.github.viniciusmartins.qrcode.controller.swagger.IQRCodeWithDueDateController;
import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateRequest;
import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateResponse;
import com.github.viniciusmartins.qrcode.service.QRCodeService;
import jakarta.validation.Valid;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/qrcodes/with-due-date")
@RequiredArgsConstructor
@Slf4j
public class QRCodeWithDueDateController implements IQRCodeWithDueDateController {

    private static final String LOG_TAG = "QRCODE_WITH_DUE_DATE/TXID";

    private final QRCodeService qrCodeService;

    @Override
    @PostMapping
    public ResponseEntity<QRCodeWithDueDateResponse> registerQrcode(@RequestBody @Valid QRCodeWithDueDateRequest request) {
        @Cleanup var mdc = MDC.putCloseable(LOG_TAG, request.txid());
        log.info("Begin register QRCode with due date");
        var response = qrCodeService.registerWithDueDate(request);
        log.info("Ending register QRCode with due date");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
