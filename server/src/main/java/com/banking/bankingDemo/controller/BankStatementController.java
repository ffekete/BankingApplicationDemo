package com.banking.bankingDemo.controller;

import com.banking.bankingDemo.controller.wto.BankStatementRequestWTO;
import com.banking.bankingDemo.controller.wto.BankStatementWTO;
import com.banking.bankingDemo.service.BankStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/h")
public class BankStatementController {

    @Autowired
    private BankStatementService bankStatementService;

    @RequestMapping(path = "/history", method = RequestMethod.POST)
    public BankStatementWTO getBankStatement(@RequestBody BankStatementRequestWTO bankStatementRequestWTO) {

        Long id = bankStatementRequestWTO.getId();
        if (id == null) {
            return null;
        }

        return bankStatementService.getBankStatement(id);

    }

}
