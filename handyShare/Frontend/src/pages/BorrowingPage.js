// import React, { useState, useEffect } from 'react';
// import axios from 'axios';
// import { useParams, useNavigate } from 'react-router-dom';
// import HeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar';  
// import DatePicker from 'react-datepicker';
// import 'react-datepicker/dist/react-datepicker.css';
// import { message } from 'antd';

// const BorrowingPage = () => {
//   const navigate = useNavigate(); 
//   const { id } = useParams();
//   const [product, setProduct] = useState(null);
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState(null);
//   const [selectedDate, setSelectedDate] = useState(null); 
//   const [selectedHours, setSelectedHours] = useState(1); 
//   const MAX_HOURS = 24;

//   useEffect(() => {
//     const fetchProductDetails = async () => {
//       if (!id || id === 'null') {
//         setError('Invalid product ID.');
//         setLoading(false);
//         return;
//       }

//       try {
//         const token = localStorage.getItem('token');
//         const response = await axios.get(`http://localhost:8080/api/v1/user/product/${id}`, {
//           headers: {
//             Authorization: `Bearer ${token}`
//           },
//           withCredentials: true
//         });

//         setProduct(response.data);
//         setLoading(false);
//       } catch (err) {
//         console.error('Error fetching product details:', err);
//         setError('Error fetching product details.');
//         setLoading(false);
//       }
//     };

//     fetchProductDetails();
//   }, [id]);

//   const handleBorrowNow = async () => {
//     if (!selectedDate) {
//       message.warning('Please select a date to proceed with the borrowing.');
//       return;
//     }

//     navigate('/borrow-summary', { 
//       state: { 
//         product, 
//         hours: selectedHours,
//         selectedDate 
//       } 
//     });
//   };

//   if (loading) return <div className="p-8 mt-16">Loading...</div>;
//   if (error) return <div className="p-8 mt-16 text-red-500">{error}</div>;
//   if (!product) return <div className="p-8 mt-16">Product not found.</div>;

//   return (
//     <div className="min-h-screen flex flex-col">
//       <HeaderBar />
//       <main className="flex-grow p-8 mt-16 flex">

//         <div className="w-1/3 bg-gray-100 p-4 mr-6 rounded-lg shadow-md">
//           {product.productImage ? (
//             <img src={product.productImage} alt={product.name} className="w-full h-64 object-cover rounded-md mb-4" />
//           ) : (
//             <div className="w-full h-64 bg-gray-300 rounded-md mb-4 flex items-center justify-center">
//               <span>No Image Available</span>
//             </div>
//           )}
//           <div>
//             <h3 className="text-lg font-semibold">Description</h3>
//             <p>{product.description}</p>
//           </div>
//         </div>

//         {/* Middle Section: Product Details */}
//         <div className="w-1/3 px-4">
//           <h2 className="text-2xl font-bold">{product.name}</h2>
//           <p className="text-lg">Price: ${product.rentalPrice}/hour</p>
          
//           {/* Hours Selector */}
//           <div className="mt-4">
//             <label className="block text-lg font-medium mb-2">Select Hours:</label>
//             <select
//               value={selectedHours}
//               onChange={(e) => setSelectedHours(Number(e.target.value))}
//               className="w-full p-2 border border-gray-300 rounded-md"
//             >
//               {[...Array(MAX_HOURS)].map((_, i) => (
//                 <option key={i + 1} value={i + 1}>
//                   {i + 1} {i + 1 === 1 ? 'hour' : 'hours'}
//                 </option>
//               ))}
//             </select>
//           </div>

//           {/* Total Price Display */}
//           <p className="mt-2 text-lg font-semibold">
//             Total Price: ${(product.rentalPrice * selectedHours).toFixed(2)}
//           </p>

//           {/* Date Picker for Rental Date */}
//           <div className="mt-6">
//             <label className="block text-lg font-medium mb-2">Select Rental Date:</label>
//             <DatePicker
//               selected={selectedDate}
//               onChange={(date) => setSelectedDate(date)}
//               dateFormat="MMMM d, yyyy"
//               className="w-full p-2 border border-gray-300 rounded-md"
//               placeholderText="Choose a date"
//               minDate={new Date()} // Disable past dates
//             />
//           </div>

//           {/* Borrow Now Button */}
//           <button
//             onClick={handleBorrowNow}
//             className="mt-4 w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
//           >
//             Borrow Now
//           </button>
//         </div>
//       </main>
//     </div>
//   );
// };

// export default BorrowingPage;

import React, { useState, useEffect } from 'react';
import { Layout, Menu, Card, Select, Input, Pagination } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import duration from 'dayjs/plugin/duration';
import HeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar.js';

dayjs.extend(duration);

const { Content, Sider } = Layout;
const { Option } = Select;

