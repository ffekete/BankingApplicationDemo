package com.banking.bankingDemo.controller;

import com.banking.bankingDemo.controller.wto.*;
import com.banking.bankingDemo.dto.Balance;
import com.banking.bankingDemo.dto.Customer;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class TransferControllerIntegrationTest {

    private static final String CREATE_CUSTOMER_URL = "http://localhost:8080/c/create";

    private HttpClient client = HttpClients.createDefault();

    private Customer customer;
    private Customer customer2;

    @Before
    public void setUp() throws IOException {
        // Creating customer 1
        customer = new Customer();
        customer.setFirstName("Bruce");
        customer.setLastName("Wayne");
        Balance balance = new Balance(100d);
        customer.setBalance(balance);
        customer.setId(2L);
        customer.setCustomerHistories(new HashSet<>());

        client = HttpClients.createDefault();
        HttpPost createUserRequest = new HttpPost("http://localhost:8080/c/create");
        CustomerRequestWTO customerRequestWTO = new CustomerRequestWTO();
        customerRequestWTO.setCustomer(customer);
        createUserRequest.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        createUserRequest.setEntity(new StringEntity(new Gson().toJson(customerRequestWTO)));
        HttpResponse response = client.execute(createUserRequest);

        assertThat(response.getStatusLine().getStatusCode(), is(200));

        // Creating customer 2
        customer2 = new Customer();
        customer2.setFirstName("Peter");
        customer2.setLastName("Parker");
        Balance balance2 = new Balance(500d);
        customer2.setBalance(balance2);
        customer2.setId(4L);
        customer2.setCustomerHistories(new HashSet<>());

        client = HttpClients.createDefault();
        HttpPost createUserRequestForUser2 = new HttpPost(CREATE_CUSTOMER_URL);
        CustomerRequestWTO customerRequestWTOForUser2 = new CustomerRequestWTO();
        customerRequestWTOForUser2.setCustomer(customer2);
        createUserRequestForUser2.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        createUserRequestForUser2.setEntity(new StringEntity(new Gson().toJson(customerRequestWTOForUser2)));
        HttpResponse responseForUser2 = client.execute(createUserRequestForUser2);

        assertThat(responseForUser2.getStatusLine().getStatusCode(), is(200));
    }

    @Test
    public void testTransactionShouldBeSuccessful() throws IOException {
        // GIVEN
        TransferRequestWTO transferRequestWTO = new TransferRequestWTO();
        transferRequestWTO.setAmount(50d);
        transferRequestWTO.setFrom(customer.getId());
        transferRequestWTO.setTo(customer2.getId());

        client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/b/transfer");
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        httpPost.setEntity(new StringEntity(new Gson().toJson(transferRequestWTO)));

        // WHEN
        HttpResponse transferResponse = client.execute(httpPost);

        // THEN
        TransferResponseWTO transferResponseWTO = new Gson().fromJson(IOUtils.toString(transferResponse.getEntity().getContent()), TransferResponseWTO.class);
        assertThat(transferResponseWTO.getHttpStatus(), is(200));
        assertThat(transferResponseWTO.getText(), is("Transaction completed successfully"));

        // do a history query for Bruce wayne
        HttpPost httpPostForHistory = new HttpPost("http://localhost:8080/h/history");
        BankStatementRequestWTO bankStatementRequestWTO = new BankStatementRequestWTO();
        bankStatementRequestWTO.setId(2L);
        httpPostForHistory.setEntity(new StringEntity(new Gson().toJson(bankStatementRequestWTO)));
        httpPostForHistory.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        client = HttpClients.createDefault();
        HttpResponse responseForHistory = client.execute(httpPostForHistory);

        assertThat(responseForHistory.getStatusLine().getStatusCode(), is(200));
        BankStatementWTO bankStatementWTO = new Gson().fromJson(IOUtils.toString(responseForHistory.getEntity().getContent()), BankStatementWTO.class);
        assertThat(bankStatementWTO.getBalance().getBalance(), is(50d));
        assertThat(bankStatementWTO.getFirstName(), is("Bruce"));

        // do a history query
        HttpPost httpPostForHistoryForcustomer2 = new HttpPost("http://localhost:8080/h/history");
        BankStatementRequestWTO bankStatementRequestWTOForcustomer2 = new BankStatementRequestWTO();
        bankStatementRequestWTOForcustomer2.setId(4L);
        httpPostForHistoryForcustomer2.setEntity(new StringEntity(new Gson().toJson(bankStatementRequestWTOForcustomer2)));
        httpPostForHistoryForcustomer2.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        client = HttpClients.createDefault();
        HttpResponse responseForHistoryForcustomer2 = client.execute(httpPostForHistoryForcustomer2);

        assertThat(responseForHistoryForcustomer2.getStatusLine().getStatusCode(), is(200));
        BankStatementWTO bankStatementWTOForcustomer2 = new Gson().fromJson(IOUtils.toString(responseForHistoryForcustomer2.getEntity().getContent()), BankStatementWTO.class);
        assertThat(bankStatementWTOForcustomer2.getBalance().getBalance(), is(550d));
        assertThat(bankStatementWTOForcustomer2.getFirstName(), is("Peter"));
    }


    @Test
    public void testTransactionShouldFail_notEnoughMoney() throws IOException {
        // GIVEN
        TransferRequestWTO transferRequestWTO = new TransferRequestWTO();
        transferRequestWTO.setAmount(150d);
        transferRequestWTO.setFrom(customer.getId());
        transferRequestWTO.setTo(customer2.getId());

        client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/b/transfer");
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        httpPost.setEntity(new StringEntity(new Gson().toJson(transferRequestWTO)));

        // WHEN
        HttpResponse transferResponse = client.execute(httpPost);

        // THEN
        TransferResponseWTO transferResponseWTO = new Gson().fromJson(IOUtils.toString(transferResponse.getEntity().getContent()), TransferResponseWTO.class);
        assertThat(transferResponseWTO.getHttpStatus(), is(400));
        assertThat(transferResponseWTO.getText(), is("Not enough money to complete the transfer"));
    }

    @Test
    public void testTransactionShouldFail_sendingUserDoesNotExist() throws IOException {
        // GIVEN
        TransferRequestWTO transferRequestWTO = new TransferRequestWTO();
        transferRequestWTO.setAmount(150d);
        transferRequestWTO.setFrom(-1L);
        transferRequestWTO.setTo(customer2.getId());

        client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/b/transfer");
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        httpPost.setEntity(new StringEntity(new Gson().toJson(transferRequestWTO)));

        // WHEN
        HttpResponse transferResponse = client.execute(httpPost);

        // THEN
        TransferResponseWTO transferResponseWTO = new Gson().fromJson(IOUtils.toString(transferResponse.getEntity().getContent()), TransferResponseWTO.class);
        assertThat(transferResponseWTO.getHttpStatus(), is(400));
        assertThat(transferResponseWTO.getText(), is("Something went wrong during the transaction"));
    }

    @Test
    public void testTransactionShouldFail_receivingUserDoesNotExist() throws IOException {
        // GIVEN
        TransferRequestWTO transferRequestWTO = new TransferRequestWTO();
        transferRequestWTO.setAmount(150d);
        transferRequestWTO.setFrom(customer.getId());
        transferRequestWTO.setTo(0L);

        client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/b/transfer");
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        httpPost.setEntity(new StringEntity(new Gson().toJson(transferRequestWTO)));

        // WHEN
        HttpResponse transferResponse = client.execute(httpPost);

        // THEN
        TransferResponseWTO transferResponseWTO = new Gson().fromJson(IOUtils.toString(transferResponse.getEntity().getContent()), TransferResponseWTO.class);
        assertThat(transferResponseWTO.getHttpStatus(), is(400));
        assertThat(transferResponseWTO.getText(), is("Something went wrong during the transaction"));
    }
}