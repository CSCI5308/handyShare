import React from 'react';
import { Input, Button, Space, Layout } from 'antd';
import { SearchOutlined, UserOutlined } from '@ant-design/icons';

const { Header } = Layout;
const HeaderBar = () => {
  return (
    <Header
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        backgroundColor: '#fff',
        padding: '0 20px',
        boxShadow: '0 2px 8px #f0f1f2',
        height: '70px', // Matches header height
      }}
    >
      {/* Logo */}
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <img
          src="/path-to-your-logo.png"
          alt="App logo"
          style={{ height: '40px', marginRight: '20px' }}
        />
      </div>

      {/* Search Bar */}
      <Input
        placeholder="Search items"
        prefix={<SearchOutlined />}
        style={{ width: '400px' }}
      />

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

export default HeaderBar;
