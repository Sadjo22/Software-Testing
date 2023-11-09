package com.peguy.softwaretesting.payment;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {

  private final PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
      this.paymentService = paymentService;
  }

  @PostMapping
  public ResponseEntity<String> MakeNewPayment(@RequestBody PaymentRequest paymentRequest) {
      paymentService.chargeCard(paymentRequest.getPayment().getCustomerId(), paymentRequest);
      HttpHeaders headers = new HttpHeaders();
      headers.add("Payment-Header", "paymentOk");
      return new ResponseEntity<>("ok", headers, HttpStatus.OK);
  }

}
