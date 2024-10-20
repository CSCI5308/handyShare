import React, { useState } from 'react';
import { Form, Input, Button, Rate, message, Upload } from 'antd';
import { UploadOutlined } from '@ant-design/icons';

const FeedbackForm = () => {
  const [loading, setLoading] = useState(false);
  const [rating, setRating] = useState(0);
  const [fileList, setFileList] = useState([]);

  const onFinish = (values) => {
    setLoading(true);
    // Simulate feedback submission process
    setTimeout(() => {
      setLoading(false);
      message.success('Feedback submitted successfully!');
      console.log('Submitted Values:', values, 'Files:', fileList);
    }, 2000);
  };

  const handleChange = (info) => {
    if (info.fileList.length > 1) {
      // Limit the number of files to 1
      setFileList(info.fileList.slice(-1));
    } else {
      setFileList(info.fileList);
    }
  };

  const getBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = (error) => reject(error);
    });
  };

  const handlePreview = async (file) => {
    if (!file.url && !file.preview) {
      file.preview = await getBase64(file.originFileObj);
    }
    const imgWindow = window.open(file.url || file.preview);
    imgWindow.document.write(`
        <html>
          <head>
            <title>Image Preview</title>
          </head>
          <body style="margin: 0; display: flex; justify-content: center; align-items: center; height: 100vh;">
            <img src="${file.url || file.preview}" style="max-width: 100%; max-height: 100%;" />
          </body>
        </html>
      `); 
  };

  return (
    <div>
      <h2 className="text-center text-2xl font-bold mb-4">Provide Feedback</h2>

      <Form layout="vertical" onFinish={onFinish} style={{ marginTop: '20px' }}>
        {/* Rating */}
        <Form.Item
          label="Rating"
          name="rating"
          rules={[{ required: true, message: 'Please provide a rating' }]}
        >
          <Rate onChange={setRating} value={rating} />
        </Form.Item>

        {/* Feedback */}
        <Form.Item
          label="Feedback"
          name="feedback"
          rules={[{ required: true, message: 'Please provide your feedback' }]}
        >
          <Input.TextArea rows={4} placeholder="Share your experience with this product..." />
        </Form.Item>

        {/* Upload Image */}
        <Form.Item
          label="Upload Image"
          name="image"
          valuePropName="fileList"
          getValueFromEvent={({ fileList }) => fileList}
          rules={[{ required: false, message: 'Please upload an image if available' }]}
        >
          <Upload
            listType="picture"
            beforeUpload={() => false} // Prevent automatic upload
            onChange={handleChange}
            fileList={fileList}
            onPreview={handlePreview} // Handle preview
            maxCount={1} // Limit to one file
          >
            <Button icon={<UploadOutlined />}>Upload Image</Button>
          </Upload>
        </Form.Item>

        {/* Submit Button */}
        <Button type="primary" htmlType="submit" loading={loading} style={{ width: '100%' }}>
          Submit Feedback
        </Button>
      </Form>
    </div>
  );
};

export default FeedbackForm;
