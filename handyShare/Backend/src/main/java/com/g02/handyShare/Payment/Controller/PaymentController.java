package com.g02.handyShare.Payment.Controller;

import com.g02.handyShare.Payment.Request.PaymentRequest;
import com.g02.handyShare.Payment.Service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/all/payment")
@CrossOrigin(origins = "http://172.17.0.99:3000", allowCredentials = "true")
public class PaymentController {

  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }


  @PostMapping("/charge")
  public ResponseEntity<Map<String, Object>> createPaymentIntent(@RequestBody PaymentRequest paymentRequest) throws Exception {
    long amount = paymentRequest.getAmount(); // amount in cents
    String currency = paymentRequest.getCurrency();
    String paymentMethodId = paymentRequest.getPaymentMethodId();


    if (paymentMethodId == null || paymentMethodId.isEmpty()) {
      throw new Exception("Payment method ID must be provided.");
  }
    // Create a PaymentRequest object
    // PaymentRequest paymentRequest = new PaymentRequest(amount, currency);

    // // Call the payment service with the PaymentRequest object
    // Map<String, Object> response = paymentService.createPaymentIntent(paymentRequest);
  Map<String,Object> response = paymentService.createPaymentIntent(paymentRequest);
    return ResponseEntity.ok(response);
    
}

  @RequestMapping(method = RequestMethod.OPTIONS)
  public void handleOptions() {
    // this will allow the preflight request to pass
  }
}