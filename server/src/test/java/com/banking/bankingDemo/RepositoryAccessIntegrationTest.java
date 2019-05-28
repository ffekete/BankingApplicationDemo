package com.banking.bankingDemo;

import com.banking.bankingDemo.dto.Balance;
import com.banking.bankingDemo.dto.Customer;
import com.banking.bankingDemo.repository.BalanceRepository;
import com.banking.bankingDemo.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:persistence.properties")
public class RepositoryAccessIntegrationTest {

    @Resource
    private CustomerRepository customerRepository;

    @Resource
    private BalanceRepository balanceRepository;

    @Before
    public void setUp() {
        customerRepository.deleteAll();
        balanceRepository.deleteAll();
    }

    @Test
    public void givenCustomer_whenSave_thenGetOk() {
        Customer customer = new Customer("john", "doe", null);
        customerRepository.save(customer);

        Optional<Customer> customers = customerRepository.findById(customer.getId());
        assertThat(customers.isPresent(), is(true));
        assertThat(customers.get(), is(customer));
    }

    @Test
    public void givenBankingDetails_whenSave_thenGetOk() {
        Balance balance = new Balance(1200.d);
        balanceRepository.save(balance);

        List<Balance> details = balanceRepository.findAll();
        assertThat(details.size(), is(1));
    }

    @Test
    public void givenBankingDetails_whenJoin_thenGetOk() {

        Balance balance = new Balance(1200.d);
        Customer customer = new Customer("john", "doe", balance);
        balance.setCustomer(customer);

        customerRepository.save(customer);

        Optional<Customer> customers = customerRepository.findById(customer.getId());
        assertThat(customers.isPresent(), is(true));
        assertThat(customers.get().getBalance(), is(balance));

        List<Balance> balances = balanceRepository.findAll();
        assertThat(balances.size(), is(1));
        assertThat(balances.get(0), is(balance));
    }
}