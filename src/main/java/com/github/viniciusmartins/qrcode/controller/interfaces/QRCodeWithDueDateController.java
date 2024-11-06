package com.github.viniciusmartins.qrcode.controller.interfaces;

import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateRequest;
import com.github.viniciusmartins.qrcode.dto.QRCodeWithDueDateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;


@Tag(name = "Immediate QRCode with due date")
public interface QRCodeWithDueDateController {

    @Operation(summary = "Register QRCode with due date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content),
            @ApiResponse(responseCode = "422", content = @Content)
    })
    ResponseEntity<QRCodeWithDueDateResponse> registerQrcode(QRCodeWithDueDateRequest request);

}
