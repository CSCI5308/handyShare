import React, { useState } from 'react';
import { CardElement, useStripe, useElements } from '@stripe/react-stripe-js';
import axios from 'axios';
import { Form, Button, Input, notification } from 'antd';

const PaymentForm = ({ onSuccess, amount }) => {
  const stripe = useStripe();
  const elements = useElements();
  const [currency, setCurrency] = useState('usd'); 
  const [loading, setLoading] = useState(false);

  const handlePayment = async () => {
    if (!stripe || !elements) {
      notification.error({ message: 'Stripe.js has not loaded yet.' });
      return;
    }

    const cardElement = elements.getElement(CardElement);

    const { error: paymentMethodError, paymentMethod } = await stripe.createPaymentMethod({
      type: 'card',
      card: cardElement,
    });

    if (paymentMethodError) {
      notification.error({
        message: 'Payment Error',
        description: paymentMethodError.message,
      });
      return;
    }

    setLoading(true);

    try {
      const response = await axios.post('http://localhost:8080/api/v1/all/payment/charge', {
        amount: parseInt(amount) * 100,
        currency: currency,
        paymentMethodId: paymentMethod.id,
      });

      const { clientSecret } = response.data;

      const { error: confirmError, paymentIntent } = await stripe.confirmCardPayment(clientSecret);

      if (confirmError) {
        notification.error({
          message: 'Payment Error',
          description: confirmError.message,
        });
      } else if (paymentIntent.status === 'succeeded') {
        notification.success({
          message: 'Payment Successful',
          description: `Payment for ${amount} ${currency} succeeded!`,
        });
        onSuccess();
      }
    } catch (error) {
      console.error('Payment processing error:', error.response?.data || error.message);
      notification.error({
        message: 'Payment Processing Error',
        description: error.response?.data?.error || 'Something went wrong',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '20px', maxWidth: '400px', margin: 'auto' }}>
      <Form layout="vertical" onFinish={handlePayment}>
        <Form.Item label="Amount">
          <Input value={amount} readOnly />
        </Form.Item>
        <Form.Item label="Currency">
          <Input
            value={currency}
            onChange={(e) => setCurrency(e.target.value)}
            placeholder="Enter currency (e.g., usd)"
            required
          />
        </Form.Item>
        <Form.Item label="Card Details">
          <CardElement options={{ hidePostalCode: true }} />
        </Form.Item>
        <Form.Item>
          <Button
            type="primary"
            htmlType="submit"
            loading={loading}
            style={{ width: '100%' }}
          >
            Pay Now
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default PaymentForm;
