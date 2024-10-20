import React from 'react';
import { Input, Button, Space, Layout } from 'antd';
import { SearchOutlined, UserOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';

const { Header: AntHeader } = Layout;

export function ProductHeader() {
  return (
    <AntHeader
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

      <Input
        placeholder="Search items"
        prefix={<SearchOutlined />}
        style={{ width: '400px' }}
      />

      <Space>
        <Link to="/lend">
          <Button>Lendings</Button>
        </Link>
        <Button>Borrowings</Button>
        <Button
          href='/profile'
          type="primary"
          shape="circle"
          icon={<UserOutlined />}
        />
      </Space>
    </AntHeader>
  );
}
