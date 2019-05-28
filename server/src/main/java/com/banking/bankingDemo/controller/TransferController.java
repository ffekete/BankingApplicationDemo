package com.banking.bankingDemo.controller;

import com.banking.bankingDemo.controller.wto.TransferRequestWTO;
import com.banking.bankingDemo.controller.wto.TransferResponseWTO;
import com.banking.bankingDemo.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "/b", description = "Bank payment transfer api.")
@RestController
@RequestMapping(value = "/b")
public class TransferController {

    @Autowired
    private PaymentService paymentService;

    @ApiOperation(value = "Simple landing page", httpMethod = "GET", response = String.class)
    @RequestMapping(value = "/l")
    public String get_landingPage() {
        return "Simple landing page";
    }

    @ApiOperation(value = "Controls transer between two customers", httpMethod = "POST", consumes = "application/json", response = TransferResponseWTO.class)
    @RequestMapping(value = "/transfer", method = RequestMethod.POST, consumes = "application/json")
    public TransferResponseWTO post_transfer(@RequestBody TransferRequestWTO transferRequestWTO) {
        TransferResponseWTO transferResponseWTO = new TransferResponseWTO();
        try {
            paymentService.pay(transferRequestWTO.getFrom(), transferRequestWTO.getTo(), transferRequestWTO.getAmount());
        } catch (PaymentService.CustomerDoesNotExistException cdne) {
            transferResponseWTO.setHttpStatus(HttpStatus.SC_BAD_REQUEST);
            transferResponseWTO.setText("Something went wrong during the transaction");
            return transferResponseWTO;

        } catch (PaymentService.NotEnoughMoneyException nme) {
            transferResponseWTO.setHttpStatus(HttpStatus.SC_BAD_REQUEST);
            transferResponseWTO.setText("Not enough money to complete the transfer");
            return transferResponseWTO;
        }

        transferResponseWTO.setHttpStatus(HttpStatus.SC_OK);
        transferResponseWTO.setText("Transaction completed successfully");
        return transferResponseWTO;
    }

}
