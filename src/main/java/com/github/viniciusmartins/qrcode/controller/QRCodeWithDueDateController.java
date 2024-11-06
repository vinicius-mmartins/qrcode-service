package com.github.viniciusmartins.qrcode.controller;

import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateRequest;
import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateResponse;
import com.github.viniciusmartins.qrcode.service.QRCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Immediate QRCode with due date")
public class QRCodeWithDueDateController {

    private static final String LOG_TAG = "QRCODE_WITH_DUE_DATE/TXID";

    private final QRCodeService qrCodeService;

    @Operation(summary = "Register QRCode with due date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content),
            @ApiResponse(responseCode = "422", content = @Content)
    })
    @PostMapping
    public ResponseEntity<QRCodeWithDueDateResponse> registerQrcode(@RequestBody @Valid QRCodeWithDueDateRequest request) {
        @Cleanup var mdc = MDC.putCloseable(LOG_TAG, request.txid());
        log.info("Begin register QRCode with due date");
        var response = qrCodeService.registerWithDueDate(request);
        log.info("Ending register QRCode with due date");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
