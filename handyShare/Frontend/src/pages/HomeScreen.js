import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom'; 
import HeaderBar from '../components/Homepage/HeaderBar.js';
import { Layout, Button, theme } from 'antd'; 
import CategoriesNavigation from '../components/Homepage/CategoriesNavigation.js';
import ContentHomeScreen from '../components/Homepage/ContentHomeScreen.js';

const { Header, Content, Footer, Sider } = Layout;

const HomeScreen = () => {
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const headerHeight = 70; 
  const [selectedCategory, setSelectedCategory] = useState(''); 
  const navigate = useNavigate(); 

  useEffect(() => {
    // Extract token from URL query parameters
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');


    if (token) {
      // Store the token in localStorage
      localStorage.setItem('token', token);
      console.log('Token stored:', token);

      // Remove the token parameter from the URL for a cleaner look
      navigate('/homepage', { replace: true });
    }
  }, [navigate]);
  
  // Function to handle category selection
  const handleCategorySelect = (category) => {
    setSelectedCategory(category);
  };

  // Function to handle "View Products" button click
  const handleViewProductsClick = () => {
    navigate('/products'); 
  };

  return (
    <Layout style={{ minHeight: '100vh' }}> {/* Ensures the layout fills the viewport */}
      {/* Fixed Header */}
      <Header
        style={{
          position: 'fixed', // Fixes the header at the top
          top: 0,
          left: 0,
          right: 0,
          zIndex: 1000, // Ensures the header stays on top
          display: 'flex',
          alignItems: 'center',
          background: '#3B7BF8',
          justifyContent: 'center',
          height: '10%', // Set the height of the header
          width: '100%',
        }}
      >
        <HeaderBar />
      </Header>

      {/* Main Content Area */}
      <Content> {/* Adds padding to avoid overlapping */}
        <Layout
          style={{
            padding: '24px 48px', 
            background: colorBgContainer,
            borderRadius: borderRadiusLG,
            minHeight: '100vh',
            marginTop: `${headerHeight}px`, 
            overflow: 'auto', // Allows scrolling of content area
          }}
        >
          {/* Scrollable Sidebar and Content */}
          <Sider
            style={{
              background: 'white',
              height: '100vh', // Full height for Sider
              overflowY: 'auto', // Enable scrolling in the sidebar
              scrollbarWidth: 'none',
              position: 'sticky', // Keeps it sticky when scrolling
              top: 0,
            }}
            width={200} // Set fixed width for the Sider
          >
            <CategoriesNavigation onCategorySelect={handleCategorySelect} /> {/* Pass handler to CategoriesNavigation */}
          </Sider>

          <Content
            style={{
              padding: '0 24px',
              minHeight: '280px',
              overflowY: 'auto', // Enables scrolling for the main content
            }}
          >
            <ContentHomeScreen category={selectedCategory} />

            {/* View Products Button */}
            <div style={{ marginTop: '24px', textAlign: 'center' }}>
              <Button type="primary" onClick={handleViewProductsClick}>
                View All Products
              </Button>
            </div>
          </Content>
        </Layout>
      </Content>

      {/* Footer */}
      <Footer
        style={{
          textAlign: 'center',
        }}
      >
        Ant Design ©{new Date().getFullYear()} Created by Ant UED
      </Footer>
    </Layout>
  );
};

export default HomeScreen;
