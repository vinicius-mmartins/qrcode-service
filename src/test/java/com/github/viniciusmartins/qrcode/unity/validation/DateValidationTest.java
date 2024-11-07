package com.github.viniciusmartins.qrcode.unity.validation;

import com.github.viniciusmartins.qrcode.exception.BadRequestException;
import com.github.viniciusmartins.qrcode.exception.ErrorCodeEnum;
import com.github.viniciusmartins.qrcode.validation.DateValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class DateValidationTest {

    @Test
    public void validateDateFormatTest() {
        try {
            DateValidation.validateDateFormat("01-01-2030", "dueDate");
            fail("Testing exception: test shouldn't get to this line");
        } catch (BadRequestException e) {
            assertEquals(ErrorCodeEnum.INVALID_FIELD, e.getErrorDTO().code());
            assertEquals("invalid.field.date.format", e.getErrorDTO().message());
            // a mensagem vai ser convertida no handler, é só pra conferir se a exceção foi lançada corretamente
            // e diferenciar os dois casos de teste
        }
    }

    @Test
    public void validateFutureDateTest() {
        try {
            DateValidation.validateFutureDate("2020-01-01", "dueDate");
            fail("Testing exception: test shouldn't get to this line");
        } catch (BadRequestException e) {
            assertEquals(ErrorCodeEnum.INVALID_FIELD, e.getErrorDTO().code());
            assertEquals("invalid.field.date.range", e.getErrorDTO().message());
        }
    }

}
