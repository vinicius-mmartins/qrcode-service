package com.github.viniciusmartins.qrcode.unity.validation;

import com.github.viniciusmartins.qrcode.exception.BadRequestException;
import com.github.viniciusmartins.qrcode.exception.ErrorCodeEnum;
import com.github.viniciusmartins.qrcode.validation.QRCodeStatusValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class QRCodeStatusValidationTest {

    @Test
    public void validateStatusTest() {
        try {
            QRCodeStatusValidation.validateStatus("invalidStatus");
            fail("Testing exception: test shouldn't get to this line");
        } catch (BadRequestException e) {
            assertEquals(ErrorCodeEnum.INVALID_FIELD, e.getErrorDTO().code());
            assertEquals("invalid.field.status", e.getErrorDTO().message());
        }
    }

    @Test
    public void validateInitialStatusTest() {
        try {
            QRCodeStatusValidation.validateInitialStatus("CANCELED");
            fail("Testing exception: test shouldn't get to this line");
        } catch (BadRequestException e) {
            assertEquals(ErrorCodeEnum.INVALID_FIELD, e.getErrorDTO().code());
            assertEquals("invalid.field.initial.status", e.getErrorDTO().message());
        }
    }

}
