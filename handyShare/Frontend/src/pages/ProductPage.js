import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import HeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar';  
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { message } from 'antd';
import { useNavigate } from 'react-router-dom'; 

const ProductPage = () => {
  const navigate = useNavigate(); 
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [lender, setLender] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedDate, setSelectedDate] = useState(null); // State for selected date
  const [selectedHours, setSelectedHours] = useState(1); // Initialize to 1 hour
  const MAX_HOURS = 24; // Define a maximum number of selectable hours

  useEffect(() => {
    console.log('Product ID from URL:', id); // Debugging

    const fetchProductDetails = async () => {
      if (!id || id === 'null') {
        setError('Invalid product ID.');
        setLoading(false);
        return;
      }

      try {
        const token = localStorage.getItem('token');
        // Fetch product details
        const productResponse = await axios.get(`http://localhost:8080/api/v1/user/product/${id}`,{
          headers: {
            Authorization: `Bearer ${token}`
          },
          withCredentials: true
        });
        console.log('Fetched Product:', productResponse.data); // Debugging
        setProduct(productResponse.data);

        // Fetch reviews related to the product
        // Uncomment if reviews are needed
        // const reviewsResponse = await axios.get(`http://localhost:8080/api/v1/all/allProducts/${id}/reviews`);
        // console.log('Fetched Reviews:', reviewsResponse.data); // Debugging
        // setReviews(reviewsResponse.data);

        // Check if userId exists before making the request
        if (productResponse.data.userId) {
          const lenderResponse = await axios.get(`http://localhost:8080/api/v1/all/users/${productResponse.data.userId}`,{
            headers: {
              Authorization: `Bearer ${token}`
            },
            withCredentials: true
          });
          console.log('Fetched Lender:', lenderResponse.data); // Debugging
          setLender(lenderResponse.data);
        } else {
          console.warn('Product does not have a userId.');
          setLender(null);
        }

        setLoading(false);
      } catch (err) {
        console.error('Error fetching product details:', err);
        setError('/Under Development');
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

    console.log('Selected Hours:', selectedHours); // Debugging

    navigate('/rent-summary', { 
      state: { 
        product, 
        hours: selectedHours,  // Pass the selected hours correctly
        selectedDate 
      } 
    });
  };

  if (loading) return <div className="p-8 mt-16">Loading...</div>;
  if (error) return <div className="p-8 mt-16 text-red-500">{error}</div>;
  if (!product) return <div className="p-8 mt-16">Product not found.</div>;

  return (
    <div className="min-h-screen flex flex-col">
      <HeaderBar />
      <main className="flex-grow p-8 mt-16 flex">

        <div className="w-1/3 bg-gray-100 p-4 mr-6 rounded-lg shadow-md">
          {product.productImage ? (
            <img src={product.productImage} alt={product.name} className="w-full h-64 object-cover rounded-md mb-4" />
          ) : (
            <div className="w-full h-64 bg-gray-300 rounded-md mb-4 flex items-center justify-center">
              <span>No Image Available</span>
            </div>
          )}
          <div>
            <h3 className="text-lg font-semibold">Description</h3>
            <p>{product.description}</p>
          </div>
          <div className="mt-4">
            <h3 className="text-lg font-semibold">Reviews</h3>
            {reviews.length > 0 ? (
              reviews.map((review, index) => (
                <div key={index} className="mt-2">
                  <p><strong>{review.user}:</strong> {review.comment}</p>
                </div>
              ))
            ) : (
              <p>No reviews available.</p>
            )}
          </div>
        </div>

        {/* Middle Section: Product Details */}
        <div className="w-1/3 px-4">
          <h2 className="text-2xl font-bold">{product.name}</h2>
          <p className="text-lg">Price: ${product.rentalPrice}/hour</p>
          
          {/* Hours Selector */}
          <div className="mt-4">
            <label className="block text-lg font-medium mb-2">Select Hours:</label>
            <select
              value={selectedHours}
              onChange={(e) => setSelectedHours(Number(e.target.value))}
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
            Total Price: ${(product.rentalPrice * selectedHours).toFixed(2)}
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
              minDate={new Date()} // Optional: disable past dates
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
        <div className="w-1/3">
          {lender ? (
            <div className="mb-4 p-4 bg-gray-100 rounded-lg shadow-md">
              <h3 className="text-lg font-semibold">Lender Information</h3>
              <p>Name: {lender.name}</p>
              <p>Rating: {lender.rating || '4.5'}</p>
              <p>Location: {lender.location || 'New York, NY'}</p>
            </div>
          ) : (
            <div className="mb-4 p-4 bg-gray-100 rounded-lg shadow-md">
              <h3 className="text-lg font-semibold">Lender Information</h3>
              <p>Loading lender information...</p>
            </div>
          )}
          <div className="mt-4 p-4 bg-gray-100 rounded-lg shadow-md">
            <h3 className="text-lg font-semibold">Lender's Location</h3>
            {/* Integrate Google Maps or any map service here */}
            <p>Google map location goes here.</p>
          </div>
        </div>
      </main>
    </div>
  );
};

export default ProductPage;