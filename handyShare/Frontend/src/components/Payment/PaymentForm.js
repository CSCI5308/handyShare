// import React, { useState } from 'react';
// import { Form, Button} from 'antd'
// import InputMask from 'react-input-mask'; // Import react-input-mask for formatting inputs
// import visaLogo from '../../assets/visa.png';
// import masterCardLogo from '../../assets/mastercard.png';

// const PaymentForm = () => {
//   const [loading, setLoading] = useState(false);

//   const onFinish = (values) => {
//     setLoading(true);
//     // Simulate payment processing
//     setTimeout(() => {
//       setLoading(false);
//       message.success('Payment successful!');
//     }, 2000);
//   };

//   return (
//     <div>
//       {/* Display Supported Card Logos */}
//       <div className="flex justify-center mb-4">
//         <img src={visaLogo} alt="Visa" className="w-10 h-6 mx-2" />
//         <img src={masterCardLogo} alt="MasterCard" className="w-10 h-6 mx-2" />
//       </div>

//       <Form
//         layout="vertical"
//         onFinish={onFinish}
//         style={{ marginTop: '20px' }}
//       >
//         {/* Name on Card */}
//         <Form.Item
//           label="Name on Card"
//           name="name"
//           rules={[{ required: true, message: 'Please enter the name on your card' }]}
//         >
//           <Input placeholder="Enter your name" />
//         </Form.Item>

//         {/* Card Number */}
//         <Form.Item
//           label="Card Number"
//           name="cardNumber"
//           rules={[{ required: true, message: 'Please enter your card number' }]}
//         >
//           {/* Using InputMask for Card Number */}
//           <InputMask
//             mask="9999 9999 9999 9999"
//             maskChar=""
//             placeholder="1234 5678 9012 3456"
//           >
//             {(inputProps) => <Input {...inputProps} />}
//           </InputMask>
//         </Form.Item>

//         {/* Expiry Date */}
//         <Form.Item
//           label="Expiry Date"
//           name="expiry"
//           rules={[{ required: true, message: 'Please enter the expiry date' }]}
//         >
//           {/* Using InputMask for Expiry Date */}
//           <InputMask
//             mask="99/99"
//             maskChar=""
//             placeholder="MM/YY"
//           >
//             {(inputProps) => <Input {...inputProps} />}
//           </InputMask>
//         </Form.Item>

//         {/* CVV */}
//         <Form.Item
//           label="CVV"
//           name="cvv"
//           rules={[{ required: true, message: 'Please enter your CVV' }]}
//         >
//           <Input placeholder="123" maxLength={3} />
//         </Form.Item>

//         {/* Submit Button */}
//         <Button
//           type="primary"
//           htmlType="submit"
//           loading={loading}
//           style={{ width: '100%' }}
//         >
//           Pay Now
//         </Button>
//       </Form>
//     </div>
//   );
// };


import React, { useState } from 'react';
import { CardElement, useStripe, useElements } from '@stripe/react-stripe-js';
import axios from 'axios';
import { Form, Button, Input, notification } from 'antd';

const PaymentForm = ({ onSuccess }) => {
  const stripe = useStripe();
  const elements = useElements();
  const [amount, setAmount] = useState('');
  const [currency, setCurrency] = useState('usd'); // Adjust as needed
  const [loading, setLoading] = useState(false);

  const handlePayment = async () => {
      if (!stripe || !elements) {
          notification.error({ message: 'Stripe.js has not loaded yet.' });
          return;
      }

      const cardElement = elements.getElement(CardElement);

      // Create payment method
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

      setLoading(true); // Start loading

      try {
          // Step 1: Create a Payment Intent on the backend
          const response = await axios.post('http://localhost:8080/api/v1/all/payment/charge', {
              amount: parseInt(amount) * 100,
              currency: currency,
              paymentMethodId: paymentMethod.id,
          });

          // Extract clientSecret from response
          const { clientSecret } = response.data;

          // Step 2: Confirm the payment on the frontend using the clientSecret
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
                    <Input
                        type="number"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        placeholder="Enter amount"
                        required
                    />
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
                {/* Submit Button */}
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


