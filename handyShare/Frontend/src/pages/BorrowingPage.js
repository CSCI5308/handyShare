import React, { useState, useEffect } from 'react'
import { motion, AnimatePresence } from 'framer-motion'
import axios from 'axios'
import dayjs from 'dayjs'
import duration from 'dayjs/plugin/duration'
import { FaSearch, FaCheck, FaUpload } from 'react-icons/fa'
import AnimatedHeader from '../components/Header'

dayjs.extend(duration)

 

const AnalogTimer = ({ startDate, endDate }) => {
  const calculateTimeLeft = () => {
    const now = dayjs()
    const start = dayjs(startDate)
    const end = dayjs(endDate)

    if (now.isBefore(start) || now.isAfter(end)) {
      return null
    }

    const timeLeft = dayjs.duration(end.diff(now))
    return {
      days: timeLeft.days(),
      hours: timeLeft.hours(),
      minutes: timeLeft.minutes(),
      seconds: timeLeft.seconds(),
    }
  }

  const [timeLeft, setTimeLeft] = useState(calculateTimeLeft())

  useEffect(() => {
    const timer = setInterval(() => {
      setTimeLeft(calculateTimeLeft())
    }, 1000)

    return () => clearInterval(timer)
  }, [startDate, endDate])

  if (!timeLeft) return null

  const totalSeconds = timeLeft.days * 86400 + timeLeft.hours * 3600 + timeLeft.minutes * 60 + timeLeft.seconds
  const totalDuration = dayjs(endDate).diff(dayjs(startDate), 'second')
  const progress = (totalSeconds / totalDuration) * 100

  return (
    <div className="relative w-24 h-24">
      <svg viewBox="0 0 100 100" className="w-full h-full transform -rotate-90">
        <circle
          cx="50"
          cy="50"
          r="45"
          fill="none"
          stroke="#e0e0e0"
          strokeWidth="10"
        />
        <motion.circle
          cx="50"
          cy="50"
          r="45"
          fill="none"
          stroke="#3b82f6"
          strokeWidth="10"
          strokeDasharray="283"
          initial={{ strokeDashoffset: 283 }}
          animate={{ strokeDashoffset: 283 - (283 * progress) / 100 }}
          transition={{ duration: 0.5 }}
        />
      </svg>
      <div className="absolute inset-0 flex flex-col items-center justify-center text-xs">
        <span className="font-bold text-lg">{timeLeft.days}d</span>
        <span>{timeLeft.hours}h {timeLeft.minutes}m</span>
      </div>
    </div>
  )
}

