import React from 'react';
import { Card, Button, Row, Col, message, Tag, Typography, Space, Avatar } from 'antd';
import { DollarCircleOutlined, ClockCircleOutlined, UserOutlined, CheckCircleOutlined, StopOutlined } from '@ant-design/icons';
import axios from 'axios';
import { SERVER_URL } from '../../constants.js';

const { Text, Title } = Typography;

const LentProductsList = ({ lentItems, onRefresh }) => {
  const handleReturnItem = async (id) => {
    try {
      const token = localStorage.getItem('token');
      await axios.post(
        `${SERVER_URL}/api/v1/user/product/ReturnedLender`,
        {borrowId : id },
        { 
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true
        }
      );
      message.success('Item returned successfully');
      onRefresh();
    } catch (error) {
      console.error('Error returning item:', error);
      message.error('Failed to return item');
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <Title level={2} style={{ marginBottom: '24px' }}>Your Listed Products</Title>
      <Row gutter={[24, 24]}>
        {lentItems.map((item) => (
          <Col xs={24} sm={12} lg={8} key={item.product.id}>
            <Card
              hoverable
              className="product-card"
              style={{
                height: '100%',
                borderRadius: '12px',
                overflow: 'hidden',
                boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)'
              }}
              cover={
                <div style={{ 
                  position: 'relative',
                  paddingTop: '56.25%', // 16:9 aspect ratio
                  background: '#f5f5f5'
                }}>
                  <img
                    alt={item.product.name}
                    src={item.product.productImage}
                    style={{
                      position: 'absolute',
                      top: 0,
                      left: 0,
                      width: '100%',
                      height: '100%',
                      objectFit: 'contain',
                      padding: '8px',
                      backgroundColor: '#fff'
                    }}
                    onError={(e) => {
                      e.target.onerror = null;
                      e.target.src = '/Assets/placeholder-image.png'; // Add a placeholder image path
                    }}
                  />
                  <Tag color={item.product.available ? 'success' : 'error'}
                    style={{
                      position: 'absolute',
                      top: '10px',
                      right: '10px',
                      padding: '4px 8px',
                      borderRadius: '4px',
                      zIndex: 1
                    }}>
                    {item.product.available ? 'Available' : 'Unavailable'}
                  </Tag>
                </div>
              }
              actions={[
                <Button 
                  type="link" 
                  danger
                  style={{ height: 'auto', padding: '4px' }}
                  onClick={() => handleReturnItem(item.id)}
                >
                  <Space direction="vertical" size={0}>
                    <span>Return Item</span>
                    <Text type="secondary" style={{ fontSize: '12px' }}>Action</Text>
                  </Space>
                </Button>
              ]}
            >
              <Card.Meta
                title={
                  <Title level={4} style={{ 
                    marginBottom: '12px',
                    overflow: 'hidden',
                    textOverflow: 'ellipsis',
                    whiteSpace: 'nowrap'
                  }}>
                    {item.product.name}
                  </Title>
                }
                description={
                  <Space direction="vertical" size={12} style={{ width: '100%' }}>
                    <Space>
                      <DollarCircleOutlined style={{ color: '#52c41a' }} />
                      <Text strong>{`Rental Price: $${item.product.rentalPrice.toFixed(2)}`}</Text>
                    </Space>
                    
                    <Space>
                      <ClockCircleOutlined style={{ color: '#1890ff' }} />
                      <Text>{`Duration: ${item.duration} hours`}</Text>
                    </Space>
                    
                    <Space>
                      <DollarCircleOutlined style={{ color: '#722ed1' }} />
                      <Text>{`Total Amount: $${item.amount}`}</Text>
                    </Space>

                    <div style={{ 
                      background: '#f5f5f5', 
                      padding: '8px', 
                      borderRadius: '6px',
                      marginTop: '8px'
                    }}>
                      <Space>
                        <Avatar icon={<UserOutlined />} />
                        <div>
                          <Text type="secondary">Borrower</Text>
                          <br />
                          <Text strong>{item.borrower.name}</Text>
                        </div>
                      </Space>
                    </div>
                  </Space>
                }
              />
            </Card>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default LentProductsList;