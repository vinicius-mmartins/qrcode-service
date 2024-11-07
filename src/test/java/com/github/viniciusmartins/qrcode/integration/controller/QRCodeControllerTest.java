package com.github.viniciusmartins.qrcode.integration.controller;

import com.github.viniciusmartins.qrcode.entity.QRCode;
import com.github.viniciusmartins.qrcode.integration.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class QRCodeControllerTest extends IntegrationTest {

    private static final String ENDPOINT = "/api/v1/qrcodes";

    @Test
    @DisplayName("Should return 400 when trying to register a QRCode without required fields")
    public void testRegister_withoutRequiredFields() throws Exception {
        MvcResult result = mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {}
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].code").value("REQUIRED_FIELD"))
                .andExpect(jsonPath("[1].code").value("REQUIRED_FIELD"))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("Field value is required"));
        assertTrue(result.getResponse().getContentAsString().contains("Field txid is required"));
    }

    @Test
    @DisplayName("Should return 400 when trying to register a QRCode with invalid value format")
    public void testRegister_withInvalidValueFormat() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "txid": "c435a648-31ce-4120-ae62-b4fec44f6e68",
                                    "value": "1.0aa"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].code").value("INVALID_FIELD"))
                .andExpect(jsonPath("[0].message")
                        .value("Numeric field value has a invalid format. Try value like 123.45"));
    }

    @Test
    @DisplayName("Should return 400 when trying to register a QRCode with value less then zero")
    public void testRegister_withValueLessThenZero() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "txid": "c435a648-31ce-4120-ae62-b4fec44f6e68",
                                    "value": "-1.0"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].code").value("INVALID_FIELD"))
                .andExpect(jsonPath("[0].message")
                        .value("Numeric field value should be greater then Zero"));
    }

    @Test
    @DisplayName("Should return 400 when sending a invalid status")
    public void testRegister_withInvalidStatus() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "txid": "c435a648-31ce-4120-ae62-b4fec44f6e68",
                                  "value": "1.0",
                                  "status": "INVALID_STATUS"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].code").value("INVALID_FIELD"))
                .andExpect(jsonPath("[0].message")
                        .value("Status doesnt match the expected values"));
    }

    @Test
    @DisplayName("Should return 201 and then 422 when trying to register a QRCode with the same txid")
    public void testRegister_withSameTxid() throws Exception {
        // cadastra qrcode
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "txid": "c435a648-31ce-4120-ae62-b4fec44f6e68",
                                  "value": "1.0"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isCreated());
        // repete a requisição e retorna erro (txid garante idempotencia no insert)
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "txid": "c435a648-31ce-4120-ae62-b4fec44f6e68",
                                  "value": "1.0"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("[0].code").value("ALREADY_REGISTERED"))
                .andExpect(jsonPath("[0].message")
                        .value("QRCode already registered"));
    }

    @Test
    @DisplayName("Should return 201 and return default status OPEN")
    public void testRegister_withDefaultStatus() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "txid": "3dbb0873-a603-4a37-845d-0495edae5554",
                                  "value": "6.0",
                                  "description":"Café e Pão de Queijo"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("txid").value("3dbb0873-a603-4a37-845d-0495edae5554"))
                .andExpect(jsonPath("value").value("6.0"))
                .andExpect(jsonPath("description").value("Café e Pão de Queijo"))
                .andExpect(jsonPath("status").value("OPEN"));
    }

    @Test
    @DisplayName("Should return 201 and save qrcode with dates: expiration, created and updated")
    public void testRegister_withDefaultValues() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "txid": "095dbcf0-f14f-4fc2-90d0-90f44e0998d2",
                                  "value": "6.0"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("txid").value("095dbcf0-f14f-4fc2-90d0-90f44e0998d2"))
                .andExpect(jsonPath("value").value("6.0"))
                .andExpect(jsonPath("status").value("OPEN"))
                .andExpect(jsonPath("description").isEmpty());

        Optional<QRCode> qrcodeOpt = qrCodeRepository.findByTxid("095dbcf0-f14f-4fc2-90d0-90f44e0998d2");
        assertTrue(qrcodeOpt.isPresent());
        assertEquals(LocalDate.now().plusDays(2), qrcodeOpt.get().getExpirationDate());
        assertEquals(LocalDate.now(), qrcodeOpt.get().getUpdatedAt().toLocalDate());
        assertEquals(LocalDate.now(), qrcodeOpt.get().getCreatedAt().toLocalDate());
    }

    @Test
    @DisplayName("Should return 400 when trying to register a QRCode with invalid initial status")
    public void testRegister_withInvalidInitialStatus() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "txid": "9e671d81-93db-470a-b4ec-a577f58bc5d2",
                                  "value": "1.0",
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
    @DisplayName("Should return 400 when trying to register a QRCode with invalid txid format")
    public void testRegister_withInvalidTxidFormat() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "txid": "9e671d81-93db-470a-b4ec-a577f58bc5d2a-JISAJISJA",
                                  "value": "1.0"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("[0].code").value("INVALID_FIELD"))
                .andExpect(jsonPath("[0].message")
                        .value("Field txid expected to be a valid UUID"));
    }

}
