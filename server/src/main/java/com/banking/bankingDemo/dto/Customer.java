package com.banking.bankingDemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "balance_id")
    @RestResource(path = "customerBalance", rel = "customer")
    @JsonIgnoreProperties("customer")
    private Balance balance;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("customerHistories")
    private Set<CustomerHistory> customerHistories;

    public Customer() {
    }

    public Customer(String firstName, String lastName, Balance balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    public Set<CustomerHistory> getCustomerHistories() {
        return customerHistories;
    }

    public void setCustomerHistories(Set<CustomerHistory> customerHistories) {
        this.customerHistories = customerHistories;
    }
}
