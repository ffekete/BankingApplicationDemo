package com.banking.bankingDemo.service;

import com.banking.bankingDemo.controller.wto.BankStatementWTO;
import com.banking.bankingDemo.dto.Balance;
import com.banking.bankingDemo.dto.Customer;
import com.banking.bankingDemo.dto.CustomerHistory;
import com.banking.bankingDemo.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;

@Service
public class BankStatementService {

    @Resource
    private CustomerRepository customerRepository;

    public BankStatementWTO getBankStatement(long id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) {
            Balance balance = customer.get().getBalance();
            Set<CustomerHistory> customerHistories = customer.get().getCustomerHistories();

            BankStatementWTO bankStatementWTO = new BankStatementWTO();
            bankStatementWTO.setBalance(balance);
            bankStatementWTO.setCustomerHistory(customerHistories);
            bankStatementWTO.setFirstName(customer.get().getFirstName());
            bankStatementWTO.setLastName(customer.get().getLastName());

            return bankStatementWTO;

        } else {
            return null;
        }
    }

}
