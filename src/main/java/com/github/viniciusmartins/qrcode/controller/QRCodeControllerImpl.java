package com.github.viniciusmartins.qrcode.controller;

import com.github.viniciusmartins.qrcode.controller.interfaces.QRCodeController;
import com.github.viniciusmartins.qrcode.dto.QRCodeRequest;
import com.github.viniciusmartins.qrcode.dto.QRCodeResponse;
import com.github.viniciusmartins.qrcode.service.interfaces.QRCodeService;
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
@RequestMapping("/api/v1/qrcodes")
@RequiredArgsConstructor
@Slf4j
public class QRCodeControllerImpl implements QRCodeController {

    private static final String LOG_TAG = "QRCODE/TXID";

    private final QRCodeService qrCodeService;

    @Override
    @PostMapping
    public ResponseEntity<QRCodeResponse> registerQrcode(@RequestBody @Valid QRCodeRequest request) {
        @Cleanup var mdc = MDC.putCloseable(LOG_TAG, request.txid());
        log.info("Beginning register QRCode");
        var response = qrCodeService.register(request);
        log.info("Ending register QRCode");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
