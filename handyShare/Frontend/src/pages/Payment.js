// import React, { useState } from 'react';
// import HeaderBar from '../components/Homepage/HeaderBar';
// import PaymentForm from '../components/Payment/PaymentForm';
// import FeedbackForm from '../components/Feedback/FeedbackForm'; // Import the feedback form
// import { Layout, message } from 'antd';
// import Feedback from './Feedback';

// const { Header, Content, Footer } = Layout;

// const Payment = () => {
//   const [paymentComplete, setPaymentComplete] = useState(false); // Track payment completion

//   const handlePaymentSuccess = () => {
//     message.success('Payment successful!');
//     setPaymentComplete(true); // Set paymentComplete to true after payment
//   };

//   return (
//     <Layout style={{ minHeight: '100vh' }}>
//       {/* Header */}
//       <Header
//         style={{
//           position: 'fixed',
//           top: 0,
//           left: 0,
//           right: 0,
//           zIndex: 1000,
//           background: '#3B7BF8',
//         }}
//       >
//         <HeaderBar />
//       </Header>

//       {/* Main content */}
//       <Content style={{ padding: '100px 50px', marginTop: '64px' }}>
//         <div style={{ maxWidth: '600px', margin: '0 auto', background: '#fff', padding: '24px', borderRadius: '8px' }}>
//           {!paymentComplete ? (
//             <>
//               <h2
//                 style={{
//                   textAlign: 'center',
//                   fontSize: '28px',
//                   fontWeight: 'bold',
//                   marginBottom: '20px',
//                 }}
//               >
//                 Payment
//               </h2>
//               <PaymentForm onSuccess={handlePaymentSuccess} /> {/* Handle payment success */}
//             </>
//           ) : (
//             <Feedback/> // Show feedback form after payment
//           )}
//         </div>
//       </Content>

//       {/* Footer */}
//       <Footer style={{ textAlign: 'center' }}>
//         HandyShare ©{new Date().getFullYear()} Created by Group G02
//       </Footer>
//     </Layout>
//   );
// };

// export default Payment;
import React, { useState } from 'react';
import { Elements } from '@stripe/react-stripe-js';
import { loadStripe } from '@stripe/stripe-js';
import HeaderBar from '../components/Homepage/HeaderBar';
import PaymentForm from '../components/Payment/PaymentForm';
import Feedback from './Feedback';
import { Layout, message } from 'antd';

const { Header, Content, Footer } = Layout;
console.log('Stripe Publishable Key:', process.env.REACT_APP_STRIPE_PUBLISHABLE_KEY);
const stripePromise = loadStripe(process.env.REACT_APP_STRIPE_PUBLISHABLE_KEY);

const Payment = () => {
  const [paymentComplete, setPaymentComplete] = useState(false);

  const handlePaymentSuccess = () => {
    message.success('Payment successful!');
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
                <PaymentForm onSuccess={handlePaymentSuccess} />
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
