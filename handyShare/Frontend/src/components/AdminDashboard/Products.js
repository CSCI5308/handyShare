import React, { useState, useEffect } from 'react';
import { Button, Table, Input, message, Modal, Switch, Select } from 'antd';
import { DeleteOutlined, SearchOutlined } from '@ant-design/icons';
import axios from 'axios';
import { SERVER_URL } from '../../constants';

const Products = () => {
  const [products, setProducts] = useState([]);
  const [searchText, setSearchText] = useState('');
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [newProduct, setNewProduct] = useState({
    name: '',
    rentalPrice: '',
    category: '',
    available: true, // Updated to "available" to match back-end
  });
  const [editMode, setEditMode] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [availableFilter, setAvailableFilter] = useState(null);
  const [categoryFilter, setCategoryFilter] = useState('');

  // Function to fetch products from the API
  const fetchProducts = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(SERVER_URL+"/api/v1/user/allProducts",{
      
        headers: {
                   
          Authorization: `Bearer ${token}`
      },
      withCredentials: true
      
      });
      setProducts(response.data);
    } catch (error) {
      message.error('Failed to load products');
    }
  };

  // Fetch all products when the component is mounted
  useEffect(() => {
    fetchProducts();
  }, []);

  // Function to handle deleting a product
  const handleDelete = async (id) => {
    try {
      const token = localStorage.getItem('token'); // Retrieve user token
      await axios.delete(`http://172.17.0.99:8080/api/v1/user/product/delete/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`, // Include token in the request header
        },
        withCredentials: true,
      });
      setProducts(products.filter(product => product.id !== id)); // Remove deleted product from state
      message.success('Product deleted successfully!');
    } catch (error) {
      if (error.response && error.response.status === 401) {
        message.error('Unauthorized: Please check your credentials');
      } else {
        message.error('Failed to delete product');
      }
    }
  };
  

  // Open the modal for adding or editing a product
  const showModal = (product = null) => {
    if (product) {
      setEditMode(true);
      setSelectedProduct(product);
      setNewProduct({
        name: product.name,
        rentalPrice: product.rentalPrice,
        category: product.category,
        available: product.available, // Updated to "available" to match back-end
      });
    } else {
      setEditMode(false);
      setNewProduct({ name: '', rentalPrice: '', category: '', available: true });
    }
    setIsModalVisible(true);
  };

  // Close the modal
  const handleCancel = () => {
    setIsModalVisible(false);
    setNewProduct({ name: '', rentalPrice: '', category: '', available: true });
    setEditMode(false);
  };

  // Save or update product
  const handleOk = async () => {
    if (!newProduct.name.trim() || !newProduct.rentalPrice.trim() || !newProduct.category.trim()) {
      message.error('All fields are required');
      return;
    }
  
    const token = localStorage.getItem('token'); // Retrieve user token
  
    try {
      if (editMode) {
        // Update product
        await axios.put(`http://172.17.0.99:8080/api/v1/user/products/update/${selectedProduct.id}`, newProduct, {
          headers: {
            Authorization: `Bearer ${token}`, // Include token in the request header
          },
          withCredentials: true,
        });
        message.success('Product updated successfully');
      } else {
        // Create new product
        await axios.post(`http://172.17.0.99:8080/api/v1/user/add`, newProduct, {
          headers: {
            Authorization: `Bearer ${token}`, // Include token in the request header
          },
          withCredentials: true,
        });
        message.success('Product added successfully');
      }
  
      fetchProducts(); // Refresh product list
      setIsModalVisible(false);
      setNewProduct({ name: '', rentalPrice: '', category: '', available: true });
      setEditMode(false);
    } catch (error) {
      console.error(error);
      message.error('Error saving product');
    }
  };
  

  // Filter products based on search text, availability, and category
  const filteredProducts = products.filter(product => {
    return (
      (searchText === '' || product.name.toLowerCase().includes(searchText.toLowerCase()) || product.category.toLowerCase().includes(searchText.toLowerCase())) &&
      (availableFilter === null || product.available === availableFilter) &&
      (categoryFilter === '' || product.category.toLowerCase() === categoryFilter.toLowerCase())
    );
  });

  // Handle clearing filters
  const handleClearFilters = () => {
    setAvailableFilter(null);
    setCategoryFilter('');
    setSearchText('');
  };

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Price',
      dataIndex: 'rentalPrice',
      key: 'rentalPrice',
    },
    {
      title: 'Category',
      dataIndex: 'category',
      key: 'category',
    },
    {
      title: 'Availability',
      dataIndex: 'available',
      key: 'available',
      render: (available) => (
        <span>{available ? 'Available' : 'Unavailable'}</span>
      ),
    },
    {
      title: 'Action',
      key: 'action',
      render: (_, record) => (
        <span>
          <Button type="link" onClick={() => showModal(record)}>Edit</Button>
          <Button
            type="link"
            icon={<DeleteOutlined />}
            onClick={() => handleDelete(record.id)}
          />
        </span>
      ),
    },
  ];

  return (
    <div>
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">Products</h1>
        <Button type="primary" onClick={() => showModal()} style={{ marginBottom: '10px' }}>Add Product</Button>
      </div>

      <div style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
        <Input
          placeholder="Search products by name or category"
          prefix={<SearchOutlined />}
          value={searchText}
          onChange={e => setSearchText(e.target.value)}
          style={{ marginBottom: '8px', width: '300px' }}
        />
        <Select
          placeholder="Filter by Availability"
          style={{ width: 180 }}
          onChange={(value) => setAvailableFilter(value)}
          value={availableFilter}
        >
          <Select.Option value={true}>Available</Select.Option>
          <Select.Option value={false}>Unavailable</Select.Option>
        </Select>
        <Select
          placeholder="Filter by Category"
          style={{ width: 180 }}
          onChange={(value) => setCategoryFilter(value)}
          value={categoryFilter || undefined}
        >
          {[...new Set(products.map(product => product.category))].map(category => (
            <Select.Option key={category} value={category}>{category}</Select.Option>
          ))}
        </Select>
        <Button onClick={handleClearFilters} type="link" style={{ marginTop: '5px' }}>Clear Filters</Button>
      </div>

      <Table
        columns={columns}
        dataSource={filteredProducts}
        pagination={{ pageSize: 5 }}
        style={{ marginTop: '20px' }}
        rowKey="id"
      />

      <Modal
        title={editMode ? 'Edit Product' : 'Add Product'}
        open={isModalVisible}
        onOk={handleOk}
        onCancel={handleCancel}
      >
        <Input
          placeholder="Product Name"
          value={newProduct.name}
          onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })}
          style={{ marginBottom: '10px' }}
        />
        <Input
          placeholder="Product Price"
          value={newProduct.rentalPrice}
          onChange={(e) => setNewProduct({ ...newProduct, rentalPrice: e.target.value })}
          style={{ marginBottom: '10px' }}
        />
        <Input
          placeholder="Product Category"
          value={newProduct.category}
          onChange={(e) => setNewProduct({ ...newProduct, category: e.target.value })}
          style={{ marginBottom: '10px' }}
        />
        <div style={{ marginBottom: '10px' }}>
          <span>Available:</span>
          <Switch
            checked={newProduct.available}
            onChange={(checked) => setNewProduct({ ...newProduct, available: checked })}
          />
        </div>
      </Modal>
    </div>
  );
};

export default Products;
