import React from 'react';
import { Input, Button, Space, Layout, Dropdown, Menu, message } from 'antd';
import { SearchOutlined, UserOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';

const { Header } = Layout;

const HeaderBar = () => {
  const navigate = useNavigate();

  // Function to handle sign out
  const handleSignOut = () => {
    // Clear user session data
    localStorage.removeItem('token');
    localStorage.removeItem('role'); 

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
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <img
          src="/Assets/Logo.png"
          alt="App logo"
          style={{ height: '40px', marginRight: '30px' }}
        />
      </div>

      {/* Search Bar */}
      <Input
        placeholder="Search items"
        prefix={<SearchOutlined />}
        style={{ width: '400px' }}
      />

      {/* Right side buttons and dropdown */}
      <Space>
        <Button href='/lend'>Lendings</Button>
        <Button>Borrowings</Button>
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
