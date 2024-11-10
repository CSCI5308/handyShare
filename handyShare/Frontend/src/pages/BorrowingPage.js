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
      calculateTimeLeft();
      const timer = setInterval(() => {
        calculateTimeLeft();
      }, 1000);
      
      return () => clearInterval(timer);
    }, [calculateTimeLeft]);

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
