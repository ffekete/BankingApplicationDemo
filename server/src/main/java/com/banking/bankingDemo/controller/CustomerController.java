package com.banking.bankingDemo.controller;

import com.banking.bankingDemo.controller.wto.CustomerRequestWTO;
import com.banking.bankingDemo.dto.Customer;
import com.banking.bankingDemo.repository.CustomerRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api("For maintaining customers")
@RestController
@RequestMapping("/c")
public class CustomerController {

    @Resource
    private CustomerRepository customerRepository;

    @ApiOperation(value = "Creates customer with balance", httpMethod = "POST")
    @RequestMapping(method = RequestMethod.POST, path = "/create")
    public Customer create_post(@RequestBody CustomerRequestWTO customerRequestWTO) {
        if (customerRequestWTO != null) {
            Customer customer = customerRequestWTO.getCustomer();
            customerRepository.save(customer);
            return customer;
        }
        return null;
    }

}
