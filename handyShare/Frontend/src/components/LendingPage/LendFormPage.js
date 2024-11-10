import React, { useState, useEffect } from 'react';
import { Form, Input, Button, Upload, message, Steps, Select, Card, InputNumber } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import axios from 'axios';
import { SERVER_URL } from '../../constants.js';

const { Step } = Steps;
const { Option } = Select;

const LendFormPage = ({ onUpdate }) => {
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
    imageName: '',
    availability: 'Available'
  });
  const [categories, setCategories] = useState([]);
  const [fileList, setFileList] = useState([]);

  // Define handleUpload before using it in steps
  const handleUpload = (info) => {
    const { file } = info;
    
    if (file) {
      setFormData(prev => ({
        ...prev,
        image: file.originFileObj || file
      }));
      setFileList([file]);
    }
  };

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

        const uniqueCategories = response.data
          .filter(category => category)
          .map(category => category.name)
          .filter((value, index, self) => self.indexOf(value) === index);

        setCategories(uniqueCategories);
      } catch (error) {
        console.error('Error fetching categories:', error);
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
          <Form.Item
            label="Category"
            name="category"
            rules={[{ required: true, message: 'Please select a category' }]}
          >
            <Select 
              placeholder="Select a category"
              value={formData.category} 
              onChange={(value) => setFormData({ ...formData, category: value })}
            >
              {categories.map((cat) => (
                <Option key={cat} value={cat}>{cat}</Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item
            label="Name"
            name="name"
            rules={[{ required: true, message: 'Please enter the name' }]}
          >
            <Input 
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            />
          </Form.Item>

          <Form.Item
            label="Description"
            name="description"
            rules={[{ required: true, message: 'Please enter the description' }]}
          >
            <Input.TextArea 
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            />
          </Form.Item>

          <Form.Item
            label="Price"
            name="price"
            rules={[
              { required: true, message: 'Please enter the price' },
              { type: 'number', min: 0.01, message: 'Price must be greater than 0' }
            ]}
          >
            <InputNumber 
              min={0.01} 
              step={0.01}
              precision={2}
              value={formData.price} 
              onChange={(value) => setFormData({ ...formData, price: value })}
              style={{ width: '100%' }}
              formatter={value => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
              parser={value => value.replace(/\$\s?|(,*)/g, '')}
            />
          </Form.Item>

          <Form.Item
            label="Image"
            name="image"
            rules={[{ required: true, message: 'Please upload an image' }]}
            valuePropName="fileList"
            getValueFromEvent={(e) => {
              if (Array.isArray(e)) {
                return e;
              }
              return e?.fileList;
            }}
          >
            <Upload
              beforeUpload={() => false}
              onChange={handleUpload}
              accept="image/*"
              listType="picture"
              maxCount={1}
              fileList={fileList}
            >
              <Button icon={<UploadOutlined />}>Click to Upload</Button>
            </Upload>
          </Form.Item>
        </>
      )
    },
    {
      title: 'Location & Address',
      content: (
        <>
          <Form.Item
            label="City"
            name="city"
            rules={[{ required: true, message: 'Please enter the city' }]}
          >
            <Input 
              value={formData.city} 
              onChange={(e) => setFormData({ ...formData, city: e.target.value })}
            />
          </Form.Item>

          <Form.Item
            label="State"
            name="state"
            rules={[{ required: true, message: 'Please enter the state' }]}
          >
            <Input 
              value={formData.state} 
              onChange={(e) => setFormData({ ...formData, state: e.target.value })}
            />
          </Form.Item>

          <Form.Item
            label="Pincode"
            name="pincode"
            rules={[{ required: true, message: 'Please enter the pincode' }]}
          >
            <Input 
              value={formData.pincode} 
              onChange={(e) => setFormData({ ...formData, pincode: e.target.value })}
            />
          </Form.Item>

          <Form.Item
            label="Address"
            name="address"
            rules={[{ required: true, message: 'Please enter the address' }]}
          >
            <Input 
              value={formData.address} 
              onChange={(e) => setFormData({ ...formData, address: e.target.value })}
            />
          </Form.Item>
        </>
      )
    },
    {
      title: 'Summary',
      content: (
        <Card title="Item Summary">
          <p><strong>Category:</strong> {formData.category}</p>
          <p><strong>Name:</strong> {formData.name}</p>
          <p><strong>Description:</strong> {formData.description}</p>
          <p><strong>Price:</strong> ${formData.price?.toFixed(2)}</p>
          <p><strong>Location:</strong> {formData.address}, {formData.city}, {formData.state} - {formData.pincode}</p>
          {fileList.length > 0 && formData.image && (
            <div style={{ marginTop: '16px' }}>
              <p><strong>Image Preview:</strong></p>
              <img 
                src={URL.createObjectURL(formData.image)} 
                alt="Item preview" 
                style={{ maxWidth: '200px', maxHeight: '200px', objectFit: 'contain' }}
                onLoad={(e) => {
                  URL.revokeObjectURL(e.target.src);
                }}
              />
            </div>
          )}
        </Card>
      )
    }
  ];

  const next = () => {
    form.validateFields().then(() => {
      setCurrentStep(currentStep + 1);
    }).catch(info => {
      console.log('Validate Failed:', info);
    });
  };

  const prev = () => {
    setCurrentStep(currentStep - 1);
  };

  const handleSubmit = async () => {
    try {
      // Validate all form fields first
      await form.validateFields();
      
      // Check if price is valid
      if (!formData.price || formData.price <= 0) {
        message.error('Price must be greater than 0');
        return;
      }

      // Check if image is present
      if (!formData.image) {
        message.error('Please upload an image');
        return;
      }

      const formToSend = new FormData();
      formToSend.append('category', formData.category);
      formToSend.append('name', formData.name);
      formToSend.append('description', formData.description);
      formToSend.append('price', formData.price.toFixed(2)); // Format price to 2 decimal places
      formToSend.append('city', formData.city);
      formToSend.append('state', formData.state);
      formToSend.append('pincode', formData.pincode);
      formToSend.append('address', formData.address);
      formToSend.append('availability', formData.availability); // Ensure availability is sent
      formToSend.append('image', formData.image);

      const token = localStorage.getItem('token');
      
      // Optionally, include user ID if required by the backend
      // Note: Backend should infer user from the token

      await axios.post(`${SERVER_URL}/api/v1/user/lending/item`, formToSend, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${token}`
        },
        withCredentials: true
      });

      message.success('Product added successfully!');
      onUpdate(); // Refresh the lent items list
      form.resetFields();
      setCurrentStep(0);
      setFormData({
        name: '',
        description: '',
        price: 0,
        category: '',
        city: '',
        state: '',
        pincode: '',
        address: '',
        image: null,
        imageName: '',
        availability: 'Available'
      });
      setFileList([]);
    } catch (error) {
      console.error('Error adding product:', error.response || error);
      if (error.response && error.response.data) {
        message.error(typeof error.response.data === 'string' ? error.response.data : 'Failed to add product');
      } else {
        message.error('Failed to add product');
      }
    }
  };

  useEffect(() => {
    return () => {
      // Cleanup is now handled by onLoad event of the image
    };
  }, [fileList]);

  return (
    <div>
      <Steps current={currentStep} style={{ marginBottom: '20px' }}>
        {steps.map(item => (
          <Step key={item.title} title={item.title} />
        ))}
      </Steps>
      <Form
        form={form}
        layout="vertical"
      >
        {steps[currentStep].content}
      </Form>
      <div style={{ marginTop: 24 }}>
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
        <Button style={{ marginLeft: '8px' }} onClick={() => { 
          form.resetFields(); 
          setCurrentStep(0); 
          setFormData({
            name: '',
            description: '',
            price: 0,
            category: '',
            city: '',
            state: '',
            pincode: '',
            address: '',
            image: null,
            imageName: '',
            availability: 'Available'
          }); 
          setFileList([]);
        }}>
          Reset
        </Button>
      </div>
    </div>
  );
};

export default LendFormPage;
