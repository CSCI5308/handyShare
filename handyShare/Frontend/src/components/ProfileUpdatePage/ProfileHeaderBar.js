import React from 'react';
import { Button, Space, Layout } from 'antd';
import { UserOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';

const { Header } = Layout;

const ProfileHeaderBar = () => {
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
        <Button>Lendings</Button>
        <Button>Borrowings</Button>
        <Button
          type="primary"
          shape="circle"
          icon={<UserOutlined />}
        />
      </Space>
    </Header>
  );
};

export default ProfileHeaderBar;
