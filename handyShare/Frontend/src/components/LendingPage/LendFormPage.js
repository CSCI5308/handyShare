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

        // Filter out any null or undefined categories and ensure uniqueness
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
        </>
      ),
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
      ),
    },
    {
      title: 'Image Upload',
      content: (
        <>
          <Form.Item
            label="Upload Image"
            name="image"
            rules={[{ required: true, message: 'Please upload an image' }]}
          >
            <Upload
              beforeUpload={(file) => {
                setFormData({ ...formData, image: file, imageName: file.name });
                setFileList([file]);
                return false;
              }}
              fileList={fileList}
              onRemove={() => {
                setFormData({ ...formData, image: null, imageName: '' });
                setFileList([]);
              }}
              listType="picture"
              maxCount={1}
            >
              <Button icon={<UploadOutlined />}>Click to Upload</Button>
            </Upload>
          </Form.Item>

          {formData.image && (
            <div style={{ marginBottom: '16px' }}>
              <strong>Image Preview:</strong>
              <br />
              <img 
                src={URL.createObjectURL(formData.image)} 
                alt={formData.imageName} 
                style={{ maxWidth: '200px', marginTop: '10px' }} 
              />
            </div>
          )}
        </>
      ),
    },
    {
      title: 'Product Summary',
      content: (
        <Card title="Summary" bordered={false}>
          <p><strong>Name:</strong> {formData.name}</p>
          <p><strong>Description:</strong> {formData.description}</p>
          <p><strong>Price:</strong> ${formData.price}</p>
          <p><strong>Category:</strong> {formData.category}</p>
          <p><strong>City:</strong> {formData.city}</p>
          <p><strong>State:</strong> {formData.state}</p>
          <p><strong>Pincode:</strong> {formData.pincode}</p>
          <p><strong>Address:</strong> {formData.address}</p>
          <p><strong>Availability:</strong> Available</p>
        </Card>
      ),
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
      formToSend.append('availability', 'Available'); // Set fixed availability
      formToSend.append('image', formData.image);

      const token = localStorage.getItem('token');
      const response = await axios.post(`${SERVER_URL}/api/v1/user/lending/item`, formToSend, {
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
        imageName: ''
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

  return (
    <div>
      <Steps current={currentStep}>
        {steps.map(item => (
          <Step key={item.title} title={item.title} />
        ))}
      </Steps>

      <Form
        form={form}
        layout="vertical"
        style={{ marginTop: '20px' }}
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
