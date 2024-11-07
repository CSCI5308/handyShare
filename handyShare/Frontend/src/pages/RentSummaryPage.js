import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import HeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar.js';
import { motion } from 'framer-motion';
import { Tooltip, Modal } from 'antd';
import defaultProfileImage from './defaultProfileImage.png';

const RentSummaryPage = () => {
  const navigate = useNavigate();
  const location = useLocation();

  // Extracting product, hours, and selectedDate from state
  const { product, hours, selectedDate } = location.state || {};

  const [isModalVisible, setIsModalVisible] = React.useState(false);

  if (!product || !hours) {
    return <div>Invalid data passed. Please go back and try again.</div>;
  }

  const totalPrice = product.rentalPrice * hours;

  const handleConfirm = () => {
    setIsModalVisible(false);
    navigate('/payment', { state: { amount: totalPrice } });
  };

  // Ensure selectedDate is a Date object
  const rentalDate = selectedDate ? new Date(selectedDate) : null;

  return (
    <div className="min-h-screen bg-gradient-to-r from-blue-50 to-blue-100 flex flex-col">
      <HeaderBar />
      
      <div className="max-w-4xl mx-auto p-6 mt-10">
        <h2 className="text-3xl font-bold text-center text-gray-700 mb-8">Rent Summary</h2>

        <motion.div
          className="bg-white shadow-xl rounded-lg p-6"
          initial={{ y: 30, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ duration: 0.6, ease: 'easeOut' }}
        >
          <div className="flex flex-col md:flex-row">
            
            {/* Product Image */}
            <motion.div
              className="w-full md:w-1/3 mb-4 md:mb-0"
              initial={{ scale: 0.9 }}
              animate={{ scale: 1 }}
              transition={{ delay: 0.2, duration: 0.5 }}
            >
              {product.productImage ? (
                <img
                  src={product.productImage}
                  alt={product.name}
                  className="w-full h-64 object-cover rounded-md shadow-md"
                />
              ) : (
                <div className="w-full h-64 bg-gray-300 rounded-md flex items-center justify-center">
                  <span>No Image Available</span>
                </div>
              )}
            </motion.div>

            {/* Product Details */}
            <div className="w-full md:w-2/3 md:pl-8">
              <h3 className="text-2xl font-semibold text-gray-700 mb-4">{product.name}</h3>
              <p className="text-md mb-2"><strong>Price per Hour:</strong> ${product.rentalPrice}</p>
              <p className="text-md mb-2"><strong>Hours Selected:</strong> {hours}</p>
              {rentalDate && (
                <p className="text-md mb-2"><strong>Rental Date:</strong> {rentalDate.toLocaleDateString()}</p>
              )}
              <p className="text-lg font-bold mb-4 text-blue-600">
                <strong>Total Price:</strong> ${totalPrice.toFixed(2)}
              </p>

              {/* Confirm Button with Tooltip */}
              <Tooltip title="Proceed with the payment">
                <motion.button
                  onClick={() => setIsModalVisible(true)}
                  className="mt-6 bg-gradient-to-r from-blue-500 to-blue-700 hover:from-blue-600 hover:to-blue-800 text-white font-bold py-2 px-6 rounded-lg transition duration-300 shadow-lg"
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                >
                  Confirm and Proceed to Payment
                </motion.button>
              </Tooltip>
            </div>
          </div>
        </motion.div>
      </div>

      {/* Confirmation Modal */}
      <Modal
        title="Confirm Rental"
        visible={isModalVisible}
        onOk={handleConfirm}
        onCancel={() => setIsModalVisible(false)}
        okText="Yes, Proceed"
        cancelText="Cancel"
      >
        <p>
          Are you sure you want to proceed with renting <strong>{product.name}</strong> for <strong>{hours}</strong> hours at a total price of <strong>${totalPrice.toFixed(2)}</strong>?
        </p>
      </Modal>
    </div>
  );
};

export default RentSummaryPage;
