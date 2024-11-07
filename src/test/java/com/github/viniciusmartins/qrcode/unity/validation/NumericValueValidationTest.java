package com.github.viniciusmartins.qrcode.unity.validation;

import com.github.viniciusmartins.qrcode.exception.BadRequestException;
import com.github.viniciusmartins.qrcode.exception.ErrorCodeEnum;
import com.github.viniciusmartins.qrcode.validation.NumericValueValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class NumericValueValidationTest {

    @Test
    public void validateValueFormatTest() {
        try {
            NumericValueValidation.validateValueFormat("30,00", "value");
            fail("Testing exception: test shouldn't get to this line");
        } catch (BadRequestException e) {
            assertEquals(ErrorCodeEnum.INVALID_FIELD, e.getErrorDTO().code());
            assertEquals("invalid.field.numeric.format", e.getErrorDTO().message());
        }
    }

    @Test
    public void validateValueGreaterThenZeroTest() {
        try {
            NumericValueValidation.validateValueGreaterThenZero("-1", "value");
            fail("Testing exception: test shouldn't get to this line");
        } catch (BadRequestException e) {
            assertEquals(ErrorCodeEnum.INVALID_FIELD, e.getErrorDTO().code());
            assertEquals("invalid.field.numeric.range", e.getErrorDTO().message());
        }
    }

}
