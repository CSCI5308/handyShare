import React from 'react';
import { Button, Space, Layout, Dropdown, Menu, message } from 'antd';
import { UserOutlined, DownOutlined } from '@ant-design/icons';
import { Link, useNavigate } from 'react-router-dom';

const { Header } = Layout;

const ProfileHeaderBar = () => {
  const navigate = useNavigate();

  // Function to handle sign out
  const handleSignOut = () => {
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
        width: '100vw', 
      }}
    >
      {/* Logo */}
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <Link to="/homepage">
          <img
            src="/Assets/Logo.png"
            alt="App logo"
            style={{ height: '40px', marginRight: '30px' }}
          />
        </Link>
      </div>

      {/* Right side buttons */}
      <Space>
        <Button href='/lend'>Lendings</Button>
        <Button href='/borrow'>Borrowings</Button>
        
        {/* Dropdown for Profile and Sign Out */}
        <Dropdown overlay={menu} trigger={['click']}>
          <Button
            type="primary"
            shape="circle"
            icon={<UserOutlined />}
          >
          </Button>
        </Dropdown>
      </Space>
    </Header>
  );
};

export default ProfileHeaderBar;
