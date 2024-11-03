import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';  
import HeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar.js';
import defaultProfileImage from './defaultProfileImage.png'; 

const RentSummaryPage = () => {
  const navigate = useNavigate();
  const location = useLocation();  

  // Extracting product, hours, and selectedDate from state
  const { product, hours, selectedDate } = location.state || {};

  console.log('Received Hours in RentSummaryPage:', hours); // Debugging

  if (!product || !hours) {
    return <div>Invalid data passed. Please go back and try again.</div>;
  }

  const totalPrice = product.rentalPrice * hours;

  // Ensure selectedDate is a Date object
  const rentalDate = selectedDate ? new Date(selectedDate) : null;

  return (
    <div>
      <HeaderBar />
      <div className="max-w-4xl mx-auto p-6">
        <h2 className="text-2xl font-semibold text-center mb-5">Rent Summary</h2>

        <div className="bg-white shadow-md rounded-lg p-6">
          <div className="flex">
            <div className="w-1/3">
              {product.productImage ? (
                <img src={product.productImage} alt={product.name} className="w-full h-64 object-cover rounded-md mb-4" />
              ) : (
                <div className="w-full h-64 bg-gray-300 rounded-md mb-4 flex items-center justify-center">
                  <span>No Image Available</span>
                </div>
              )}
            </div>

            {/* Product Details */}
            <div className="w-2/3 pl-6">
              <h3 className="text-xl font-semibold mb-4">{product.name}</h3>
              <p className="text-md mb-2"><strong>Price per Hour:</strong> ${product.rentalPrice}</p>
              <p className="text-md mb-2"><strong>Hours Selected:</strong> {hours}</p>
              {rentalDate && (
                <p className="text-md mb-2"><strong>Rental Date:</strong> {rentalDate.toLocaleDateString()}</p>
              )}
              <p className="text-lg font-bold mb-4"><strong>Total Price:</strong> ${totalPrice.toFixed(2)}</p>

              {/* Button to confirm */}
              <button
                onClick={() => navigate('/payment', { state: { amount: totalPrice } })}
                className="mt-4 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
              >
                Confirm and Proceed to Payment
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RentSummaryPage;