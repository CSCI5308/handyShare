package com.g02.handyShare.Payment.Service;

import com.g02.handyShare.Payment.Request.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

  private static final String STRIPE_SECRET_KEY = "sk_test_51QCMlpCj4cfMdtSgvGv3Y949jGNTGjZbSWHD6GKUJ0hczCCQ8f5SPtX91LqFA4RLPn96F4KsoG8mwjwPEmwEafUf00wIvarU3p";

  public PaymentService() {
    Stripe.apiKey = STRIPE_SECRET_KEY;
  }

  public Map<String, Object> createCheckoutSession(Long amount, String currency) {
    Map<String, Object> responseData = new HashMap<>();
    try {
      SessionCreateParams params = SessionCreateParams.builder()
          .setMode(SessionCreateParams.Mode.PAYMENT)
          .setSuccessUrl("http://localhost:3000/feedback")
          // .setCancelUrl("http://localhost:3000/cancel") //In case want to add a cancel
          // endpoint
          .addLineItem(SessionCreateParams.LineItem.builder()
              .setQuantity(1L)
              .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                  .setCurrency(currency)
                  .setUnitAmount(amount)
                  .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                      .setName("Sample Product")
                      .build())
                  .build())
              .build())
          .build();

      Session session = Session.create(params);
      responseData.put("url", session.getUrl());
    } catch (StripeException e) {
      e.printStackTrace();
      responseData.put("error", "Failed to create checkout session: " + e.getMessage());
    }
    return responseData;
  }

  public Map<String, Object> onboardCustomer(PaymentRequest paymentRequest) {
    try {
      CustomerCreateParams params = CustomerCreateParams.builder()
          .setEmail(paymentRequest.getEmail())
          .setName(paymentRequest.getName())
          .build();

      Customer customer = Customer.create(params);

      Map<String, Object> response = new HashMap<>();
      response.put("customerId", customer.getId());
      response.put("message", "Customer onboarded successfully in test mode.");

      return response;
    } catch (StripeException e) {
      throw new RuntimeException("Error onboarding customer: " + e.getMessage());
    }
  }

  public Map<String, Object> saveCard(PaymentRequest paymentRequest) throws StripeException {
    if (paymentRequest.getCustomerId() == null) {
        throw new IllegalArgumentException("CustomerId is required to save card.");
    }
    Map<String, Object> cardParams = new HashMap<>();
    cardParams.put("number", paymentRequest.getCardNumber());
    cardParams.put("exp_month", paymentRequest.getExpiryMonth());
    cardParams.put("exp_year", paymentRequest.getExpiryYear());
    cardParams.put("cvc", paymentRequest.getCvc());

    Map<String, Object> paymentMethodParams = new HashMap<>();
    paymentMethodParams.put("type", "card");
    paymentMethodParams.put("card", cardParams);

    PaymentMethod paymentMethod = PaymentMethod.create(paymentMethodParams);

    Map<String, Object> attachParams = new HashMap<>();
    attachParams.put("customer", paymentRequest.getCustomerId());
    paymentMethod.attach(attachParams);

    Customer customer = Customer.retrieve(paymentRequest.getCustomerId());
    Map<String, Object> updateParams = new HashMap<>();
    updateParams.put("invoice_settings", Map.of("default_payment_method", paymentMethod.getId()));
    customer.update(updateParams);

    // Return response
    Map<String, Object> responseData = new HashMap<>();
    responseData.put("message", "Card saved successfully and set as default.");
    responseData.put("paymentMethodId", paymentMethod.getId());
    return responseData;
}

}