import React, { useState, useEffect } from 'react';
import { Form, Input, Button, Upload, message, Steps, Select } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import axios from 'axios';
import { SERVER_URL } from '../../config';

const { Step } = Steps;
const { Option } = Select;

const LendFormPage = ({ onProductAdded }) => {
  const [currentStep, setCurrentStep] = useState(0);
  const [form] = Form.useForm();
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: 0,
    category: '',
    city: '',
    state: '',
    pincode: '',
    address: '',
    image: null,
    imageName: ''
  });
  const [categories, setCategories] = useState([]);

  // Fetch categories from API
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(`${SERVER_URL}/api/v1/user/allCategories`, {
          headers: {
            Authorization: `Bearer ${token}`
          },
          withCredentials: true
        });

        setCategories(response.data.map(cat => cat.name));
      } catch (error) {
        console.error('Error fetching categories:', error.response || error);
        message.error('Failed to fetch categories');
      }
    };
    fetchCategories();
  }, []);

  const steps = [
    {
      title: 'Item Description',
      content: (
        <>
          <Form.Item label="Category" name="category" rules={[{ required: true, message: 'Please select a category' }]}>
            <Select 
              value={formData.category} 
              onChange={(value) => setFormData({ ...formData, category: value })}
            >
              {categories.map(category => (
                <Option key={category} value={category}>{category}</Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item label="Item Name" name="name" rules={[{ required: true, message: 'Please enter item name' }]}>
            <Input 
              value={formData.name} 
              onChange={(e) => setFormData({ ...formData, name: e.target.value })} 
            />
          </Form.Item>
          <Form.Item label="Description" name="description">
            <Input.TextArea 
              value={formData.description} 
              onChange={(e) => setFormData({ ...formData, description: e.target.value })} 
            />
          </Form.Item>
          <Form.Item label="Rental Price" name="price" rules={[{ required: true, message: 'Please enter price' }]}>
            <Input 
              type="number" 
              value={formData.price} 
              onChange={(e) => {
                const value = e.target.value;
                setFormData({ 
                  ...formData, 
                  price: value ? parseFloat(value) : 0 
                });
              }} 
            />
          </Form.Item>
          <Form.Item
            label="Upload Image"
            name="image"
            valuePropName="file"
            rules={[{ required: false }]}
          >
            <Upload
              beforeUpload={(file) => {
                const isValidType = ['image/png', 'image/svg+xml', 'image/jpeg', 'image/jpg'].includes(file.type);
                if (!isValidType) {
                  message.error('You can only upload PNG, SVG, JPG, or JPEG files.');
                  return Upload.LIST_IGNORE;
                }
                setFormData({ ...formData, image: file, imageName: file.name });
                return false;
              }}
              onRemove={() => setFormData({ ...formData, image: null, imageName: '' })}
              fileList={formData.image ? [{ uid: '-1', name: formData.imageName }] : []}
            >
              <Button icon={<UploadOutlined />}>Click to Upload</Button>
            </Upload>
          </Form.Item>
        </>
      ),
    },
    {
      title: 'Location',
      content: (
        <>
          <Form.Item label="Address" name="address" rules={[{ required: true, message: 'Please enter address' }]}>
            <Input 
              value={formData.address} 
              onChange={(e) => setFormData({ ...formData, address: e.target.value })} 
            />
          </Form.Item>
          <Form.Item label="City" name="city" rules={[{ required: true, message: 'Please enter city' }]}>
            <Input 
              value={formData.city} 
              onChange={(e) => setFormData({ ...formData, city: e.target.value })} 
            />
          </Form.Item>
          <Form.Item label="State" name="state" rules={[{ required: true, message: 'Please enter state' }]}>
            <Input 
              value={formData.state} 
              onChange={(e) => setFormData({ ...formData, state: e.target.value })} 
            />
          </Form.Item>
          <Form.Item label="Pincode" name="pincode" rules={[{ required: true, message: 'Please enter pincode' }]}>
            <Input 
              value={formData.pincode} 
              onChange={(e) => setFormData({ ...formData, pincode: e.target.value })} 
            />
          </Form.Item>
        </>
      ),
    },
    {
      title: 'Summary',
      content: (
        <div className="summary-container" style={{ textAlign: 'center' }}>
          <div className="image-summary" style={{ display: 'flex', justifyContent: 'center', marginBottom: '20px' }}>
            {formData.image ? (
              <img
                src={URL.createObjectURL(formData.image)}
                alt="Uploaded item"
                style={{ width: '200px', height: '200px', objectFit: 'cover', marginBottom: '20px' }}
              />
            ) : null}
          </div>
          <div className="item-details" style={{ marginBottom: '20px' }}>
            <h2 style={{ fontWeight: 'bold', fontSize: '20px' }}>Item Details</h2>
            <p><strong>Name:</strong> {formData.name}</p>
            <p><strong>Category:</strong> {formData.category}</p>
            <p><strong>Price:</strong> ${Number(formData.price).toFixed(2)}</p>
            <p><strong>Description:</strong> {formData.description || 'No description provided'}</p>
          </div>

          <div className="location-details">
            <h2 style={{ fontWeight: 'bold', fontSize: '20px' }}>Location Details</h2>
            <p><strong>Address:</strong> {formData.address}</p>
            <p><strong>City:</strong> {formData.city}</p>
            <p><strong>State:</strong> {formData.state}</p>
            <p><strong>Pincode:</strong> {formData.pincode}</p>
          </div>
        </div>
      ),
    },
  ];

  const next = async () => {
    try {
      await form.validateFields();
      setCurrentStep(currentStep + 1);
    } catch (errorInfo) {
      // Validation failed, stay on the current step
    }
  };

  const prev = () => {
    setCurrentStep(currentStep - 1);
  };

  const handleSubmit = async () => {
    try {
      const token = localStorage.getItem('token');
      const payload = { ...formData };
      // Handle image upload here if necessary
      const response = await axios.post(`${SERVER_URL}/api/v1/all/lending/item`, payload, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        },
        withCredentials: true
      });
      message.success('Item added successfully');
      onProductAdded();
      form.resetFields();
      setFormData({
        name: '',
        description: '',
        price: '',
        category: '',
        city: '',
        state: '',
        pincode: '',
        address: '',
        image: null,
        imageName: ''
      });
      setCurrentStep(0);
    } catch (error) {
      console.error('Error adding item:', error.response || error);
      message.error('Failed to add item');
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-6 bg-white shadow-md rounded-lg mt-2">
      <Steps current={currentStep}>
        {steps.map(item => (
          <Step key={item.title} title={item.title} />
        ))}
      </Steps>
      <div className="steps-content mt-6">
        <Form form={form}>
          {steps[currentStep].content}
        </Form>
      </div>
      <div className="steps-action mt-4">
        {currentStep < steps.length - 1 && (
          <Button type="primary" onClick={() => next()}>
            Next
          </Button>
        )}
        {currentStep > 0 && (
          <Button style={{ margin: '0 8px' }} onClick={() => prev()}>
            Back
          </Button>
        )}
        {currentStep === steps.length - 1 && (
          <Button type="primary" onClick={handleSubmit}>
            Submit
          </Button>
        )}
        <Button style={{ marginLeft: '8px' }} onClick={() => { form.resetFields(); setCurrentStep(0); }}>
          Reset
        </Button>
      </div>
    </div>
  );
};

export default LendFormPage;
