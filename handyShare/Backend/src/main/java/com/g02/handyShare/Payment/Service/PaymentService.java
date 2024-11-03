package com.g02.handyShare.Payment.Service;

import com.g02.handyShare.Payment.Request.PaymentRequest;

import com.stripe.Stripe;

import com.stripe.model.PaymentIntent;
import com.stripe.exception.StripeException;

import com.stripe.param.PaymentIntentCreateParams;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class PaymentService {

  // Hardcoded Stripe Secret Key
  private static final String STRIPE_SECRET_KEY = "sk_test_51QCMlpCj4cfMdtSgvGv3Y949jGNTGjZbSWHD6GKUJ0hczCCQ8f5SPtX91LqFA4RLPn96F4KsoG8mwjwPEmwEafUf00wIvarU3p";  // Replace with your actual secret key

  public PaymentService() {
      Stripe.apiKey = STRIPE_SECRET_KEY;  // Set the Stripe secret key directly
  }

//   public PaymentService(@Value("${STRIPE_SECRET_KEY}") String secretKey) {
//     Stripe.apiKey = secretKey; // Set the Stripe secret key
// }

public Map<String, Object> createPaymentIntent(PaymentRequest paymentRequest) throws Exception {
    // ChargeCreateParams params = ChargeCreateParams.builder()
    // .setAmount(paymentRequest.getAmount())
    // .setCurrency("usd")
    // .setSource(paymentRequest.getToken()) // Stripe token
    // .setDescription("Charge for " + paymentRequest.getName())
    // .build();

    // return Charge.create(params);
    // if (paymentRequest == null || paymentRequest.getPaymentMethodId() == null) {
    //   throw new IllegalArgumentException("Payment request is missing required fields");
    // }

    // PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
    //     .setAmount((long) paymentRequest.getAmount()) // Amount in cents
    //     .setCurrency(paymentRequest.getCurrency())
    //     .setPaymentMethod(paymentRequest.getPaymentMethodId())
    //     .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
    //     .setConfirm(true)
    //     .build();
    PaymentIntentCreateParams params =
    PaymentIntentCreateParams.builder()
        .setAmount((long) paymentRequest.getAmount()) // amount in cents
        .setCurrency(paymentRequest.getCurrency())
        .setPaymentMethod(paymentRequest.getPaymentMethodId())
        .setAutomaticPaymentMethods(
            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                .setEnabled(true)
                .build()
        )
        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("clientSecret", paymentIntent.getClientSecret());

        return responseData;
 
  }
}
