import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Input, message, Switch, Tooltip } from 'antd';
import { DeleteOutlined, InfoCircleOutlined } from '@ant-design/icons';
import axios from 'axios';
// import { SERVER_URL_CATEGORIES } from '../../constants';

const Categories = () => {
  const [categories, setCategories] = useState([]);
  const [filteredCategories, setFilteredCategories] = useState([]); // For search functionality
  const [searchTerm, setSearchTerm] = useState(''); // Search term state
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [newCategory, setNewCategory] = useState({
    name: '',
    description: '',
    isActive: true,
    parentCategoryId: null,
  });
  const [editMode, setEditMode] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState(null);

  // Fetch categories from API
  const fetchCategories = async () => {
    try {
      // Fetch product details
      const token = localStorage.getItem('token');
      const response = await axios.get(`http://localhost:8080/api/v1/user/allCategories`,{
      
        headers: {
                   
          Authorization: `Bearer ${token}`
      },
      withCredentials: true

      
      });
      setCategories(response.data);
      setFilteredCategories(response.data); // Initialize filtered categories
    } catch (error) {
      console.error('Error fetching categories:', error);
      message.error('Failed to load categories');
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  // Open the modal for adding or editing a category
  const showModal = (category = null) => {
    if (category) {
      setEditMode(true);
      setSelectedCategory(category);
      setNewCategory({
        name: category.name,
        description: category.description,
        isActive: category.isActive,
        parentCategoryId: category.parentCategory?.categoryId || null,
      });
    } else {
      setEditMode(false);
      setNewCategory({ name: '', description: '', isActive: true, parentCategoryId: null });
    }
    setIsModalVisible(true);
  };

  // Close the modal
  const handleCancel = () => {
    setIsModalVisible(false);
    setNewCategory({ name: '', description: '', isActive: true, parentCategoryId: null });
    setEditMode(false);
  };

  // Save or update category
  const handleOk = async () => {
    // Front-end validation
    if (!newCategory.name.trim() || !newCategory.description.trim()) {
      message.error('Name and Description are required');
      return;
    }
  
    try {
      if (editMode) {
        // Update category
        await axios.put(`http://localhost:8080/api/v1/all/category/${selectedCategory.categoryId}`, newCategory);
        message.success('Category updated successfully');
      } else {
        // Create new category
        await axios.post(`http://localhost:8080/api/v1/all/create`, newCategory);
        message.success('Category added successfully');
      }
  
      fetchCategories(); // Refresh category list
      setIsModalVisible(false);
      setNewCategory({ name: '', description: '', isActive: true, parentCategoryId: null });
      setEditMode(false);
    } catch (error) {
      console.error(error);
      message.error('Error saving category');
    }
  };  

  // Delete category
  const handleDelete = async (categoryId) => {
    try {
      await axios.delete(`http://localhost:8080/api/v1/all/category/delete/${categoryId}`);
      message.success('Category deleted successfully');
      fetchCategories();
    } catch (error) {
      console.error(error);
      message.error('Error deleting category');
    }
  };

  // Search categories by name
  const handleSearch = (value) => {
    setSearchTerm(value);
    const filtered = categories.filter(category =>
      category.name.toLowerCase().includes(value.toLowerCase())
    );
    setFilteredCategories(filtered);
  };

  // Table columns definition
  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: 'Status',
      key: 'isActive',
      render: (category) => (
        <span>{category.isActive ? 'Active' : 'Inactive'}</span>
      ),
    },
    {
      title: 'Action',
      key: 'action',
      render: (category) => (
        <span>
          <Button type="link" onClick={() => showModal(category)}>Edit</Button>
          <Button
            type="link"
            icon={<DeleteOutlined />}
            onClick={() => handleDelete(category.categoryId)}
          />
        </span>
      ),
    },
  ];

  return (
    <>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '20px' }}>
        <h2 style={{ fontSize: '24px', fontWeight: 'bold' }}>Categories</h2>
        <Button type="primary" onClick={() => showModal()}>Add Category</Button>
      </div>

      {/* Search bar */}
      <Input.Search
        placeholder="Search categories by name"
        value={searchTerm}
        onChange={(e) => handleSearch(e.target.value)}
        onSearch={handleSearch}
        style={{ marginBottom: '20px', width: '300px' }}
      />

      {/* Display filtered categories */}
      <Table
        dataSource={filteredCategories}
        columns={columns}
        rowKey="categoryId"
        pagination={{ pageSize: 5 }}
      />

      <Modal
        title={editMode ? 'Edit Category' : 'Add Category'}
        open={isModalVisible}
        onOk={handleOk}
        onCancel={handleCancel}
      >
        <Input
          placeholder="Category Name"
          value={newCategory.name}
          onChange={(e) => setNewCategory({ ...newCategory, name: e.target.value })}
          style={{ marginBottom: '10px' }}
        />
        <Input
          placeholder="Category Description"
          value={newCategory.description}
          onChange={(e) => setNewCategory({ ...newCategory, description: e.target.value })}
          style={{ marginBottom: '10px' }}
        />
        <div style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
          <span style={{ marginRight: '10px' }}>Status: </span>
          <Switch
            checked={newCategory.isActive}
            onChange={(checked) => setNewCategory({ ...newCategory, isActive: checked })}
          />
          <Tooltip title="Status: Active or Inactive">
            <InfoCircleOutlined style={{ marginLeft: '10px' }} />
          </Tooltip>
        </div>
      </Modal>
    </>
  );
};

export default Categories;
