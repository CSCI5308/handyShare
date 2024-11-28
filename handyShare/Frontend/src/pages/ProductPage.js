import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import HeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar.js';
import { message, Modal, List } from 'antd';
import { MailOutlined, PhoneOutlined, ProductFilled, StarFilled } from '@ant-design/icons';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import lenderService from '../services/lenderService.js';
import LenderMap from '../components/LendingPage/LenderMap';

const ProductPage = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [lenderDetails, setLenderDetails] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [currentReviewIndex, setCurrentReviewIndex] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState("");
  const [selectedHours, setSelectedHours] = useState(1);
  const [showTimeSelector, setShowTimeSelector] = useState(true);
  const [timeValid, setTimeValid] = useState(true);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [lenderProducts, setLenderProducts] = useState([]);
  const [modalLoading, setModalLoading] = useState(false);
  const [modalError, setModalError] = useState(null);

  const MAX_HOURS = 24;
  const MAX_DAYS_IN_ADVANCE = 3;

  useEffect(() => {
    const fetchProductDetails = async () => {
      if (!id || id === 'null') {
        setError('Invalid product ID.');
        setLoading(false);
        return;
      }

      try {
        const token = localStorage.getItem('token');

        // Fetch product details
        const productResponse = await axios.get(`http://172.17.0.99:8080/api/v1/user/product/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true
        });
        setProduct(productResponse.data);

        // Fetch reviews for the product
        const reviewsResponse = await axios.get(`http://172.17.0.99:8080/api/v1/user/review-product/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true
        });
        setReviews(reviewsResponse.data);

        setLoading(false);
      } catch (err) {
        setError('Error loading product details.');
        setLoading(false);
      }
    };
    fetchProductDetails();
  }, [id]);


  const handleLenderClick = async () => {
    if (!product || !product.lender || !product.lender.id) {
      message.error('Lender information is unavailable.');
      return;
    }

    setModalLoading(true);
    setModalError(null);

    try {
      const token = localStorage.getItem('token');
      const lenderData = await lenderService.getLenderDetails(product.lender.id, token);
      setLenderDetails(lenderData);
      setLenderProducts(lenderData.products);
      setIsModalVisible(true);
    } catch (errorMsg) {
      setModalError(errorMsg);
      message.error(errorMsg);
    } finally {
      setModalLoading(false);
    }
  };

  const handleNextReview = () => {
    setCurrentReviewIndex((prevIndex) =>
      prevIndex === reviews.length - 1 ? 0 : prevIndex + 1
    );
  };

  const handlePrevReview = () => {
    setCurrentReviewIndex((prevIndex) =>
      prevIndex === 0 ? reviews.length - 1 : prevIndex - 1
    );
  };

  // Handle rent now
  const handleRentNow = async () => {
    if (!selectedDate || !selectedTime) {
      message.warning('Please select both date and time to proceed with the rental.');
      return;
    }

    try {
      const token = localStorage.getItem('token');
      const totalAmount = product.rentalPrice * selectedHours;

      // Format the date-time string as requested
      const formattedDateTime = `${selectedDate.toISOString().split('T')[0]}T${selectedTime}:00`;

      const borrowData = {
        product: { id: parseInt(id) },
        duration: selectedHours,
        amount: totalAmount,
        penalty: Math.ceil(totalAmount * 0.5),
        timerStart: formattedDateTime
      };



      const response = await axios.post(
        'http://172.17.0.99:8080/api/v1/user/borrowProduct',
        borrowData,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
          withCredentials: true
        }
      );

      if (response.status === 200 || response.status === 201) {
        message.success('Rental request processed successfully!');
        navigate('/rent-summary', {
          state: {
            product,
            hours: selectedHours,
            selectedDate,
            selectedTime,
            borrowDetails: response.data
          }
        });
      }
    } catch (error) {
      console.error('Rental error:', error);
      message.error(error.response?.data?.message || 'Failed to process rental request. Please try again.');
    }
  };

  // Check if selected date is within allowed range
  const isValidDate = (date) => {
    const today = new Date();
    const maxDate = new Date(today);
    maxDate.setDate(today.getDate() + MAX_DAYS_IN_ADVANCE);
    return date <= maxDate;
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
            <div className="relative mt-4">
              <div className="text-gray-600 text-center">
                <p>
                  <strong>{reviews[currentReviewIndex].username}:</strong>{" "}
                  <span className="text-yellow-500">
                    {"★".repeat(reviews[currentReviewIndex].rating)}
                  </span>
                </p>
                <p>{reviews[currentReviewIndex].reviewText}</p>
                {reviews[currentReviewIndex].image && (
                  <img
                    src={reviews[currentReviewIndex].image}
                    alt="Review"
                    className="mt-2 mx-auto w-64 h-64 object-contain"
                  />
                )}
              </div>

              {/* Conditionally render navigation buttons */}
              {reviews.length > 1 && (
                <>
                  <button
                    onClick={handlePrevReview}
                    className="absolute top-1/2 left-0 transform -translate-y-1/2 bg-gray-200 rounded-full p-2 shadow hover:bg-gray-300"
                  >
                    ◀
                  </button>
                  <button
                    onClick={handleNextReview}
                    className="absolute top-1/2 right-0 transform -translate-y-1/2 bg-gray-200 rounded-full p-2 shadow hover:bg-gray-300"
                  >
                    ▶
                  </button>
                </>
              )}
            </div>
          ) : (
            <p>No reviews available.</p>
          )}
        </div>

        {/* Middle Section: Product Details */}
        <div className="bg-white p-6 rounded-lg shadow-md">
          <h2 className="heading-lg text-gray-800">{product.name}</h2>
          <p className="text-lg text-gray-600 mb-4">Price: <span className="font-semibold">${product.rentalPrice}/hour</span></p>

          {/* Hours Selector */}
          <div className="mt-4">
            <label className="block text-lg font-medium mb-2">Select Hours:</label>
            <select
              value={selectedHours}
              onChange={(e) => {
                const hours = Number(e.target.value);
                setSelectedHours(hours);
                setShowTimeSelector(true);
              }}
              className="w-full p-2 border border-gray-300 rounded-md"
            >
              {[...Array(MAX_HOURS)].map((_, i) => (
                <option key={i + 1} value={i + 1}>
                  {i + 1} {i + 1 === 1 ? 'hour' : 'hours'}
                </option>
              ))}
            </select>
          </div>

          {/* Time Selector */}
          {showTimeSelector && (
            <div className="mt-4">
              <label className="block text-lg font-medium mb-2">Select Date:</label>
              <DatePicker
                selected={selectedDate}
                onChange={(date) => {
                  setSelectedDate(date);
                  setTimeValid(isValidDate(date));
                }}
                dateFormat="MMMM d, yyyy"
                className="w-full p-2 border border-gray-300 rounded-md"
                placeholderText="Choose a date"
                minDate={new Date()}
                maxDate={new Date(new Date().setDate(new Date().getDate() + MAX_DAYS_IN_ADVANCE))}
              />
            </div>
          )}

          {/* Time of Day Selection */}
          {showTimeSelector && selectedDate && timeValid && (
            <div className="mt-4">
              <label className="block text-lg font-medium mb-2">Select Time:</label>
              <input
                type="time"
                value={selectedTime}
                onChange={(e) => setSelectedTime(e.target.value)}
                className="w-full p-2 border border-gray-300 rounded-md"
              />
            </div>
          )}

          {/* Display validation message */}
          {!timeValid && selectedDate && (
            <p className="text-red-500 text-sm mt-2">You can only book up to 3 days in advance.</p>
          )}

          {/* Total Price Display */}
          <p className="mt-2 text-lg font-semibold">
            Total Price: ${(product.rentalPrice * selectedHours).toFixed(2)}
          </p>

          {/* Rent Now Button */}
          <button
            onClick={handleRentNow}
            className="mt-4 w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          >
            Rent Now
          </button>
        </div>

        {/* Right Section: Lender Information */}
        <div className="bg-white p-6 rounded-lg shadow-md flex flex-col items-center">
          {product.lender ? (
            <div className="w-full">
              <h3 className="text-2xl font-semibold text-gray-800 mb-4 text-center">Lender Information</h3>
              {/* Lender Profile Picture with Click Handler */}
              <div className="flex justify-center cursor-pointer" onClick={handleLenderClick}>
                {product.lender.imageData ? (
                  <img
                    src={product.lender.imageData}
                    alt={product.lender.name}
                    className="w-24 h-24 rounded-full object-cover mb-4 shadow-lg"
                  />
                ) : (
                  <div className="w-24 h-24 bg-gray-300 rounded-full flex items-center justify-center mb-4">
                    <span className="text-gray-500">No Image Available</span>
                  </div>
                )}
              </div>

              {/* Lender Details */}
              <div className="text-center space-y-2">
                <p className="text-lg flex items-center justify-center"><MailOutlined className="mr-2" /> {product.lender.email}</p>
                {product.lender.phone && (
                  <p className="text-lg flex items-center justify-center"><PhoneOutlined className="mr-2" /> {product.lender.phone}</p>
                )}
                {product.lender.rating && (
                  <p className="text-lg flex items-center justify-center">
                    <StarFilled className="text-yellow-500 mr-2" /> {product.lender.rating} ★
                  </p>
                )}
              </div>

              {/* Lender's Location */}
              <h3 className="text-2xl font-semibold text-gray-800 mt-6 mb-2 text-center">Lender's Location</h3>
              <div className="w-full h-48 rounded-md overflow-hidden">
                <LenderMap address={lenderDetails?.address || product.lender.address} />
              </div>
            </div>
          ) : (
            <p>Loading lender information...</p>
          )}
        </div>
      </main>

      {/* Lender Details Modal */}
      <Modal
        title="Lender Details"
        visible={isModalVisible}
        onCancel={() => setIsModalVisible(false)}
        footer={null}
      >
        {modalLoading ? (
          <div className="text-center">Loading...</div>
        ) : modalError ? (
          <div className="text-center text-red-500">{modalError}</div>
        ) : lenderDetails ? (
          <div>
            <div className="flex items-center mb-4">
              {lenderDetails.imageData ? (
                <img src={lenderDetails.imageData} alt={lenderDetails.name} className="w-24 h-24 rounded-full object-cover mr-4" />
              ) : (
                <div className="w-24 h-24 bg-gray-300 rounded-full flex items-center justify-center mr-4">
                  <span className="text-gray-500">No Image</span>
                </div>
              )}
              <div>
                <h2 className="text-xl font-semibold">{lenderDetails.name}</h2>
                <p><MailOutlined className="mr-2" /> {lenderDetails.email}</p>
                {lenderDetails.phone && <p><PhoneOutlined className="mr-2" /> {lenderDetails.phone}</p>}
              </div>
            </div>
            <div className="mb-4">
              <h3 className="font-semibold">Address:</h3>
              <p>{lenderDetails.address || 'N/A'}</p>
              <p>Pincode: {lenderDetails.pincode || 'N/A'}</p>
            </div>
            <div>
              <h3 className="font-semibold mb-2">Products by {lenderDetails.name}:</h3>
              {lenderProducts.length > 0 ? (
                <List
                  itemLayout="horizontal"
                  dataSource={lenderProducts}
                  renderItem={item => (
                    <List.Item>
                      <List.Item.Meta
                        title={<a href={`/product/${item.id}`}>{item.name}</a>}
                        description={`Price: $${item.rentalPrice}/hour`}
                      />
                    </List.Item>
                  )}
                />
              ) : (
                <p>No products listed by this lender.</p>
              )}
            </div>
          </div>
        ) : null}
      </Modal>
    </div>
  );
};

export default ProductPage;

