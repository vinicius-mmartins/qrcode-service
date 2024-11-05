package com.github.viniciusmartins.qrcode.dto;

public interface IQRCodeRequest {

    String value();

    String status();

    default String dueDate() {
        return null;
    }

}
