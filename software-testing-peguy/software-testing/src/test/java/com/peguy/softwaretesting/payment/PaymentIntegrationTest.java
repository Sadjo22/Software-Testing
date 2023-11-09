package com.peguy.softwaretesting.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peguy.softwaretesting.customer.Customer;
import com.peguy.softwaretesting.customer.CustomerRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import javax.net.ssl.SSLEngineResult;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegrationTest {

     @Autowired
     PaymentRepository paymentRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        //Given
        UUID id = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        Long paymentId = 1L;
        String creditCard = "creditCard";
        Currency curr = Currency.USD;
        String paymentDoneSuccessfully = "paymentDoneSuccessfully";
        String phoneNumber = "3298711186";
        String name = "James";
        Customer customer = new Customer(id, name, phoneNumber);
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        //When
        Payment payment  = new Payment(paymentId, id, new BigDecimal(10), curr, creditCard, paymentDoneSuccessfully);
        PaymentRequest paymentRequest = new PaymentRequest(payment);
        ResultActions resultActions = mockMvc.perform(put("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerRegistrationRequest)));

        ResultActions paymentResultActions = mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(paymentRequest)));

        //Then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(header().exists("Custom-Header"));
        resultActions.andExpect(header().string("Custom-Header", "foo"));

        paymentResultActions.andExpect(status().isOk());
        paymentResultActions.andExpect(header().exists("Payment-Header"));
        paymentResultActions.andExpect(header().string("Payment-Header", "paymentOk"));
        assertThat(paymentRepository.findById(paymentId)).isPresent();

    }
}
