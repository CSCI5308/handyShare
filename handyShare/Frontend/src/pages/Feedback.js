import React from 'react';
import HeaderBar from '../components/Homepage/HeaderBar';
import FeedbackForm from '../components/Feedback/FeedbackForm'; // Import the feedback form
import { Layout } from 'antd';

const { Header, Content, Footer } = Layout;

const Feedback = () => {
  return (
    <Layout style={{ minHeight: '100vh' }}>
      {/* Header */}
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

      {/* Main content */}
      <Content style={{ padding: '100px 50px', marginTop: '64px' }}>
        <div style={{ maxWidth: '600px', margin: '0 auto', background: '#fff', padding: '24px', borderRadius: '8px' }}>
          <FeedbackForm />
        </div>
      </Content>

      {/* Footer */}
      <Footer style={{ textAlign: 'center' }}>
        HandyShare ©{new Date().getFullYear()} Created by Group G02
      </Footer>
    </Layout>
  );
};

export default Feedback;
