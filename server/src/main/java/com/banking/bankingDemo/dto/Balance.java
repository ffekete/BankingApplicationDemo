package com.banking.bankingDemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "balance")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "balance")
    @JsonIgnoreProperties("balance")
    private Customer customer;

    private Double balance;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Balance() {

    }

    public Balance(Double balance) {
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Double getBalance() {
        return balance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
