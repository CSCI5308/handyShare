import React from 'react';
import { Layout, Input, Button, Space, Menu, Dropdown, message } from 'antd';
import { SearchOutlined, UserOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';

const { Header } = Layout;

const HeaderBar = () => {
  const navigate = useNavigate();

  const handleSignOut = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('productId');
    localStorage.removeItem('userId');
    message.success('Successfully signed out');
    navigate('/login');
  };

  const menu = (
    <Menu>
      <Menu.Item key="profile" onClick={() => navigate('/profile')}>
        Profile
      </Menu.Item>
      <Menu.Item key="signout" onClick={handleSignOut}>
        Sign Out
      </Menu.Item>
    </Menu>
  );

  return (
    <Header
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        backgroundColor: '#3B7BF8',
        height: '100%',
        flex: 1,
      }}
    >
      <div 
        style={{ 
          display: 'flex', 
          alignItems: 'center', 
          justifyContent: 'space-between',
          cursor: 'pointer'
        }}
        onClick={() => navigate('/homepage')}
      >
        <img
          src="/Assets/Logo.png"
          alt="App logo"
          style={{ height: '40px', marginRight: '30px' }}
        />
      </div>

      <Input
        placeholder="Search items"
        prefix={<SearchOutlined />}
        style={{ width: '400px' }}
      />

      <Space>
        <Button onClick={() => navigate('/lendings')}>Lendings</Button>
        <Button onClick={() => navigate('/borrow')}>Borrowings</Button>
        <Dropdown overlay={menu} trigger={['click']}>
          <Button
            type="primary"
            shape="circle"
            icon={<UserOutlined />}
          />
        </Dropdown>
      </Space>
    </Header>
  );
};

export default HeaderBar;
