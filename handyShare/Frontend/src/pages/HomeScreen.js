import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Layout, Button, Input, Skeleton } from 'antd';
import { motion, AnimatePresence } from 'framer-motion';
import { FaSearch, FaBox, FaChartLine, FaPlus, FaSpinner, FaCat, FaDog, FaFish } from 'react-icons/fa';
import { SERVER_URL } from '../constants.js';
import  AnimatedHeader   from '../components/Header.js';

const { Header, Content, Sider } = Layout;

const EnhancedHomeScreen = () => {
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState('');
  const [trending, setTrending] = useState([]);
  const [newlyAdded, setNewlyAdded] = useState([]);
  const [hasProducts, setHasProducts] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    const role = urlParams.get('role');
    
    if (token) {
      localStorage.setItem('token', token);
      localStorage.setItem('role', role);
      window.history.replaceState({}, document.title, window.location.pathname);
    } else if (!localStorage.getItem('token')) {
      navigate('/login');
    }

    fetchCategories();
  }, [navigate]);

  useEffect(() => {
    if (selectedCategory) {
      fetchTrending();
      fetchNewListings();
    }
  }, [selectedCategory]);

  const fetchCategories = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await axios.get("http://localhost:8080/api/v1/user/allCategories", {
        headers: { Authorization: `Bearer ${token}` },
        withCredentials: true
      });
      setCategories(response.data);
      setIsLoading(false);
    } catch (error) {
      console.error("Error fetching categories", error);
      setIsLoading(false);
    }
  };

  const fetchTrending = async () => {
    setIsLoading(true);
    const token = localStorage.getItem('token');
    try {
      const response = await axios.get(SERVER_URL + `/api/v1/user/getTrendingByCategory?category=${selectedCategory}`, {
        headers: { Authorization: `Bearer ${token}` },
        withCredentials: true
      });
      setTrending(response.data?.body || []);
      setHasProducts(response.data?.body?.length > 0);
      setIsLoading(false);
    } catch (error) {
      console.log("Error while loading trending topics", error);
      setHasProducts(false);
      setIsLoading(false);
    }
  };

  const fetchNewListings = async () => {
    const token = localStorage.getItem('token');
    try {
      const response = await axios.get(SERVER_URL + `/api/v1/user/newly-added?category=${selectedCategory}`, {
        headers: { Authorization: `Bearer ${token}` },
        withCredentials: true
      });
      setNewlyAdded(response.data || []);
    } catch (error) {
      console.log(error);
    }
  };

  const handleCategorySelect = (category) => {
    setSelectedCategory(category);
  };

  const handleViewProductsClick = () => {
    navigate('/products');
  };

  const filteredTrending = trending.filter(item => 
    item.product.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const filteredNewlyAdded = newlyAdded.filter(item => 
    item.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const CategoryButton = ({ category }) => (
    <motion.div
      whileHover={{ scale: 1.05 }}
      whileTap={{ scale: 0.95 }}
    >
      <Button 
        style={{ width: '100%', height: '60px', marginBottom: '10px', background: '#f0f2f5' }}
        onClick={() => handleCategorySelect(category.name)}
      >
        {category.name}
      </Button>
    </motion.div>
  );

  const ProductCard = ({ name, description, rentalPrice, productImage, productID }) => (
    <motion.div 
      whileHover={{ scale: 1.05 }}
      style={{
        width: 200,
        background: '#ffffff',
        borderRadius: '10px',
        padding: '10px',
        marginRight: '20px',
        boxShadow: '0 4px 8px rgba(0,0,0,0.1)',
        
      }}
      onClick={() => navigate(`/product/${productID}`)}
    >
      <img src={productImage || '/placeholder.svg'} alt={name} style={{ width: '100%', height: 150, objectFit: 'cover', borderRadius: '10px' }} />
      <h3 style={{ color: '#3B7BF8' }}>{name}</h3>
      <p style={{ color: '#666' }}>{description}</p>
      <p style={{ color: '#3B7BF8', fontWeight: 'bold' }}>${rentalPrice}/hr</p>
    </motion.div>
  );

  

  return (
    <Layout style={{ minHeight: '100vh', background: '#f9f9f9' }}>
      <motion.header
        initial={{ y: -100 }}
        animate={{ y: 0 }}
        transition={{ type: 'spring', stiffness: 300, damping: 30 }}
        style={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          zIndex: 1000,
          background: '#3B7BF8',
          padding: '10px 20px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          
        }}
      >
       <AnimatedHeader/>
      </motion.header>

      <Layout style={{ marginTop: '60px', background: '#f9f9f9' , marginTop :100}}>
        <Sider width={200} style={{ background: '#f9f9f9' }}>
          <motion.div
            initial={{ x: -200 }}
            animate={{ x: 0 }}
            transition={{ type: 'spring', stiffness: 100 }}
          >
            {categories.map((category, index) => (
              <CategoryButton key={index} category={category} />
            ))}
          </motion.div>
        </Sider>
        <Layout style={{ padding: '24px' }}>
          <Content
            style={{
              padding: 24,
              margin: 0,
              minHeight: 280,
              background: '#ffffff',
              borderRadius: '10px',
            }}
          >
            <AnimatePresence mode="wait">
              {isLoading ? (
                <motion.div
                  key="loading"
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  exit={{ opacity: 0 }}
                >
                  <Skeleton active paragraph={{ rows: 4 }} />
                </motion.div>
              ) : selectedCategory ? (
                <motion.div
                  key={selectedCategory}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: -20 }}
                  transition={{ duration: 0.5 }}
                >
                  <h2>{selectedCategory}</h2>
                  {
                    <>
                      <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5, delay: 0.2 }}
                      >
                        <h3><FaChartLine /> Trending Products</h3>
                        <div style={{ display: 'flex', overflowX: 'auto' }}  >
                          {filteredTrending.map((item, index) => (
                            <ProductCard
                              key={index}
                              productID = {item.product.id}
                              name={item.product.name}
                              description={item.product.description}
                              rentalPrice={item.product.rentalPrice}
                              productImage={item.product.productImage}
                             
                            />
                          ))}
                        </div>
                      </motion.div>
                      <motion.div
                        initial={{ opacity: 0, y: 20 }}
                        animate={{ opacity: 1, y: 0 }}
                        transition={{ duration: 0.5, delay: 0.4 }}
                      >
                        <h3><FaBox /> Newly Listed</h3>
                        <div style={{ display: 'flex', overflowX: 'auto' }}>
                          {filteredNewlyAdded.map((item, index) => (
                            <ProductCard
                              key={index}
                              name={item.name}
                              productID={item.id}
                              description={item.description}
                              rentalPrice={item.rentalPrice}
                              productImage={item.productImage}
                            />
                          ))}
                        </div>
                      </motion.div>
                    </>
                   }
                </motion.div>
              ) : (
                <motion.div
                  key="no-selection"
                  initial={{ opacity: 0, scale: 0.8 }}
                  animate={{ opacity: 1, scale: 1 }}
                  transition={{ duration: 0.5 }}
                  style={{ textAlign: 'center', padding: '50px' }}
                >
                  <FaSpinner className="animate-spin" style={{ fontSize: '48px', color: '#3B7BF8' }} />
                  <h2>Select a category to view products</h2>
                </motion.div>
              )}
            </AnimatePresence>
            <motion.div
              style={{ marginTop: '24px', textAlign: 'center' }}
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
            >
              <Button type="primary" onClick={handleViewProductsClick} size="large">
                View All Products
              </Button>
            </motion.div>
          </Content>
        </Layout>
      </Layout>
      <motion.footer
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 1 }}
        style={{
          textAlign: 'center',
          background: '#f0f2f5',
          padding: '24px',
        }}
      >
        HandyShare ©{new Date().getFullYear()} - Share What You Have
      </motion.footer>
    </Layout>
  );
};

export default EnhancedHomeScreen;

