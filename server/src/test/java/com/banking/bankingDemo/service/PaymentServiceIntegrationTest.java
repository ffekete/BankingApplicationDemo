package com.banking.bankingDemo.service;

import com.banking.bankingDemo.dto.Balance;
import com.banking.bankingDemo.dto.Customer;
import com.banking.bankingDemo.repository.BalanceRepository;
import com.banking.bankingDemo.repository.CustomerRepository;
import org.hamcrest.number.IsCloseTo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:persistence.properties")
public class PaymentServiceIntegrationTest {

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private BalanceRepository balanceRepository;

    private PaymentService paymentService;

    @Test
    public void s() {
        paymentService = new PaymentService(customerRepository, balanceRepository);
        Balance balanceForCustomer1 = new Balance(101.2d);
        Customer customer = new Customer("John", "Doe", balanceForCustomer1);
        customerRepository.save(customer);

        Balance balanceForCustomer2 = new Balance(1d);
        Customer customer2 = new Customer("Vicky", "Gulie", balanceForCustomer2);
        customerRepository.save(customer2);
    }

    @Test
    public void testPayShouldPayToExistingCustomer() throws PaymentService.NotEnoughMoneyException, PaymentService.CustomerDoesNotExistException {
        // GIVEN
        paymentService = new PaymentService(customerRepository, balanceRepository);
        Balance balanceForCustomer1 = new Balance(101.2d);
        Customer customer = new Customer("John", "Doe", balanceForCustomer1);
        customerRepository.save(customer);

        Balance balanceForCustomer2 = new Balance(1d);
        Customer customer2 = new Customer("Vicky", "Gulie", balanceForCustomer2);
        customerRepository.save(customer2);
        // WHEN
        paymentService.pay(customer.getId(), customer2.getId(), 100d);

        // THEN
        Balance balanceForCustomer1AfterTransaction = balanceRepository.findByCustomerId(customer.getId());
        assertThat(balanceForCustomer1AfterTransaction.getBalance(), IsCloseTo.closeTo(1.2d, 0.000001));

        Balance balanceForCustomer2AfterTransaction = balanceRepository.findByCustomerId(customer2.getId());
        assertThat(balanceForCustomer2AfterTransaction.getBalance(), IsCloseTo.closeTo(101.0d, 0.000001));
    }

}