import React from 'react';
import { Card, Col, Row, Typography } from 'antd';

const { Text } = Typography;

const CategoriesNavigation = ({ onCategorySelect }) => {
  const categories = ['Electronics', 'Fashion', 'Home', 'Beauty', 'Sports', 'Toys', 'Books', 'Automotive'];

  return (
    <div style={{ padding: '24px' }}>
      <Row gutter={[16, 16]} justify="center" wrap> {/* Wrap allows flexible layout */}
        {categories.map((category) => (
          <Col key={category} xs={12} sm={8} md={6} lg={4}>
            <Card
              hoverable
              onClick={() => onCategorySelect(category)}
              style={{
                width: '100%',
                height: '100%', 
                aspectRatio: '1 / 1', 
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                borderRadius: '12px',
                boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.1)',
                backgroundColor: '#c3d9fa',
                cursor: 'pointer',
                transition: 'transform 0.3s, box-shadow 0.3s',
              }}
              onMouseEnter={(e) => {
                e.currentTarget.style.transform = 'scale(1.05)';
                e.currentTarget.style.boxShadow = '0px 8px 16px rgba(0, 0, 0, 0.2)';
              }}
              onMouseLeave={(e) => {
                e.currentTarget.style.transform = 'scale(1)';
                e.currentTarget.style.boxShadow = '0px 4px 12px rgba(0, 0, 0, 0.1)';
              }}
            >
              <Text strong style={{ fontSize: '16px', textAlign: 'center' }}>{category}</Text>
            </Card>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default CategoriesNavigation;
