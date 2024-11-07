package com.github.viniciusmartins.qrcode.unity.validation;


import com.github.viniciusmartins.qrcode.exception.BadRequestException;
import com.github.viniciusmartins.qrcode.exception.ErrorCodeEnum;
import com.github.viniciusmartins.qrcode.validation.UUIDFormatValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UUIDFormatValidationTest {

    @Test
    public void testValidateUUIDFormat() {
        try {
            UUIDFormatValidation.validateUUIDFormat("123e4567-e89b-12d3-a456-426614174aaa", "txid");
        } catch (BadRequestException e) {
            assertEquals(ErrorCodeEnum.INVALID_FIELD, e.getErrorDTO().code());
            assertEquals("invalid.uuid.format", e.getErrorDTO().message());
        }
    }

}
