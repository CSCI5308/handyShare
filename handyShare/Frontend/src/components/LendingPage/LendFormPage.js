import React, { useState, useEffect } from 'react';
import { Steps, Button, Form, Input, Select, Upload, message, InputNumber } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Step } = Steps;
const { Option } = Select;

const LendFormPage = ({ item, onUpdate, onCancel, isEditing = false, onProductAdded }) => {
  const [currentStep, setCurrentStep] = useState(isEditing ? 1 : 0);
  const [form] = Form.useForm();
  const [categories, setCategories] = useState([]);

  // Fetch categories from API
  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get("http://localhost:8080/api/v1/user/allCategories", {
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

  // Set initial form values when editing
  useEffect(() => {
    if (isEditing && item) {
      form.setFieldsValue({
        category: item.category,
        name: item.name,
        description: item.description,
        price: item.price,
        address: item.address,
        city: item.city,
        state: item.state,
        pincode: item.pincode,
        // image: item.imageName, // Handle image upload differently if needed
      });
    } else {
      form.resetFields();
    }
  }, [isEditing, item, form]);

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
            >
              {categories.map(category => (
                <Option key={category} value={category}>{category}</Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item
            label="Item Name"
            name="name"
            rules={[{ required: true, message: 'Please enter item name' }]}
          >
            <Input
              placeholder="Enter item name"
            />
          </Form.Item>
          <Form.Item
            label="Description"
            name="description"
            rules={[{ required: true, message: 'Please enter description' }]}
          >
            <Input.TextArea
              placeholder="Enter description"
            />
          </Form.Item>
          <Form.Item
            label="Price (per hour)"
            name="price"
            rules={[
              { required: true, message: 'Please enter price' },
              { type: 'number', min: 0, message: 'Price must be a positive number' }
            ]}
          >
            <InputNumber
              min={0}
              step={0.01}
              placeholder="Enter price per hour"
              style={{ width: '100%' }}
            />
          </Form.Item>
        </>
      ),
    },
    {
      title: 'Location Details',
      content: (
        <>
          <Form.Item
            label="Address"
            name="address"
            rules={[{ required: true, message: 'Please enter address' }]}
          >
            <Input
              placeholder="Enter address"
            />
          </Form.Item>
          <Form.Item
            label="City"
            name="city"
            rules={[{ required: true, message: 'Please enter city' }]}
          >
            <Input
              placeholder="Enter city"
            />
          </Form.Item>
          <Form.Item
            label="State"
            name="state"
            rules={[{ required: true, message: 'Please enter state' }]}
          >
            <Input
              placeholder="Enter state"
            />
          </Form.Item>
          <Form.Item
            label="Pincode"
            name="pincode"
            rules={[{ required: true, message: 'Please enter pincode' }]}
          >
            <Input
              placeholder="Enter pincode"
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
            rules={[{ required: !isEditing, message: 'Please upload an image' }]}
          >
            <Upload
              beforeUpload={() => false} // Prevent automatic upload
              listType="picture"
              maxCount={1}
              accept="image/*"
            >
              <Button icon={<UploadOutlined />}>Click to Upload</Button>
            </Upload>
          </Form.Item>
          {isEditing && item.imageName && (
            <div style={{ marginBottom: '20px' }}>
              <img src={item.imageName} alt="Current" style={{ width: '100%', maxHeight: '200px', objectFit: 'cover' }} />
              <p>Current Image</p>
            </div>
          )}
        </>
      ),
    },
    {
      title: 'Summary',
      content: (
        <>
          <p><strong>Category:</strong> {form.getFieldValue('category')}</p>
          <p><strong>Name:</strong> {form.getFieldValue('name')}</p>
          <p><strong>Description:</strong> {form.getFieldValue('description')}</p>
          <p><strong>Price:</strong> ${form.getFieldValue('price')?.toFixed(2)}</p>
          <p><strong>Address:</strong> {form.getFieldValue('address')}</p>
          <p><strong>City:</strong> {form.getFieldValue('city')}</p>
          <p><strong>State:</strong> {form.getFieldValue('state')}</p>
          <p><strong>Pincode:</strong> {form.getFieldValue('pincode')}</p>
          {form.getFieldValue('image') && (
            <div>
              <strong>Uploaded Image:</strong>
              <img src={URL.createObjectURL(form.getFieldValue('image')[0]?.originFileObj)} alt="Uploaded" style={{ width: '100%', maxHeight: '200px', objectFit: 'cover' }} />
            </div>
          )}
        </>
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
      const values = await form.validateFields();
      
      // Prepare the data to send to the backend
      const formDataToSend = { ...values };
      
      // Handle image upload if a new image is uploaded
      if (values.image && values.image.length > 0) {
        const imageFile = values.image[0].originFileObj;
        // Upload the image to your backend or cloud storage and get the URL
        // For simplicity, let's assume the backend handles image upload via a separate endpoint
        // You might need to adjust this based on your backend implementation

        // Example: Upload image and get URL
        const uploadData = new FormData();
        uploadData.append('image', imageFile);
        
        const token = localStorage.getItem('token');
        const imageUploadResponse = await axios.post('http://localhost:8080/api/v1/user/uploadImage', uploadData, {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${token}`
          }
        });
        
        formDataToSend.imageName = imageUploadResponse.data.imageUrl; // Adjust based on your response
      }

      if (isEditing) {
        // Include the item ID
        formDataToSend.id = item.id;
        await onUpdate(formDataToSend);
        message.success('Item updated successfully');
      } else {
        await onProductAdded(formDataToSend);
        message.success('Item added successfully');
      }
      
    } catch (error) {
      console.error('Error submitting form:', error);
      message.error('Failed to submit form');
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
        <Form form={form} layout="vertical">
          {steps[currentStep].content}
        </Form>
      </div>
      <div className="steps-action mt-4">
        {currentStep < steps.length - 1 && (
          <Button type="primary" onClick={next}>
            Next
          </Button>
        )}
        {currentStep > 0 && (
          <Button style={{ margin: '0 8px' }} onClick={prev}>
            Back
          </Button>
        )}
        {currentStep === steps.length - 1 && (
          <Button type="primary" onClick={handleSubmit}>
            {isEditing ? 'Update' : 'Submit'}
          </Button>
        )}
        <Button style={{ marginLeft: '8px' }} onClick={onCancel}>
          Cancel
        </Button>
      </div>
    </div>
  );
};

export default LendFormPage;
