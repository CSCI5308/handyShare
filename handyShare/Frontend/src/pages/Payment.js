import React, { useState } from 'react';
import { Elements } from '@stripe/react-stripe-js';
import { loadStripe } from '@stripe/stripe-js';
import HeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar.js';
import PaymentForm from '../components/Payment/PaymentForm';
import Feedback from './Feedback';
import { Layout, message, Steps } from 'antd';
import { useLocation } from 'react-router-dom';
import { motion } from 'framer-motion';

const { Header, Content, Footer } = Layout;
const { Step } = Steps;

const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLISHABLE_KEY);

const Payment = () => {
  const [paymentComplete, setPaymentComplete] = useState(false);
  const location = useLocation();
  const { amount } = location.state || {}; // Retrieve amount from RentSummaryPage

  const handlePaymentSuccess = () => {
    setPaymentComplete(true);
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header
        style={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          zIndex: 1000,
          background: '#3B7BF8',
        }}
      >
        <HeaderBar />
      </Header>

      <Content style={{ padding: '100px 50px', marginTop: '64px' }}>
        <div
          style={{
            maxWidth: '600px',
            margin: '0 auto',
            background: '#fff',
            padding: '24px',
            borderRadius: '12px',
            boxShadow: '0 8px 16px rgba(0, 0, 0, 0.15)',
          }}
        >
          {/* Payment Steps */}
          <Steps
            current={1}
            style={{ marginBottom: '20px' }}
            labelPlacement="vertical"
          >
            <Step title="Rent Summary" />
            <Step title="Payment" />
            <Step title="Feedback" />
          </Steps>

          {!paymentComplete ? (
            <>
              <motion.h2
                style={{
                  textAlign: 'center',
                  fontSize: '28px',
                  fontWeight: 'bold',
                  marginBottom: '20px',
                  color: '#3B7BF8',
                }}
                initial={{ opacity: 0, y: -10 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.5 }}
              >
                Payment
              </motion.h2>

              <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ duration: 0.5, delay: 0.2 }}
              >
                <Elements stripe={stripePromise}>
                  <PaymentForm onSuccess={handlePaymentSuccess} amount={amount} />
                </Elements>
              </motion.div>
            </>
          ) : (
            <Feedback />
          )}
        </div>
      </Content>

      <Footer style={{ textAlign: 'center', background: '#f0f2f5', padding: '20px' }}>
        <p style={{ color: '#3B7BF8', fontWeight: 'bold' }}>
          HandyShare ©{new Date().getFullYear()} Created by Group G02
        </p>
      </Footer>
    </Layout>
  );
};

export default Payment;
