import React, { useState, useEffect } from 'react';
import LendFormPage from '../components/LendingPage/LendFormPage.js'; 
import EditLendForm from '../components/LendingPage/EditLendForm.js'; 
import LendPageHeader from '../components/LendingPage/LendPageHeader.js';
import { Layout, Menu, Table, Button, Modal, message, Switch } from 'antd';
import axios from 'axios';
import { SERVER_URL } from '../constants.js';

const { Content, Sider } = Layout;

const LendPage = () => {
  const [view, setView] = useState('lendings');
  const [loading, setLoading] = useState(true);
  const [lentItems, setLentItems] = useState([]);
  const [editingItem, setEditingItem] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);

  // Define the columns for the Table
  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Price',
      dataIndex: 'rentalPrice',
      key: 'rentalPrice',
      render: (price) => `$${price.toFixed(2)}`,
    },
    {
      title: 'Availability',
      dataIndex: 'available',
      key: 'available',
      render: (available, record) => (
        <Switch
          checked={available}
          onChange={async (checked) => {
            try {
              const token = localStorage.getItem('token');
              await axios.put(`${SERVER_URL}/api/v1/user/product/changeAvailability/${record.id}`, 
                { status: checked },  // Send JSON with status key
                {
                  headers: { Authorization: `Bearer ${token}` },
                  withCredentials: true
                }
              );
              message.success(`Product is now ${checked ? 'available' : 'unavailable'}`);
              fetchLentItemsRefresh(); // Refresh the list after changing availability
            } catch (error) {
              console.error('Error updating availability:', error);
              message.error('Failed to update availability');
            }
          }}
        />
      ),
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (text, record) => (
        <>
          <Button
            type="link"
            onClick={() => handleEdit(record)}
          >
            Edit
          </Button>
          <Button
            type="link"
            danger
            onClick={() => handleDelete(record.id)}
          >
            Delete
          </Button>
        </>
      ),
    },
  ];

  useEffect(() => {
    // Fetch lent items when component mounts
    fetchLentItemsRefresh();
  }, []);

  const fetchLentItemsRefresh = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`${SERVER_URL}/api/v1/user/listUserItems`, {
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

  const handleEdit = (item) => {
    setEditingItem(item);
    setIsModalVisible(true);
  };

  const handleDelete = async (id) => {
    try {
      const token = localStorage.getItem('token');
      await axios.delete(`${SERVER_URL}/api/v1/user/product/delete/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
        withCredentials: true,
      });
      message.success('Item deleted successfully');
      fetchLentItemsRefresh(); // Refresh the list after deletion
    } catch (error) {
      console.error('Error deleting item:', error);
      message.error('Failed to delete the item');
    }
  };

  const handleUpdate = () => {
    fetchLentItemsRefresh();
    setIsModalVisible(false);
  };

  return (
    <Layout>
      <LendPageHeader />
      <Layout>
        <Sider>
          <Menu selectedKeys={[view]} onClick={(e) => setView(e.key)}>
            <Menu.Item key="lendings">Products</Menu.Item>
            <Menu.Item key="add">Add New product</Menu.Item>
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
              <LendFormPage onUpdate={fetchLentItemsRefresh} />
            )}
          </Content>
        </Layout>
      </Layout>

      {/* Edit Modal */}
      <Modal
        title="Edit Lent Item"
        visible={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        footer={null}
      >
        {editingItem && (
          <EditLendForm 
            item={editingItem} 
            onUpdate={handleUpdate} 
            onCancel={() => setIsModalVisible(false)} 
          />
        )}
      </Modal>
    </Layout>
  );
};

export default LendPage;
