import React, { useState, useEffect } from 'react';
import { Layout, Menu, Card, Select, Input, Pagination, Row, Col, Avatar, Typography } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import axios from 'axios';
import dayjs from 'dayjs';
import duration from 'dayjs/plugin/duration';
import HeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar.js';

dayjs.extend(duration);

const { Content, Sider } = Layout;
const { Option } = Select;
const { Title, Text } = Typography;

const BorrowingPage = () => {
  const [borrowings, setBorrowings] = useState([]);
  const [view, setView] = useState('borrowings');
  const [sortOrder, setSortOrder] = useState('Newest');
  const [searchText, setSearchText] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 3;

  useEffect(() => {
    const fetchBorrowedProducts = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:8080/api/v1/user/borrowedProducts', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          withCredentials: true,
        });
        setBorrowings(response.data);
      } catch (err) {
        console.error('Error fetching borrowed products:', err);
      }
    };

    fetchBorrowedProducts();
  }, []);

  const AnalogTimer = ({ startDate, endDate }) => {
    const calculateTimeLeft = () => {
      const now = dayjs();
      const start = dayjs(startDate);
      const end = dayjs(endDate);

      if (now.isBefore(start) || now.isAfter(end)) {
        return null;
      }

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
    }, [startDate, endDate]);

    if (!timeLeft) return null;

    return (
      <div style={{ fontSize: '16px', fontWeight: 'bold' }}>
        {timeLeft.days}d {timeLeft.hours}h {timeLeft.minutes}m {timeLeft.seconds}s
      </div>
    );
  };

  const filteredBorrowings = borrowings.filter((borrowing) =>
    borrowing.product.name.toLowerCase().includes(searchText.toLowerCase())
  );

  const sortedBorrowings = [...filteredBorrowings].sort((a, b) => {
    return sortOrder === 'Newest'
      ? new Date(b.timerStart) - new Date(a.timerStart)
      : new Date(a.timerStart) - new Date(b.timerStart);
  });

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
    setCurrentPage(1);
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <div>
      <HeaderBar />
      <Layout style={{ minHeight: '100vh' }}>
        <Sider width={220} style={{ background: '#fff' }}>
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
          <Content style={{ background: '#f9f9f9', padding: '30px', borderRadius: '8px' }}>
            <Title level={2} style={{ marginBottom: '20px' }}>Your Borrowed Items</Title>

            <Row gutter={[16, 16]} style={{ marginBottom: '20px' }} align="middle">
              <Col xs={24} sm={12} md={8} lg={6}>
                <Text strong>Sort By:</Text>
                <Select
                  value={sortOrder}
                  onChange={handleSortChange}
                  style={{ width: '100%', marginTop: '5px' }}
                >
                  <Option value="Newest">Newest</Option>
                  <Option value="Oldest">Oldest</Option>
                </Select>
              </Col>
              <Col xs={24} sm={12} md={16} lg={12}>
                <Input
                  placeholder="Search by product name"
                  prefix={<SearchOutlined />}
                  value={searchText}
                  onChange={handleSearchChange}
                  allowClear
                />
              </Col>
            </Row>

            <Row gutter={[24, 24]}>
              {currentItems.map((borrowing) => (
                <Col xs={24} sm={24} md={12} lg={8} key={borrowing.id}>
                  <Card hoverable>
                    <Row gutter={[16, 16]}>
                      {/* Product Section */}
                      <Col span={24}>
                        <Row gutter={[16, 16]}>
                          <Col span={8}>
                            <img
                              src={borrowing.product.productImage}
                              alt={borrowing.product.name}
                              style={{ width: '100%', borderRadius: '8px' }}
                            />
                          </Col>
                          <Col span={16}>
                            <Title level={4}>{borrowing.product.name}</Title>
                            <Text>{borrowing.product.description}</Text>
                            <br />
                            <Text strong>Price:</Text> {borrowing.product.rentalPrice} AED/day
                            <br />
                            <Text strong>Duration:</Text> {dayjs(borrowing.timerEnd).diff(dayjs(borrowing.timerStart), 'days')} days
                          </Col>
                        </Row>
                      </Col>

                      {/* Timer and Lender Section */}
                      <Col span={24}>
                        <Row justify="space-between" align="middle">
                          {/* Timer */}
                          <Col>
                            <Text strong>Time Left:</Text>
                            <AnalogTimer startDate={borrowing.timerStart} endDate={borrowing.timerEnd} />
                          </Col>

                          {/* Lender */}
                          <Col>
                            <Row align="middle" gutter={[8, 8]}>
                              <Col>
                                <Avatar
                                  src={borrowing.product.lender.imageData}
                                  size="large"
                                />
                              </Col>
                              <Col>
                                <Text strong>{borrowing.product.lender.name}</Text>
                                <br />
                                <Text type="secondary">{borrowing.product.lender.email}</Text>
                              </Col>
                            </Row>
                          </Col>
                        </Row>
                      </Col>
                    </Row>
                  </Card>
                </Col>
              ))}
            </Row>

            <Pagination
              current={currentPage}
              pageSize={itemsPerPage}
              total={sortedBorrowings.length}
              onChange={handlePageChange}
              style={{ textAlign: 'center', marginTop: '20px' }}
              showSizeChanger={false}
            />
          </Content>
        </Layout>
      </Layout>
    </div>
  );
};

export default BorrowingPage;
