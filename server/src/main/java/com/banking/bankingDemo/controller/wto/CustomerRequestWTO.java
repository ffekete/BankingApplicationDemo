package com.banking.bankingDemo.controller.wto;

import com.banking.bankingDemo.dto.Customer;

public class CustomerRequestWTO {

    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
