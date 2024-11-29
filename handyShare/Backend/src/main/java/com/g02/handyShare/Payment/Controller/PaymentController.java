
package com.g02.handyShare.Payment.Controller;

import com.g02.handyShare.Payment.Request.PaymentRequest;
import com.g02.handyShare.Payment.Service.PaymentService;
import com.stripe.exception.StripeException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/all/payment")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PaymentController {

  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping("/checkout-session")
  public ResponseEntity<Map<String, Object>> createCheckoutSession(@RequestBody Map<String, Object> request) {
    try {
      Long amount = Long.parseLong(request.get("amount").toString());
      String currency = request.get("currency").toString();

      Map<String, Object> response = paymentService.createCheckoutSession(amount, currency);
      if (response.containsKey("error")) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
      }
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("error", "Failed to create Stripe checkout session"));
    }
  }

  @PostMapping("/onboard")
  public ResponseEntity<Map<String, Object>> onboardCustomer(@RequestBody PaymentRequest paymentRequest) {
    Map<String, Object> response = paymentService.onboardCustomer(paymentRequest);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/save-card")
  public ResponseEntity<Map<String, Object>> saveCard(@RequestBody PaymentRequest paymentRequest) throws Exception {
    Map<String, Object> response = paymentService.saveCard(paymentRequest);
    return ResponseEntity.ok(response);
  }

  @RequestMapping(method = RequestMethod.OPTIONS)
  public void handleOptions() {
    // Allow preflight request to pass
  }
}