const BorrowingPage = () => {
  const [view, setView] = useState('borrowings');
  const [sortOrder, setSortOrder] = useState('Newest');
  const [searchText, setSearchText] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 3;

  // Static data for design purposes
  const borrowings = [
    {
      id: 1,
      product: {
        name: "Full Frame Mirrorless Camera",
        imageUrl: "https://via.placeholder.com/100",
      },
      borrowStartDate: "14-November-2024",
      borrowEndDate: "15-November-2024",
      pricePerDay: 150,
      orderNumber: 248,
    },
    {
      id: 2,
      product: {
        name: "DSLR Camera",
        imageUrl: "https://via.placeholder.com/100",
      },
      borrowStartDate: "12-November-2024",
      borrowEndDate: "14-November-2024",
      pricePerDay: 100,
      orderNumber: 249,
    },
    {
      id: 3,
      product: {
        name: "Tripod Stand",
        imageUrl: "https://via.placeholder.com/100",
      },
      borrowStartDate: "10-November-2024",
      borrowEndDate: "12-November-2024",
      pricePerDay: 25,
      orderNumber: 250,
    },
    {
      id: 4,
      product: {
        name: "External Flash",
        imageUrl: "https://via.placeholder.com/100",
      },
      borrowStartDate: "09-November-2024",
      borrowEndDate: "10-November-2024",
      pricePerDay: 30,
      orderNumber: 251,
    },
    {
      id: 5,
      product: {
        name: "Drone",
        imageUrl: "https://via.placeholder.com/100",
      },
      borrowStartDate: "01-November-2024",
      borrowEndDate: "05-November-2024",
      pricePerDay: 200,
      orderNumber: 252,
    },
  ];

  // Timer Component
  const Timer = ({ endDate }) => {
    const calculateTimeLeft = () => {
      const now = dayjs();
      const end = dayjs(endDate, "DD-MMMM-YYYY");
      const timeLeft = dayjs.duration(end.diff(now));
      return {
        days: timeLeft.days(),
        hours: timeLeft.hours(),
        minutes: timeLeft.minutes(),
        seconds: timeLeft.seconds(),
      };
    };

    const [timeLeft, setTimeLeft] = useState(calculateTimeLeft());

    useEffect(() => {
      const timer = setInterval(() => {
        setTimeLeft(calculateTimeLeft());
      }, 1000);

      return () => clearInterval(timer);
    }, [endDate]);

    return (
      <div>
        {timeLeft.days}d {timeLeft.hours}h {timeLeft.minutes}m {timeLeft.seconds}s
      </div>
    );
  };

  // Filtering and sorting borrowed items
  const filteredBorrowings = borrowings.filter(borrowing =>
    borrowing.product.name.toLowerCase().includes(searchText.toLowerCase())
  );

  const sortedBorrowings = [...filteredBorrowings].sort((a, b) => {
    return sortOrder === 'Newest'
      ? new Date(b.borrowStartDate) - new Date(a.borrowStartDate)
      : new Date(a.borrowStartDate) - new Date(b.borrowStartDate);
  });

  // Pagination logic
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = sortedBorrowings.slice(indexOfFirstItem, indexOfLastItem);

  const handleMenuClick = (e) => {
    setView(e.key);
  };

  const handleSortChange = (value) => {
    setSortOrder(value);
  };

  const handleSearchChange = (e) => {
    setSearchText(e.target.value);
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <div>
      <HeaderBar />
      <Layout>
        <Sider width={200}>
          <Menu
            mode="inline"
            defaultSelectedKeys={['borrowings']}
            style={{ height: '100%', borderRight: 0 }}
            onClick={handleMenuClick}
          >
            <Menu.Item key="borrowings">My Borrowings</Menu.Item>
          </Menu>
        </Sider>
        <Layout style={{ padding: '20px' }}>
          <Content style={{ padding: '20px', background: '#fff' }}>
            <h1 style={{ fontSize: '24px', fontWeight: 'bold', marginBottom: '10px' }}>Your Borrowed Items</h1>
            
            {/* Sort and Search Bar */}
            <div style={{ marginBottom: '20px', display: 'flex', alignItems: 'center' }}>
              <span style={{ marginRight: '10px' }}>Sort:</span>
              <Select
                defaultValue={sortOrder}
                onChange={handleSortChange}
                style={{ width: 120, marginRight: '10px' }}
              >
                <Option value="Newest">Newest</Option>
                <Option value="Oldest">Oldest</Option>
              </Select>
              <Input
                placeholder="Search by name"
                prefix={<SearchOutlined />}
                value={searchText}
                onChange={handleSearchChange}
                style={{ marginBottom: '8px', width: '300px', marginTop: '10px' }}
              />
            </div>

            {/* Borrowed Items List */}
            <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
              {currentItems.map(borrowing => (
                <Card
                  key={borrowing.id}
                  style={{ display: 'flex', alignItems: 'center', padding: '20px', position: 'relative' }}
                >
                  <img
                    src={borrowing.product.imageUrl}
                    alt="product"
                    style={{ width: '100px', height: '100px', marginRight: '20px' }}
                  />
                  <div style={{ flex: 1 }}>
                    <h3 style={{ margin: 0 }}>{borrowing.product.name}</h3>
                    <p style={{ margin: '5px 0' }}>Borrow Period: {borrowing.borrowStartDate} - {borrowing.borrowEndDate}</p>
                    <p style={{ margin: '5px 0' }}>Order Number: {borrowing.orderNumber}</p>
                    <p style={{ margin: '5px 0' }}>Borrowed for: {borrowing.pricePerDay} AED/day</p>
                  </div>
                  {/* Timer */}
                  <div style={{ position: 'absolute', top: '10px', right: '10px' }}>
                    <Timer endDate={borrowing.borrowEndDate} />
                  </div>
                </Card>
              ))}
            </div>

            {/* Pagination */}
            <Pagination
              current={currentPage}
              pageSize={itemsPerPage}
              total={sortedBorrowings.length}
              onChange={handlePageChange}
              style={{ marginTop: '20px', textAlign: 'right' }}
            />
          </Content>
        </Layout>
      </Layout>
    </div>
  );
};

export default BorrowingPage;
