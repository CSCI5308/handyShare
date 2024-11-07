import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Card, Col, Row, Spin, Typography, message } from 'antd';
import { SERVER_URL } from '../../constants';

const { Title } = Typography;

const ContentHomeScreen = ({ category }) => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        setLoading(true);

        // Retrieve the token from localStorage
        const token = localStorage.getItem('token');
        
        const response = await axios.get(`${SERVER_URL}/api/v1/products`, {
          params: { category },
          headers: {
            Authorization: `Bearer ${token}`,
          },
          withCredentials: true,
        });

        setProducts(response.data);
      } catch (error) {
        console.error("Error fetching products", error);
        message.error("Failed to load products. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, [category]);

  if (loading) {
    return <Spin tip="Loading products..." style={{ display: 'flex', justifyContent: 'center', marginTop: 50 }} />;
  }

  return (
    <div style={{ padding: '24px' }}>
      <Row gutter={[16, 16]} justify="center">
        {products.length ? (
          products.map((product) => (
            <Col key={product.id} xs={24} sm={12} md={8} lg={6}>
              <Card
                hoverable
                style={{
                  width: '150px', // Set width for square holders
                  height: '150px', // Set height to make them square
                  display: 'flex',
                  flexDirection: 'column',
                  justifyContent: 'center',
                  alignItems: 'center',
                  boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
                }}
                cover={
                  <img
                    alt={product.name}
                    src={product.image || 'path/to/default/image.jpg'} // Add a placeholder image path
                    style={{
                      width: '100%',
                      height: '100%',
                      objectFit: 'cover',
                    }}
                  />
                }
              >
                <Card.Meta title={product.name} description={`$${product.price}`} />
              </Card>
            </Col>
          ))
        ) : (
          <div style={{ textAlign: 'center', width: '100%' }}>
            <Title level={4}>No products found in this category</Title>
          </div>
        )}
      </Row>
    </div>
  );
};

export default ContentHomeScreen;
