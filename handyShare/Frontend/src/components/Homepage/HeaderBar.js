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
        backgroundColor: '#3B7BF8',
      
        height: '100%', 
        flex:1,
       
      }}
    >
     
      <div style={{ display: 'flex', alignItems: 'center' , justifyContent:'space-between'}}>
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

      {/* Right side buttons */}
      <Space>
        <Button
        href='/lend'
        >Lendings</Button>
        <Button>Borrowings</Button>
        <Button
          href='/profile'
          type="primary"
          shape="circle"
          icon={<UserOutlined />}
        />
      </Space>
    </Header>
  );
};

export default HeaderBar;
