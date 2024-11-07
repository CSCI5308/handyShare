import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; 
import HeaderBar from '../components/Homepage/HeaderBar.js';
import { Layout, Button, Typography } from 'antd'; 
import CategoriesNavigation from '../components/Homepage/CategoriesNavigation.js';
import ContentHomeScreen from '../components/Homepage/ContentHomeScreen.js';

const { Header, Footer } = Layout;
const { Title } = Typography;

const HomeScreen = () => {
  const [selectedCategory, setSelectedCategory] = useState(null); 
  const navigate = useNavigate(); 

  // Selects a category to display products for that category
  const handleCategorySelect = (category) => {
    setSelectedCategory(category); 
  };

  // Navigate back to categories view
  const handleBackToCategoriesClick = () => {
    setSelectedCategory(null); 
  };

  // Navigate to the Products Page
  const handleViewAllProductsClick = () => {
    navigate('/products'); 
  };

  return (
    <Layout style={{ minHeight: '100vh', backgroundColor: '#f0f2f5' }}> 
      {/* Header */}
      <Header
        style={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          padding: '0 24px',
          background: '#3B7BF8',
          height: '70px',
          boxShadow: '0 2px 8px rgba(0, 0, 0, 0.15)',
        }}
      >
        <HeaderBar />
      </Header>

      {/* Main Content Area */}
      <Layout style={{ padding: '80px 24px 24px 24px', background: '#f0f2f5' }}>
        {/* Show Categories if no category is selected */}
        {!selectedCategory ? (
          <div style={{ textAlign: 'center' }}>
            <Title level={2}>Browse Categories</Title>
            <CategoriesNavigation onCategorySelect={handleCategorySelect} />
          </div>
        ) : (
          // Full-Screen Product Display with "View All Products" button
          <div style={{ padding: '24px', width: '100%', overflowY: 'auto' }}>
            <Title level={3} style={{ textAlign: 'center' }}>
              Products in {selectedCategory}
            </Title>
            
            <ContentHomeScreen category={selectedCategory} />

            {/* View All Products and Back to Categories Buttons */}
            <div style={{ textAlign: 'center', marginTop: '24px' }}>
              <Button type="primary" onClick={handleBackToCategoriesClick} style={{ marginRight: '12px' }}>
                Back to Categories
              </Button>
              <Button type="default" onClick={handleViewAllProductsClick}>
                View All Products
              </Button>
            </div>
          </div>
        )}
      </Layout>

      {/* Footer */}
      <Footer style={{ textAlign: 'center' }}>
        Ant Design ©{new Date().getFullYear()} Created by Ant UED
      </Footer>
    </Layout>
  );
};

export default HomeScreen;
