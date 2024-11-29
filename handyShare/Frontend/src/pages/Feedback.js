import React, { useEffect, useState } from "react";
import axios from "axios";
import { Layout, message } from 'antd';
import HeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar.js'; 
import { useNavigate } from 'react-router-dom';

const { Header, Content, Footer } = Layout;

const FeedbackPage = ({ productId, userId }) => {
  const [feedbacks, setFeedbacks] = useState([]);
  const [reviewText, setReviewText] = useState("");
  const [rating, setRating] = useState(0);
  const [image, setImage] = useState(null);
  const [hoverRating, setHoverRating] = useState(0);
  const navigate = useNavigate();

  const handleStarClick = (starRating) => {
    setRating(starRating);  
  };

  const handleStarHover = (starRating) => {
    setHoverRating(starRating);  
  };

  const handleStarLeave = () => {
    setHoverRating(0);  
  };

  // Submit new feedback and refresh the feedback list
  const handleSubmitFeedback = async (event) => {
    event.preventDefault();

    if (!reviewText || !rating) {
      alert("Review text and rating are required.");
      return;
    }

    try {
      const token = localStorage.getItem('token');
      const userId = localStorage.getItem('userId');
      const productId = localStorage.getItem('productId');

      const reviewData = {
        productId: productId,
        userId: userId,
        reviewText: reviewText,
        rating: rating,
        image: image,
      };

      const formData = new FormData();
      formData.append("productId", productId);
      formData.append("userId", userId);
      formData.append("reviewText", reviewText);
      formData.append("rating", rating);
      if (image) formData.append("image", image);

      const response = await axios.post('http://localhost:8080/api/v1/user/review-create', formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'multipart/form-data',
        },
        withCredentials: true,
      });

      const { data } = response;
      if (response.status === 201) {
        message.success('Review submitted successfully!');
        setFeedbacks([...feedbacks, data.review]);
        setReviewText("");  // Reset review form
        setRating(0);       // Reset rating
        setImage(null);      // Reset image
        navigate('/borrow');
      } else {
        message.error('Failed to submit review. Please try again.');
      }
    } catch (error) {
      console.error("Error submitting feedback:", error);
      message.error('Error submitting feedback. Please try again.');
    }
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      {/* Header */}
      <Header
        style={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          zIndex: 1000,
          background: '#3B7BF8',
        }}
      >
        <HeaderBar />
      </Header>

      {/* Main content */}
      <Content style={{ padding: '100px 50px', marginTop: '64px' }}>
        <div
          style={{
            maxWidth: '800px',
            margin: '0 auto',
            background: '#fff',
            padding: '24px',
            borderRadius: '8px',
            boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)',
          }}
        >
          <h1 className="text-2xl font-bold mb-6">Product Feedback</h1>

          {/* Feedback Form */}
          <form onSubmit={handleSubmitFeedback}>
            <textarea
              value={reviewText}
              onChange={(e) => setReviewText(e.target.value)}
              placeholder="Write your review"
              className="w-full p-2 border border-gray-300 rounded-md"
              rows="4"
            />
            <div className="mb-4">
              <label className="block text-sm font-medium mb-2">Rating:</label>
              <div className="flex space-x-2">
                {[1, 2, 3, 4, 5].map((star) => (
                  <span
                    key={star}
                    className={`cursor-pointer text-2xl 
                  ${star <= (hoverRating || rating) ? 'text-yellow-500' : 'text-gray-400'} 
                  ${star <= hoverRating ? 'text-yellow-300' : ''} 
                `}
                    onClick={() => handleStarClick(star)}
                    onMouseEnter={() => handleStarHover(star)}
                    onMouseLeave={handleStarLeave}
                  >
                    ★
                  </span>
                ))}
              </div>
            </div>
            <div className="mt-4">
              <label>Upload Image (optional): </label>
              <input
                type="file"
                onChange={(e) => setImage(e.target.files[0])}
                className="p-2 border border-gray-300 rounded-md"
              />
            </div>
            <button
              type="submit"
              className="mt-6 px-4 py-2 bg-blue-600 text-white rounded-md"
              onClick={handleSubmitFeedback}
            >
              Submit Review
            </button>
          </form>
        </div>
      </Content>

      {/* Footer */}
      <Footer style={{ textAlign: 'center' }}>
        HandyShare ©{new Date().getFullYear()} Created by Group G02
      </Footer>
    </Layout>
  );
};

export default FeedbackPage;
