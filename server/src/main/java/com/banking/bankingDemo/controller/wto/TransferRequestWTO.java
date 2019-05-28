package com.banking.bankingDemo.controller.wto;

public class TransferRequestWTO {

    private Long from;
    private Long to;
    private Double amount;

    public void setFrom(Long from) {
        this.from = from;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public Double getAmount() {
        return amount;
    }
}
