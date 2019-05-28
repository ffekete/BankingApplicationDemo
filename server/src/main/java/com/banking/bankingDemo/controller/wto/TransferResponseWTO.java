package com.banking.bankingDemo.controller.wto;

public class TransferResponseWTO {

    private Integer httpStatus;
    private String text;

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public String getText() {
        return text;
    }
}
