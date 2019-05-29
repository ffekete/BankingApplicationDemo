package com.banking.bankingDemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
public class CustomerHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long fromId;
    private Long toId;
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("customerHistories")
    private Customer customer;

    public CustomerHistory() {
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getFromId() {
        return fromId;
    }

    public Long getToId() {
        return toId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Long getId() {
        return id;
    }
}
