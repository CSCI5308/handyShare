import React, { useState, useEffect } from 'react';
import { Form, Input, Button, Upload, message } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import axios from 'axios';
import { SERVER_URL } from '../../constants';

const EditLendForm = ({ item, onUpdate, onCancel }) => {
  const [form] = Form.useForm();
  const [formData, setFormData] = useState({
    id: item.id || null,
    name: item.name || '',
    description: item.description || '',
    price: item.price || 0,
    category: item.category || '',
    city: item.city || '',
    state: item.state || '',
    pincode: item.pincode || '',
    address: item.address || '',
    image: null,
    imageName: item.imageName || ''
  });

  useEffect(() => {
    setFormData({
      id: item.id || null,
      name: item.name || '',
      description: item.description || '',
      price: item.price || 0,
      category: item.category || '',
      city: item.city || '',
      state: item.state || '',
      pincode: item.pincode || '',
      address: item.address || '',
      image: null,
      imageName: item.imageName || ''
    });
    form.setFieldsValue({
      name: item.name,
      description: item.description,
      price: item.price,
      category: item.category,
      city: item.city,
      state: item.state,
      pincode: item.pincode,
      address: item.address
    });
  }, [item, form]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'price' ? (value ? parseFloat(value) : 0) : value
    }));
  };

  const handleImageChange = ({ file }) => {
    setFormData(prev => ({
      ...prev,
      image: file.originFileObj
    }));
  };

  const handleUpdateSubmit = async () => {
    try {
      await form.validateFields();
      const payload = new FormData();
      Object.keys(formData).forEach(key => {
        if (key === 'image' && formData.image) {
          payload.append('image', formData.image);
        } else {
          payload.append(key, formData[key]);
        }
      });

      console.log('Submitting updated item:', formData); // Debugging log
      onUpdate(payload);
    } catch (error) {
      console.error('Validation Failed:', error);
      message.error('Please correct the errors in the form.');
    }
  };

  return (
    <Form form={form} layout="vertical">
      <Form.Item label="Name" name="name" rules={[{ required: true }]}>
        <Input name="name" onChange={handleChange} />
      </Form.Item>
      <Form.Item label="Description" name="description" rules={[{ required: true }]}>
        <Input name="description" onChange={handleChange} />
      </Form.Item>
      <Form.Item
        label="Price"
        name="price"
        rules={[{ required: true, type: 'number', message: 'Please enter a valid price' }]}
      >
        <Input
          type="number"
          name="price"
          value={formData.price}
          onChange={handleChange}
        />
      </Form.Item>
      {/* Add other form fields similarly */}
      <Form.Item label="Image">
        <Upload beforeUpload={() => false} onChange={handleImageChange}>
          <Button icon={<UploadOutlined />}>Upload Image</Button>
        </Upload>
      </Form.Item>
      <Form.Item>
        <Button type="primary" onClick={handleUpdateSubmit}>
          Update
        </Button>
        <Button onClick={onCancel} style={{ marginLeft: '8px' }}>
          Cancel
        </Button>
      </Form.Item>
    </Form>
  );
};

export default EditLendForm;
