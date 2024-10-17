import React, { useState } from 'react';
import HeaderBar from '../components/Homepage/HeaderBar.js';
import { Layout, theme } from 'antd';
import CategoriesNavigation from '../components/Homepage/CategoriesNavigation.js';
import ContentHomeScreen from '../components/Homepage/ContentHomeScreen.js';

const { Header, Content, Footer, Sider } = Layout;

const HomeScreen = () => {
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const headerHeight = 70; // Define the header height
  const [selectedCategory, setSelectedCategory] = useState(''); // State for selected category

  // Function to handle category selection
  const handleCategorySelect = (category) => {
    setSelectedCategory(category);
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
          background: '#fff',
          justifyContent: 'center',
          height: `${headerHeight}px`, // Set the height of the header
          width: '100%',
        }}
      >
        <HeaderBar />
      </Header>

      {/* Main Content Area */}
      <Content style={{ paddingTop: `${headerHeight}px` }}> {/* Adds padding to avoid overlapping */}
        <Layout
          style={{
            padding: '24px 48px', // Adjust padding based on your needs
            background: colorBgContainer,
            borderRadius: borderRadiusLG,
            minHeight: '100vh',
            marginTop: `${headerHeight}px`, // Adds margin to account for fixed header
            overflow: 'auto', // Allows scrolling of content area
          }}
        >
          {/* Scrollable Sidebar and Content */}
          <Sider
            style={{
              background: 'white',
              height: '100vh', // Full height for Sider
              overflowY: 'auto', // Enable scrolling in the sidebar
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
          <ContentHomeScreen category={selectedCategory}/>
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