function BorrowingPage() {
  const [borrowings, setBorrowings] = useState([])
  const [sortOrder, setSortOrder] = useState('Newest')
  const [searchText, setSearchText] = useState('')
  const [currentPage, setCurrentPage] = useState(1)
  const [isModalVisible, setIsModalVisible] = useState(false)
  const [selectedFile, setSelectedFile] = useState(null)
  const [returnedItems, setReturnedItems] = useState(new Set())
  const [selectedBorrowId, setSelectedBorrowId] = useState(null)

  const itemsPerPage = 3

  useEffect(() => {
    const fetchBorrowedProducts = async () => {
      try {
        const token = localStorage.getItem('token')
        const response = await axios.get('http://localhost:8080/api/v1/user/borrowedProducts', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          withCredentials: true,
        })
        setBorrowings(response.data)
      } catch (err) {
        console.error('Error fetching borrowed products:', err)
      }
    }

    fetchBorrowedProducts()
  }, [])

  const filteredBorrowings = borrowings.filter((borrowing) =>
    borrowing.product.name.toLowerCase().includes(searchText.toLowerCase())
  )

  const sortedBorrowings = [...filteredBorrowings].sort((a, b) => {
    return sortOrder === 'Newest'
      ? new Date(b.timerStart) - new Date(a.timerStart)
      : new Date(a.timerStart) - new Date(b.timerStart)
  })

  const indexOfLastItem = currentPage * itemsPerPage
  const indexOfFirstItem = indexOfLastItem - itemsPerPage
  const currentItems = sortedBorrowings.slice(indexOfFirstItem, indexOfLastItem)

  const handleSortChange = (e) => {
    setSortOrder(e.target.value)
  }

  const handleSearchChange = (e) => {
    setSearchText(e.target.value)
  }

  const handlePageChange = (page) => {
    setCurrentPage(page)
  }

  const showReturnModal = (borrowId) => {
    setSelectedBorrowId(borrowId)
    setIsModalVisible(true)
  }

  const handleFileChange = (e) => {
    if (e.target.files) {
      setSelectedFile(e.target.files[0])
    }
  }

  const calculateBreakdown = (borrowing) => {
    const penalty = borrowing.penalty || 0
    const totalPayment = borrowing.totalPayment || 0
    const platformFees = totalPayment * 0.02
    const productCharge = totalPayment - penalty - platformFees

    return { productCharge, penalty, platformFees, totalPayment }
  }

  const handleCancel = () => {
    setIsModalVisible(false)
    setSelectedFile(null)
    setSelectedBorrowId(null)
  }

  const handleReturnSubmit = async () => {
    if (!selectedFile) {
      alert("Please select an image to proceed.")
      return
    }

    if (selectedFile.size > 5 * 1024 * 1024) {
      alert("File size exceeds the limit of 5MB.")
      return
    }

    if (!['image/jpeg', 'image/png', 'image/gif'].includes(selectedFile.type)) {
      alert("Only image files are allowed.")
      return
    }

    const formData = new FormData()
    formData.append('borrowId', selectedBorrowId)
    formData.append('productImage', selectedFile)

    try {
      const token = localStorage.getItem('token')
      await axios.post('http://localhost:8080/api/v1/user/product/ReturnedBorrower', formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'multipart/form-data',
        },
      })

      setReturnedItems((prev) => new Set(prev).add(selectedBorrowId))
      alert("Product returned successfully!")
    } catch (error) {
      alert("Failed to return product")
      console.error(error)
    } finally {
      handleCancel()
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-blue-100 to-blue-200">
<AnimatedHeader/>
      <div className="container mx-auto px-4 py-8">
        <motion.h1 
          className="text-3xl font-bold mb-8 text-center text-gray-800"
          initial={{ opacity: 0, y: -50 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.8, type: "spring", stiffness: 120 }}
        >
          Your Borrowed Items
        </motion.h1>
        
        <div className="flex flex-col md:flex-row justify-between items-center mb-6">
          <motion.select
            className="mb-4 md:mb-0 p-2 border border-gray-300 rounded-md bg-white shadow-sm"
            value={sortOrder}
            onChange={handleSortChange}
            initial={{ opacity: 0, x: -50 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.5, delay: 0.2 }}
          >
            <option value="Newest">Newest</option>
            <option value="Oldest">Oldest</option>
          </motion.select>
          <motion.div 
            className="relative w-full md:w-64"
            initial={{ opacity: 0, x: 50 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ duration: 0.5, delay: 0.4 }}
          >
            <FaSearch className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
            <input
              type="text"
              placeholder="Search by name"
              value={searchText}
              onChange={handleSearchChange}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md bg-white shadow-sm"
            />
          </motion.div>
        </div>

        <AnimatePresence>
          {currentItems.map((borrowing, index) => {
            const isReturned = borrowing.returnImage || borrowing.returnByBorrowerTime || returnedItems.has(borrowing.id)
            const breakdown = calculateBreakdown(borrowing)

            return (
              <motion.div
                key={borrowing.id}
                initial={{ opacity: 0, y: 50 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0, y: -50 }}
                transition={{ duration: 0.5, delay: index * 0.1 }}
                className="bg-[#FFFAF0] rounded-lg shadow-lg mb-6 overflow-hidden transform hover:scale-105 transition-all duration-300"
              >
                <div className="p-6 flex flex-col md:flex-row items-start space-y-4 md:space-y-0 md:space-x-6">
                  <div className="w-full md:w-1/3 bg-[#FFF8E7] rounded-lg p-4 shadow-inner">
                    <img
                      src={borrowing.product.productImage}
                      alt={borrowing.product.name}
                      className="w-full h-48 rounded-lg object-cover mb-4"
                    />
                    <h3 className="text-xl font-semibold mb-2">{borrowing.product.name}</h3>
                    <p className="text-gray-600 mb-2">{borrowing.product.description}</p>
                    <p className="text-sm text-gray-500">Price: {borrowing.product.rentalPrice} $/day</p>
                    <p className="text-sm text-gray-500">
                      Duration: {dayjs(borrowing.timerEnd).diff(dayjs(borrowing.timerStart), 'days')} days
                    </p>
                  </div>
                  <div className="w-full md:w-1/3 bg-[#FFF8E7] rounded-lg p-4 shadow-inner">
                    <h4 className="font-semibold mb-3 text-lg">Payment Breakdown</h4>
                    <div className="space-y-2">
                      <p className="text-sm text-gray-600 flex justify-between">
                        <span>Product Charge:</span>
                        <span>$ {breakdown.productCharge.toFixed(2)} </span>
                      </p>
                      <p className="text-sm text-gray-600 flex justify-between">
                        <span>Penalty:</span>
                        <span>$ {breakdown.penalty.toFixed(2)} </span>
                      </p>
                      <p className="text-sm text-gray-600 flex justify-between">
                        <span>Platform Fees (2%):</span>
                        <span>$ {breakdown.platformFees.toFixed(2)} </span>
                      </p>
                      <div className="border-t border-gray-300 my-2"></div>
                      <p className="text-sm font-medium flex justify-between">
                        <span>Total Payment:</span>
                        <span>$ {breakdown.totalPayment.toFixed(2)} </span>
                      </p>
                    </div>
                  </div>
                  <div className="w-full md:w-1/3 bg-[#FFF8E7] rounded-lg p-4 shadow-inner flex flex-col items-center justify-center space-y-4">
                    {isReturned ? (
                      <div className="w-24 h-24 rounded-full bg-green-100 flex items-center justify-center">
                        <FaCheck className="w-12 h-12 text-green-500" />
                      </div>
                    ) : (
                      <AnalogTimer startDate={borrowing.timerStart} endDate={borrowing.timerEnd} />
                    )}
                    <motion.button
                      onClick={() => showReturnModal(borrowing.id)}
                      disabled={isReturned}
                      className={`px-6 py-2 rounded-full text-sm font-medium transition-colors duration-300 ${
                        isReturned
                          ? 'bg-gray-300 text-gray-600 cursor-not-allowed'
                          : 'bg-blue-600 text-white hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2'
                      }`}
                      whileHover={{ scale: 1.05 }}
                      whileTap={{ scale: 0.95 }}
                    >
                      {isReturned ? 'Returned' : 'Return Product'}
                    </motion.button>
                  </div>
                </div>
              </motion.div>
            )
          })}
        </AnimatePresence>

        <div className="flex justify-center mt-8 space-x-2">
          {Array.from({ length: Math.ceil(sortedBorrowings.length / itemsPerPage) }, (_, i) => (
            <motion.button
              key={i}
              onClick={() => handlePageChange(i + 1)}
              className={`w-10 h-10 flex items-center justify-center rounded-full transition-colors duration-300 ${
                currentPage === i + 1
                  ? 'bg-blue-600 text-white'
                  : 'bg-white text-blue-600 border border-blue-600 hover:bg-blue-100'
              }`}
              whileHover={{ scale: 1.1 }}
              whileTap={{ scale: 0.9 }}
            >
              {i + 1}
            </motion.button>
          ))}
        </div>
      </div>

      <AnimatePresence>
        {isModalVisible && (
          <motion.div 
            className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
          >
            <motion.div 
              className="bg-white rounded-lg p-6 w-full max-w-md"
              initial={{ scale: 0.5, y: -100 }}
              animate={{ scale: 1, y: 0 }}
              exit={{ scale: 0.5, y: 100 }}
              transition={{ type: "spring", damping: 15 }}
            >
              <h2 className="text-2xl font-bold mb-4">Return Product</h2>
              <p className="mb-4 text-gray-600">Please upload an image of the returned product:</p>
              <div className="border-2 border-dashed border-gray-300 rounded-lg p-4 text-center cursor-pointer hover:border-blue-500 transition-colors duration-300">
                <input
                  type="file"
                  onChange={handleFileChange}
                  accept="image/*"
                  className="hidden"
                  id="file-upload"
                />
                <label htmlFor="file-upload" className="cursor-pointer flex flex-col items-center justify-center text-gray-500 hover:text-blue-500">
                  <FaUpload className="w-12 h-12 mb-2" />
                  <span className="text-sm font-medium">Click to upload or drag and drop</span>
                  <span className="text-xs">PNG, JPG, GIF up to 5MB</span>
                </label>
              </div>
              {selectedFile && (
                <p className="mt-2 text-sm text-gray-500">
                  Selected file: {selectedFile.name}
                </p>
              )}
              <div className="flex justify-end space-x-4 mt-6">
                <motion.button
                  onClick={handleCancel}
                  className="px-4 py-2 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 transition-colors duration-300"
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                >
                  Cancel
                </motion.button>
                <motion.button
                  onClick={handleReturnSubmit}
                  className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors duration-300"
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                >
                  Submit
                </motion.button>
              </div>
            </motion.div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  )
}

export default BorrowingPage
 