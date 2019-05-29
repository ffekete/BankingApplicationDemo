package com.banking.bankingDemo.controller.wto;

import com.banking.bankingDemo.dto.Balance;
import com.banking.bankingDemo.dto.CustomerHistory;

import java.util.Set;

public class BankStatementWTO {

    private String firstName;
    private String LastName;
    private Balance balance;
    private Set<CustomerHistory> customerHistory;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public Set<CustomerHistory> getCustomerHistory() {
        return customerHistory;
    }

    public void setCustomerHistory(Set<CustomerHistory> customerHistory) {
        this.customerHistory = customerHistory;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public Balance getBalance() {
        return balance;
    }

}
