import React, { useState } from 'react';
import { Button, Layout, Menu, Form, Input } from 'antd';
import ProfileHeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar'; 
import defaultImage from '../components/ProfileUpdatePage/defaultProfileImage.png'; 
import { useNavigate } from 'react-router-dom'; 

const { Header, Content, Sider } = Layout;

const Profile = () => {
  const navigate = useNavigate(); 

  // Default current user details
  const [userDetails] = useState({
    name: 'John Doe', 
    email: 'johndoe@example.com', 
    address: null, 
    phone: '123-456-7890',
    pincode: null, 
    profileImage: null,
  });

  // State to handle the current view (Profile or Change Password)
  const [view, setView] = useState('profile');

  // Handle menu item click
  const handleMenuClick = (e) => {
    setView(e.key);
  };

  // Handle password change form submission
  const onFinishPasswordChange = (values) => {
    console.log('Password change values:', values);
  };

  // Navigate to the Profile Update page with user details
  const handleEditClick = () => {
    navigate('/profile-update', { state: { userDetails } }); 
  };

  return (
    <Layout>
      <Header>
        <ProfileHeaderBar/>
      </Header>
      <Layout>
        <Sider width={200}>
          <Menu
            mode="inline"
            defaultSelectedKeys={['profile']}
            style={{ height: '100%', borderRight: 0 }}
            onClick={handleMenuClick}
          >
            <Menu.Item key="profile">Profile</Menu.Item>
            <Menu.Item key="changePassword">Change Password</Menu.Item>
          </Menu>
        </Sider>
        <Layout style={{ padding: '20px' }}>
          <Content style={{ padding: '20px', background: '#fff' }}>
            {view === 'profile' ? (
              <>
                <h1 style={{ fontSize: '24px', fontWeight: 'bold' }}>Profile</h1>
                <div style={{ display: 'flex', alignItems: 'center' }}>
                  <img
                    src={userDetails.profileImage || defaultImage} // Use the imported default image
                    alt="Profile"
                    style={{
                      height: '100px',
                      width: '100px',
                      borderRadius: '50%', // Circular image
                      marginRight: '20px',
                      border: '2px solid #4CAF50', // Border for visibility
                    }}
                  />
                  <div>
                    <p><strong>Name:</strong> {userDetails.name || 'N/A'}</p>
                    <p><strong>Email:</strong> {userDetails.email}</p>
                    <p><strong>Address:</strong> {userDetails.address || 'N/A'}</p>
                    <p><strong>Phone:</strong> {userDetails.phone || 'N/A'}</p>
                    <p><strong>Pincode:</strong> {userDetails.pincode || 'N/A'}</p>
                    <Button 
                        type="primary" 
                        onClick={handleEditClick} // Call the edit function
                        style={{ marginTop: '10px' }}  
                    >
                      Edit
                    </Button>
                  </div>
                </div>
              </>
            ) : (
              <>
                <h1 style={{ fontSize: '24px', fontWeight: 'bold' }}>Change Password</h1>
                <Form
                  layout="vertical"
                  onFinish={onFinishPasswordChange}
                >
                  <Form.Item
                    name="currentPassword"
                    label="Current Password"
                    rules={[{ required: true, message: 'Please input your current password!' }]}
                  >
                    <Input.Password />
                  </Form.Item>
                  <Form.Item
                    name="newPassword"
                    label="New Password"
                    rules={[{ required: true, message: 'Please input your new password!' }]}
                  >
                    <Input.Password />
                  </Form.Item>
                  <Form.Item
                    name="confirmPassword"
                    label="Confirm New Password"
                    rules={[
                      { required: true, message: 'Please confirm your new password!' },
                      ({ getFieldValue }) => ({
                        validator(_, value) {
                          if (!value || getFieldValue('newPassword') === value) {
                            return Promise.resolve();
                          }
                          return Promise.reject(new Error('The two passwords do not match!'));
                        },
                      }),
                    ]}
                  >
                    <Input.Password />
                  </Form.Item>
                  <Form.Item>
                    <Button type="primary" htmlType="submit">
                      Submit
                    </Button>
                  </Form.Item>
                </Form>
              </>
            )}
          </Content>
        </Layout>
      </Layout>
    </Layout>
  );
};

export default Profile;
