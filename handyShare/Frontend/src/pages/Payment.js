import React, { useState } from 'react';
import { Elements } from '@stripe/react-stripe-js';
import { loadStripe } from '@stripe/stripe-js';
import HeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar.js';
import PaymentForm from '../components/Payment/PaymentForm';
import Feedback from './Feedback';
import { Layout, message } from 'antd';
import { useLocation } from 'react-router-dom';

const { Header, Content, Footer } = Layout;

// const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLISHABLE_KEY);

const STRIPE_PUBLISHABLE_KEY = 'pk_test_51QCMlpCj4cfMdtSgAbaQWosVrynmSsvjqzFwkYyvLRzQUtggclYAr6mmKAQ5iQJV4v78ZIt8m7rmtzohoWPB4EvZ00kOiOJFmR';  // Replace this with your actual Stripe publishable key
console.log('Stripe Publishable Key:', STRIPE_PUBLISHABLE_KEY);

const stripePromise = loadStripe(STRIPE_PUBLISHABLE_KEY);

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
        <div style={{ maxWidth: '600px', margin: '0 auto', background: '#fff', padding: '24px', borderRadius: '8px' }}>
          {!paymentComplete ? (
            <>
              <h2
                style={{
                  textAlign: 'center',
                  fontSize: '28px',
                  fontWeight: 'bold',
                  marginBottom: '20px',
                }}
              >
                Payment
              </h2>

              <Elements stripe={stripePromise}>
                <PaymentForm onSuccess={handlePaymentSuccess} amount={amount} />
              </Elements>
            </>
          ) : (
            <Feedback />
          )}
        </div>
      </Content>

      <Footer style={{ textAlign: 'center' }}>
        HandyShare ©{new Date().getFullYear()} Created by Group G02
      </Footer>
    </Layout>
  );
};

export default Payment;
