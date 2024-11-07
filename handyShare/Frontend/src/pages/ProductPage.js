import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import HeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar.js';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { message } from 'antd';

const ProductPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [lender, setLender] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedDate, setSelectedDate] = useState(null);
  const MAX_HOURS = 24; // Maximum hours selectable for rent

  useEffect(() => {
    const fetchProductDetails = async () => {
      if (!id || id === 'null') {
        setError('Invalid product ID.');
        setLoading(false);
        return;
      }

      try {
        const token = localStorage.getItem('token');
        const productResponse = await axios.get(`http://localhost:8080/api/v1/user/product/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true
        });
        setProduct(productResponse.data);

        // Fetch lender information if userId is available
        if (productResponse.data.userId) {
          const lenderResponse = await axios.get(`http://localhost:8080/api/v1/all/users/${productResponse.data.userId}`, {
            headers: { Authorization: `Bearer ${token}` },
            withCredentials: true
          });
          setLender(lenderResponse.data);
        } else {
          setLender(null);
        }
        setLoading(false);
      } catch (err) {
        setError('Error loading product details.');
        setLoading(false);
      }
    };
    fetchProductDetails();
  }, [id]);

  const handleRentNow = async () => {
    if (!selectedDate) {
      message.warning('Please select a date to proceed with the rental.');
      return;
    }
    navigate('/rent-summary', { 
      state: { 
        product, 
        hours: product.transactionTime || 2, 
        selectedDate 
      } 
    });
  };

  if (loading) return <div className="p-8 mt-16 animate-pulse text-blue-600">Loading...</div>;
  if (error) return <div className="p-8 mt-16 text-red-500 font-semibold">{error}</div>;
  if (!product) return <div className="p-8 mt-16">Product not found.</div>;

  return (
    <div className="min-h-screen flex flex-col bg-gray-50">
      <HeaderBar />
      <main className="flex-grow p-8 mt-16 grid gap-8 sm:grid-cols-1 lg:grid-cols-3">

        {/* Left Section: Product Image and Description */}
        <div className="bg-white p-6 rounded-lg shadow-md">
          {product.productImage ? (
            <img src={product.productImage} alt={product.name} className="w-full h-64 object-cover rounded-md mb-4 shadow-lg" />
          ) : (
            <div className="w-full h-64 bg-gray-300 rounded-md flex items-center justify-center shadow-md">
              <span className="text-gray-500">No Image Available</span>
            </div>
          )}
          <h3 className="heading-lg mb-2">Description</h3>
          <p className="text-gray-600">{product.description}</p>
          <h3 className="heading-lg mt-6">Reviews</h3>
          {reviews.length > 0 ? (
            reviews.map((review, index) => (
              <div key={index} className="mt-2 text-gray-600">
                <p><strong>{review.user}:</strong> {review.comment}</p>
              </div>
            ))
          ) : (
            <p>No reviews available.</p>
          )}
        </div>

        {/* Middle Section: Product Details */}
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h2 className="heading-lg text-gray-800">{product.name}</h2>
          <p className="text-lg text-gray-600 mb-4">Price: <span className="font-semibold">${product.rentalPrice}/hour</span></p>
          <p className="text-gray-600">Transaction Time: <span className="font-semibold">{product.transactionTime || '2'}</span> hours</p>

          {/* Hours Selector */}
          <div className="mt-4">
            <label className="block text-lg font-medium mb-2">Select Hours:</label>
            <select
              value={product.transactionTime}
              onChange={(e) => setProduct({...product, transactionTime: Number(e.target.value)})}
              className="w-full p-2 border border-gray-300 rounded-md"
            >
              {[...Array(MAX_HOURS)].map((_, i) => (
                <option key={i + 1} value={i + 1}>
                  {i + 1} {i + 1 === 1 ? 'hour' : 'hours'}
                </option>
              ))}
            </select>
          </div>

          {/* Total Price Display */}
          <p className="mt-2 text-lg font-semibold">
            Total Price: ${(product.rentalPrice * (product.transactionTime || 1)).toFixed(2)}
          </p>

          {/* Date Picker for Rental Date */}
          <div className="mt-6">
            <label className="block text-lg font-medium mb-2">Select Rental Date:</label>
            <DatePicker
              selected={selectedDate}
              onChange={(date) => setSelectedDate(date)}
              dateFormat="MMMM d, yyyy"
              className="w-full p-2 border border-gray-300 rounded-md"
              placeholderText="Choose a date"
              minDate={new Date()} // Disable past dates
            />
          </div>

          {/* Rent Now Button */}
          <button
            onClick={handleRentNow}
            className="mt-4 w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          >
            Rent Now
          </button>
        </div>

        {/* Right Section: Lender Information */}
        <div className="bg-white p-6 rounded-lg shadow-md">
          {lender ? (
            <>
              <h3 className="heading-lg mb-2">Lender Information</h3>
              <p>Name: {lender.name}</p>
              <p>Rating: {lender.rating || '4.5'}</p>
              <p>Location: {lender.location || 'New York, NY'}</p>
            </>
          ) : (
            <p>Loading lender information...</p>
          )}
          <h3 className="heading-lg mt-4">Lender's Location</h3>
          {/* Integrate Google Maps or any map service here */}
          <p>Google map location goes here.</p>
        </div>
      </main>
    </div>
  );
};

export default ProductPage;
