import React, { useState, useEffect } from 'react';
import HeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar.js';
import LendFormPage from '../components/LendingPage/LendFormPage.js'; 
import EditLendForm from '../components/LendingPage/EditLendForm.js'; 
import { Layout, Menu, Table, Button, Modal, message } from 'antd';
import axios from 'axios';
import { SERVER_URL } from '../constants.js';


const { Content, Sider } = Layout;

const LendPage = () => {
  const [view, setView] = useState('lendings');
  const [loading, setLoading] = useState(true);
  const [lentItems, setLentItems] = useState([]);
  const [editingItem, setEditingItem] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);

  useEffect(() => {

    const fetchLentItems = async () => {
      try {
        const response = await axios.get(SERVER_URL+"/api/v1/all/lending/items")
        setLentItems(response.data); // Add the fetched data
        setLoading(false);
      } catch (error) {
        console.error('Error fetching lent items:', error);
        message.error('Failed to load lent items');
        setLoading(false);
      }
    };

    fetchLentItems();
  }, []);

  const fetchLentItems = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`${SERVER_URL}/api/v1/all/lending/items`, {
        headers: {
          Authorization: `Bearer ${token}`
        },
        withCredentials: true
      });
      setLentItems(response.data);
    } catch (error) {
      console.error('Error fetching lent items:', error);
      message.error('Failed to load lent items');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      const token = localStorage.getItem('token');
      await axios.delete(`${SERVER_URL}/api/v1/all/lending/item/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`
        },
        withCredentials: true
      });
      message.success('Item deleted successfully');
      fetchLentItems();
    } catch (error) {
      console.error('Error deleting item:', error);
      message.error('Failed to delete item');
    }
  };

  const handleEdit = (item) => {
    setEditingItem(item);
    setIsModalVisible(true);
  };

  const handleModalCancel = () => {
    setEditingItem(null);
    setIsModalVisible(false);
    setEditingItem(null);
  };

  const handleUpdate = async (formData) => {
    if (!editingItem || !editingItem.id) {
      message.error('Invalid item data.');
      return;
    }
    try {
      const token = localStorage.getItem('token');
      console.log('Updating item with ID:', editingItem.id); // Debugging log
      console.log('FormData:', formData); // Debugging log
      await axios.put(`${SERVER_URL}/api/v1/all/lending/item/${editingItem.id}`, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        },
        withCredentials: true
      });
      message.success('Item updated successfully');
      setIsModalVisible(false);
      fetchLentItems();
    } catch (error) {
      console.error('Error updating item:', error.response || error);
      message.error('Failed to update item');
    }
  };

  const handleAdd = async (newItem) => {
    try {
      await axios.post('http://localhost:8080/api/v1/all/lending/item', newItem);
      message.success('Item added successfully');
      setView('lendings');
      fetchLentItems();
    } catch (error) {
      console.error('Error adding item:', error);
      message.error('Failed to add item');
    }
  };

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_, record) => (
        <>
          <Button type="link" onClick={() => handleEdit(record)}>Edit</Button>
          <Button type="link" danger onClick={() => handleDelete(record.id)}>Delete</Button>
        </>
      ),
    },
  ];

  const handleMenuClick = (e) => {
    setView(e.key);
  };

  return (
    <div>
      <HeaderBar />
      <Layout>
        <Sider width={200}>
          <Menu
            mode="inline"
            selectedKeys={[view]}
            onClick={handleMenuClick}
            style={{ height: '100%', borderRight: 0 }}
          >
            <Menu.Item key="lendings">Lendings</Menu.Item>
            <Menu.Item key="add">Add New Lending</Menu.Item>
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
              <Table 
                columns={columns} 
                dataSource={lentItems} 
                rowKey="id" 
                loading={loading} 
              />
            )}
            {view === 'add' && (
              <LendFormPage onProductAdded={fetchLentItems} />
            )}
          </Content>
        </Layout>
      </Layout>

      {/* Edit Modal */}
      <Modal
        title="Edit Lent Item"
        visible={isModalVisible}
        onCancel={handleModalCancel}
        footer={null}
      >
        {editingItem && (
          <EditLendForm 
            item={editingItem} 
            onUpdate={handleUpdate} 
            onCancel={handleModalCancel} 
          />
        )}
      </Modal>
    </div>
  );
};

export default LendPage;
