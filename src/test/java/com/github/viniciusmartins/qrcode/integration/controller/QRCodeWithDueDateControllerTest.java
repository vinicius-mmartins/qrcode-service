package com.github.viniciusmartins.qrcode.integration.controller;

import com.github.viniciusmartins.qrcode.entity.QRCode;
import com.github.viniciusmartins.qrcode.integration.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QRCodeWithDueDateControllerTest extends IntegrationTest {

    public static final String ENDPOINT = "/api/v1/qrcodes/with-due-date";

    @Test
    @DisplayName("Should return 400 when trying to register a QRCode with due without required fields")
    public void testRegister_withDue_withoutRequiredFields() throws Exception {
        MvcResult result = mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {}
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].code").value("REQUIRED_FIELD"))
                .andExpect(jsonPath("[1].code").value("REQUIRED_FIELD"))
                .andExpect(jsonPath("[2].code").value("REQUIRED_FIELD"))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Field txid is required"));
        assertTrue(result.getResponse().getContentAsString().contains("Field value is required"));
        assertTrue(result.getResponse().getContentAsString().contains("Field dueDate is required"));
    }

    @Test
    @DisplayName("Should return 400 when trying to register a QRCode with due with invalid value format")
    public void testRegister_withDue_invalidValueFormat() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "txid": "2672f581-ac24-4787-9c65-1535b34f1bec",
                                    "value": "99,90",
                                    "dueDate": "2029-12-31"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].code").value("INVALID_FIELD"))
                .andExpect(jsonPath("[0].message")
                        .value("Numeric field value has a invalid format. Try value like 123.45"));
    }


    @Test
    @DisplayName("Should return 400 when trying to register a QRCode with due with value less then zero")
    public void testRegister_withDue_valueLessThenZero() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "txid": "2672f581-ac24-4787-9c65-1535b34f1bec",
                                    "value": "-1.0",
                                    "dueDate": "2029-12-31"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].code").value("INVALID_FIELD"))
                .andExpect(jsonPath("[0].message")
                        .value("Numeric field value should be greater then Zero"));
    }

    @Test
    @DisplayName("Should return 400 when sending a invalid status for QRCode with due")
    public void testRegister_withDue_invalidStatus() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "txid": "2672f581-ac24-4787-9c65-1535b34f1bec",
                                    "value": "99.90",
                                    "dueDate": "2029-12-31",
                                    "status": "INVALID_STATUS"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].code").value("INVALID_FIELD"))
                .andExpect(jsonPath("[0].message").value("Status doesnt match the expected values"));
    }

    @Test
    @DisplayName("Should return 201 and then 422 when trying to register a QRCode with due with the same txid")
    public void testRegister_withDue_sameTxid() throws Exception {
        // cadastra qrcode
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "txid": "3047437f-cb3e-4f31-b110-ab8839dd7612",
                                    "value": "99.90",
                                    "dueDate": "2029-12-31"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isCreated());
        // repete a requisição e retorna erro (txid garante idempotencia no insert)
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "txid": "3047437f-cb3e-4f31-b110-ab8839dd7612",
                                    "value": "99.90",
                                    "dueDate": "2029-12-31"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("[0].code").value("ALREADY_REGISTERED"))
                .andExpect(jsonPath("[0].message")
                        .value("QRCode already registered"));
    }

    @Test
    @DisplayName("Should return 201 and return default status OPEN for QRCode with due")
    public void testRegister_withDue_defaultStatus() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "txid": "3d8cc46a-f549-4328-9d40-08bbdd3a2ec5",
                                    "value": "99.90",
                                    "dueDate": "2029-12-31"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("txid").value("3d8cc46a-f549-4328-9d40-08bbdd3a2ec5"))
                .andExpect(jsonPath("status").value("OPEN"));
    }

    @Test
    @DisplayName("Should return 201 and save qrcode with due with dates: expiration (calculated), created and updated")
    public void testRegister_withDue_dates() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "txid": "ecdc13f7-d6be-4dd9-822d-013aab40b699",
                                    "value": "99.90",
                                    "dueDate": "2100-01-01"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("txid").value("ecdc13f7-d6be-4dd9-822d-013aab40b699"))
                .andExpect(jsonPath("status").value("OPEN"))
                .andExpect(jsonPath("value").value("99.90"))
                .andExpect(jsonPath("description").isEmpty());

        Optional<QRCode> qrcodeOpt = qrCodeRepository.findByTxid("ecdc13f7-d6be-4dd9-822d-013aab40b699");
        assertTrue(qrcodeOpt.isPresent());
        // colocando data exp dois dias apos vencimento
        assertEquals("2100-01-03", qrcodeOpt.get().getExpirationDate().toString());
        assertEquals(LocalDate.now(), qrcodeOpt.get().getUpdatedAt().toLocalDate());
        assertEquals(LocalDate.now(), qrcodeOpt.get().getCreatedAt().toLocalDate());
    }

    @Test
    @DisplayName("Should return 400 when trying to register a QRCode with due with invalid initial status")
    public void testRegister_withDue_invalidInitialStatus() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "txid": "2672f581-ac24-4787-9c65-1535b34f1bec",
                                    "value": "99.90",
                                    "dueDate": "2029-12-31",
                                    "status": "CANCELED"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].code").value("INVALID_FIELD"))
                .andExpect(jsonPath("[0].message")
                        .value("Status CANCELED is invalid for initial status"));
    }

    @Test
    @DisplayName("Should return 400 when trying to register a QRCode with due with invalid txid format")
    public void testRegister_withDue_invalidTxidFormat() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "txid": "2672f581-ac24-4787-9c65-1535b34f1beca-JISAJISJA",
                                    "value": "99.90",
                                    "dueDate": "2029-12-31"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].code").value("INVALID_FIELD"))
                .andExpect(jsonPath("[0].message")
                        .value("Field txid expected to be a valid UUID"));
    }

    @Test
    @DisplayName("Should return 400 when trying to register a QRCode with due with invalid due date format")
    public void testRegister_withDue_invalidDueDateFormat() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "txid": "2672f581-ac24-4787-9c65-1535b34f1bec",
                                    "value": "99.90",
                                    "dueDate": "2029-12-31T00:00:00"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].code").value("INVALID_FIELD"))
                .andExpect(jsonPath("[0].message")
                        .value("Date field dueDate has a invalid format. Try pattern yyyy-MM-dd"));
    }

    @Test
    @DisplayName("Should return 400 when trying to register a QRCode with due with due date in the past")
    public void testRegister_withDue_dueDateInThePast() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "txid": "2672f581-ac24-4787-9c65-1535b34f1bec",
                                    "value": "99.90",
                                    "dueDate": "2019-12-31"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].code").value("INVALID_FIELD"))
                .andExpect(jsonPath("[0].message")
                        .value("Date field dueDate should be a future date"));
    }

}





















