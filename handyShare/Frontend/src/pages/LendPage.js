import React, { useState, useEffect } from 'react';
import { Layout, Menu, Card, Button, Modal, message, Row, Col, Switch } from 'antd';
import axios from 'axios';
import { SERVER_URL } from '../constants.js';
 import { Link } from 'react-router-dom';
import LendFormPage from '../components/LendingPage/LendFormPage';
import LentProductsList from '../components/LendingPage/LentProductsList';
import  AnimatedHeader   from '../components/Header.js';

const { Content, Sider } = Layout;

const LendPage = () => {
  const [view, setView] = useState('lendings');
  const [loading, setLoading] = useState(true);
  const [lentItems, setLentItems] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);

  useEffect(() => {
    // Fetch lent items when component mounts
    fetchLentItemsRefresh();
  }, []);

  const fetchLentItemsRefresh = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`${SERVER_URL}/api/v1/user/lendedProducts`, {
        headers: { Authorization: `Bearer ${token}` },
        withCredentials: true
      });
      setLentItems(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching lent items:', error);
      message.error('Failed to refresh lent items');
      setLoading(false);
    }
  };

  const handleUpdate = () => {
    fetchLentItemsRefresh();
    setIsModalVisible(false);
  };

  return (
    <Layout>
      <AnimatedHeader/>
      <Layout>
        <Sider>
          <Menu selectedKeys={[view]} onClick={(e) => setView(e.key)}>
            <Menu.Item key="lendings">Rented Products</Menu.Item>
             
          </Menu>
        </Sider>
        <Layout style={{ padding: '0 24px 24px' }}>
          <Content
            className="site-layout-background"
            style={{
              padding: 24,
              margin: 0,
              minHeight: 280,
            }}
          >
            {view === 'lendings' && (
              <LentProductsList 
                lentItems={lentItems} 
                onRefresh={fetchLentItemsRefresh}
              />
            )}
            {view === 'add' && (
              <LendFormPage onSuccess={() => {
                setView('lendings');
                fetchLentItemsRefresh();
              }} />
            )}
          </Content>
        </Layout>
      </Layout>

      {/* Edit Modal for Updates */}
      <Modal
        title="Update Lent Item"
        visible={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        footer={null}
      >
        {/* Your form for editing the lent product */}
      </Modal>
    </Layout>
  );
};

export default LendPage;