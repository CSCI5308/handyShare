import React from 'react';
import { Layout, Button } from 'antd';

const { Header: AntHeader } = Layout;

const Header = ({ onSignOut }) => {
  return (
    <AntHeader style={{ background: '#001529', padding: '0 24px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
      <div style={{ fontSize: '24px', fontWeight: 'bold', color: 'white' }}>Admin Dashboard</div>
      <Button type="primary" onClick={onSignOut} style={{ marginLeft: 'auto' }}>
        Sign Out
      </Button>
    </AntHeader>
  );
};

export default Header;

