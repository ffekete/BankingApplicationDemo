package com.banking.bankingDemo.service;

import com.banking.bankingDemo.dto.Balance;
import com.banking.bankingDemo.dto.Customer;
import com.banking.bankingDemo.repository.BalanceRepository;
import com.banking.bankingDemo.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class PaymentService {

    public static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private BalanceRepository balanceRepository;

    @Autowired
    public PaymentService(CustomerRepository customerRepository, BalanceRepository balanceRepository) {
        this.customerRepository = customerRepository;
        this.balanceRepository = balanceRepository;
    }

    public void pay(long fromId, long toId, double amount) throws NotEnoughMoneyException, CustomerDoesNotExistException {

        Optional<Customer> from = customerRepository.findById(fromId);
        Optional<Customer> to = customerRepository.findById(toId);

        if (from.isPresent() && to.isPresent()) {
            Customer fromCustomer = from.get();
            Customer toCustomer = to.get();

            Balance fromBalance = balanceRepository.findByCustomerId(fromId);
            Balance toBalance = balanceRepository.findByCustomerId(toId);

            if (fromBalance.getBalance() >= amount) {
                fromBalance.setBalance(fromBalance.getBalance() - amount);
                toBalance.setBalance(toBalance.getBalance() + amount);
                customerRepository.save(fromCustomer);
                customerRepository.save(toCustomer);

            } else {
                logger.info(String.format("Not enough money to complete transfer between %s and %s", fromCustomer.getFirstName(), toCustomer.getFirstName()));
                throw new NotEnoughMoneyException();
            }
        } else {
            throw new CustomerDoesNotExistException();
        }
    }

    public class CustomerDoesNotExistException extends Exception {

    }

    public class NotEnoughMoneyException extends Exception {

    }

}
