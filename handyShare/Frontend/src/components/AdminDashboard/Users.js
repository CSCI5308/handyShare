// import React, { useState, useEffect } from 'react';
// import { Button, Table, Input, message } from 'antd';
// import { DeleteOutlined, SearchOutlined } from '@ant-design/icons';
// import axios from 'axios';

// const Users = () => {
//   const [users, setUsers] = useState([]);
//   const [searchText, setSearchText] = useState('');

//   // Fetch users from backend
//   useEffect(async () => {
//     const token = localStorage.getItem('token');
//     // axios.get('http://172.17.0.99:8080/api/v1/admin/getUser')
//     const productResponse = await axios.get(`http://172.17.0.99:8080/api/v1/admin/getUser`,{
        
//       headers: {
                 
//         Authorization: `Bearer ${token}`
//     },
//     withCredentials: true

    
//     })
//     .then(response => {
//       setUsers(response.data);
//     })
//     .catch(error => {
//       message.error('Failed to load users');
//       console.error(error);
//     });
//   }, []);

//   const handleDelete = (key) => {
//     setUsers(users.filter(user => user.id !== key));
//   };

//   const columns = [
//     {
//       title: 'Name',
//       dataIndex: 'name',
//       key: 'name',
//     },
//     {
//       title: 'Email',
//       dataIndex: 'email',
//       key: 'email',
//     },
//     {
//       title: 'Role',
//       dataIndex: 'role',
//       key: 'role',
//     },
//     {
//       title: 'Action',
//       key: 'action',
//       render: (_, record) => (
//         <Button
//           type="link"
//           icon={<DeleteOutlined />}
//           onClick={() => handleDelete(record.id)}
//         />
//       ),
//     },
//   ];

//   // Filter users based on search text
//   const filteredUsers = users.filter(user =>
//     user.name.toLowerCase().includes(searchText.toLowerCase()) ||
//     user.email.toLowerCase().includes(searchText.toLowerCase())
//   );

//   return (
//     <div>
//       <h1 style={{ fontSize: '24px', fontWeight: 'bold' }}>Users</h1>
//       <Input
//         placeholder="Search users by name or email"
//         prefix={<SearchOutlined />}
//         value={searchText}
//         onChange={e => setSearchText(e.target.value)}
//         style={{ marginBottom: '8px', width: '300px', marginTop:'10px' }}
//       />
//       <Table 
//         columns={columns} 
//         dataSource={filteredUsers} 
//         pagination={{ pageSize: 5 }} 
//         rowKey="id"  // Ensure each row has a unique key (using id from backend)
//         style={{ marginTop: '20px' }} 
//       />
//     </div>
//   );
// };

// export default Users;

import React, { useState, useEffect } from 'react';
import { Button, Table, Input, message } from 'antd';
import { DeleteOutlined, SearchOutlined } from '@ant-design/icons';
import axios from 'axios';

const Users = () => {
  const [users, setUsers] = useState([]);
  const [searchText, setSearchText] = useState('');

  // Fetch users from the backend
  useEffect(() => {
    const fetchUsers = async () => {
      const token = localStorage.getItem('token');
      try {
        const response = await axios.get(`http://172.17.0.99:8080/api/v1/admin/getUser`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          withCredentials: true,
        });
        setUsers(response.data);
      } catch (error) {
        message.error('Failed to load users');
        console.error(error);
      }
    };
    fetchUsers();
  }, []);

  const handleDelete = (key) => {
    setUsers(users.filter((user) => user.id !== key));
  };

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Role',
      dataIndex: 'role',
      key: 'role',
    },
    {
      title: 'Action',
      key: 'action',
      render: (_, record) => (
        <Button
          type="link"
          icon={<DeleteOutlined />}
          onClick={() => handleDelete(record.id)}
        />
      ),
    },
  ];

  // Filter users based on search text
  const filteredUsers = users.filter(
    (user) =>
      user.name.toLowerCase().includes(searchText.toLowerCase()) ||
      user.email.toLowerCase().includes(searchText.toLowerCase())
  );

  return (
    <div>
      <h1 style={{ fontSize: '24px', fontWeight: 'bold' }}>Users</h1>
      <Input
        placeholder="Search users by name or email"
        prefix={<SearchOutlined />}
        value={searchText}
        onChange={(e) => setSearchText(e.target.value)}
        style={{ marginBottom: '8px', width: '300px', marginTop: '10px' }}
      />
      <Table
        columns={columns}
        dataSource={filteredUsers}
        pagination={{ pageSize: 5 }}
        rowKey="id" // Ensure each row has a unique key (using id from backend)
        style={{ marginTop: '20px' }}
      />
    </div>
  );
};

export default Users;
