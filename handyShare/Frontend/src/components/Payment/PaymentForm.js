import React, { useState } from 'react';
import { Form, Input, Button, message } from 'antd';
import InputMask from 'react-input-mask'; // Import react-input-mask for formatting inputs
import visaLogo from '../../assets/visa.png';
import masterCardLogo from '../../assets/mastercard.png';

const PaymentForm = () => {
  const [loading, setLoading] = useState(false);

  const onFinish = (values) => {
    setLoading(true);
    // Simulate payment processing
    setTimeout(() => {
      setLoading(false);
      message.success('Payment successful!');
    }, 2000);
  };

  return (
    <div>
      {/* Display Supported Card Logos */}
      <div className="flex justify-center mb-4">
        <img src={visaLogo} alt="Visa" className="w-10 h-6 mx-2" />
        <img src={masterCardLogo} alt="MasterCard" className="w-10 h-6 mx-2" />
      </div>

      <Form
        layout="vertical"
        onFinish={onFinish}
        style={{ marginTop: '20px' }}
      >
        {/* Name on Card */}
        <Form.Item
          label="Name on Card"
          name="name"
          rules={[{ required: true, message: 'Please enter the name on your card' }]}
        >
          <Input placeholder="Enter your name" />
        </Form.Item>

        {/* Card Number */}
        <Form.Item
          label="Card Number"
          name="cardNumber"
          rules={[{ required: true, message: 'Please enter your card number' }]}
        >
          {/* Using InputMask for Card Number */}
          <InputMask
            mask="9999 9999 9999 9999"
            maskChar=""
            placeholder="1234 5678 9012 3456"
          >
            {(inputProps) => <Input {...inputProps} />}
          </InputMask>
        </Form.Item>

        {/* Expiry Date */}
        <Form.Item
          label="Expiry Date"
          name="expiry"
          rules={[{ required: true, message: 'Please enter the expiry date' }]}
        >
          {/* Using InputMask for Expiry Date */}
          <InputMask
            mask="99/99"
            maskChar=""
            placeholder="MM/YY"
          >
            {(inputProps) => <Input {...inputProps} />}
          </InputMask>
        </Form.Item>

        {/* CVV */}
        <Form.Item
          label="CVV"
          name="cvv"
          rules={[{ required: true, message: 'Please enter your CVV' }]}
        >
          <Input placeholder="123" maxLength={3} />
        </Form.Item>

        {/* Submit Button */}
        <Button
          type="primary"
          htmlType="submit"
          loading={loading}
          style={{ width: '100%' }}
        >
          Pay Now
        </Button>
      </Form>
    </div>
  );
};

export default PaymentForm;
