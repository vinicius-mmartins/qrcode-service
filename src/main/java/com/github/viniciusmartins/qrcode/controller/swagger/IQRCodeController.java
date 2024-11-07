package com.github.viniciusmartins.qrcode.controller.swagger;

import com.github.viniciusmartins.qrcode.dto.QRCodeRequest;
import com.github.viniciusmartins.qrcode.dto.QRCodeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Immediate QRCode without due date")
public interface IQRCodeController {

    @Operation(summary = "Register QRCode without due date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", content = @Content),
            @ApiResponse(responseCode = "422", content = @Content)
    })
    ResponseEntity<QRCodeResponse> registerQrcode(QRCodeRequest request);

}
