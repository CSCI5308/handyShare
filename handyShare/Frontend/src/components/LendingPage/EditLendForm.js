import React, { useState, useEffect } from 'react';
import { Form, Input, Button, Upload, message, Select, Steps, InputNumber, Card } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import axios from 'axios';
import { SERVER_URL } from '../../constants.js';

const { Option } = Select;
const { Step } = Steps;

const EditLendForm = ({ item, onUpdate, onCancel }) => {
  const [form] = Form.useForm();
  const [currentStep, setCurrentStep] = useState(0);
  const [categories, setCategories] = useState([]);
  const [formData, setFormData] = useState({
    name: item.name || '',
    description: item.description || '',
    price: item.price || 0,
    category: item.category || '',
    city: item.city || '',
    state: item.state || '',
    pincode: item.pincode || '',
    address: item.address || '',
    image: null,
    imageName: item.imageName || '',
    availability: item.availability || 'Available',
  });

  const [fileList, setFileList] = useState([]);

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

  useEffect(() => {
    form.setFieldsValue({
      name: item.name,
      description: item.description,
      price: item.price,
      category: item.category,
      city: item.city,
      state: item.state,
      pincode: item.pincode,
      address: item.address,
      availability: item.availability
    });
  }, [form, item]);

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
            rules={[{ required: true, message: 'Please enter the price' }]}
          >
            <InputNumber 
              min={0} 
              value={formData.price} 
              onChange={(value) => setFormData({ ...formData, price: value })}
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
      title: 'Image & Availability',
      content: (
        <>
          <Form.Item
            label="Upload Image"
            name="image"
            rules={[{ required: false }]} // Image is optional in edit
          >
            <Upload
              beforeUpload={(file) => {
                setFormData({ ...formData, image: file, imageName: file.name });
                setFileList([file]);
                return false; // Prevent automatic upload
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

          <Form.Item 
            label="Availability" 
            name="availability" 
            rules={[{ required: true, message: 'Please select availability' }]}
          >
            <Select 
              placeholder="Select availability"
              value={formData.availability} 
              onChange={(value) => setFormData({ ...formData, availability: value })}
            >
              <Option key="Available" value="Available">Available</Option>
              <Option key="Unavailable" value="Unavailable">Unavailable</Option>
            </Select>
          </Form.Item>
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
          <p><strong>Availability:</strong> {formData.availability}</p>
          {formData.image && (
            <div>
              <strong>Image:</strong>
              <br />
              <img src={URL.createObjectURL(formData.image)} alt={formData.imageName} style={{ maxWidth: '200px', marginTop: '10px' }} />
            </div>
          )}
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
    // Prepare form data
    const formToSend = new FormData();
    formToSend.append('name', formData.name);
    formToSend.append('description', formData.description);
    formToSend.append('price', formData.price);
    formToSend.append('category', formData.category); // Now category is a string
    formToSend.append('city', formData.city);
    formToSend.append('state', formData.state);
    formToSend.append('pincode', formData.pincode);
    formToSend.append('address', formData.address);
    formToSend.append('availability', formData.availability);
    if (formData.image) {
      formToSend.append('image', formData.image);
    }

    try {
      const token = localStorage.getItem('token');
      await axios.put(`${SERVER_URL}/api/v1/user/lending/item/${item.id}`, formToSend, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${token}`
        },
        withCredentials: true
      });

      message.success('Product updated successfully!');
      onUpdate(); // Refresh the lent items list
      onCancel(); // Close the modal
    } catch (error) {
      console.error('Error updating product:', error.response || error);
      if (error.response && error.response.data) {
        if (typeof error.response.data === 'object') {
          Object.values(error.response.data).forEach(errMsg => {
            message.error(errMsg);
          });
        } else {
          message.error(error.response.data);
        }
      } else {
        message.error('Failed to update product');
      }
    }
  };

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

export default EditLendForm;
