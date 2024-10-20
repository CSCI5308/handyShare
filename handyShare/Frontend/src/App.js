import React from 'react';
import { Routes, Route } from 'react-router-dom'; 
import Signup from './pages/Signup.js'; 
import Login from './pages/Login.js';
import ForgotPassword from './pages/ForgotPassword.js';
import HomeScreen from './pages/HomeScreen.js'; 
import Profile from './pages/Profile';
import ProfileUpdate from './pages/ProfileUpdate';
import AdminDashboard from './pages/AdminDashboard.js';
import ProductPage from './pages/ProductPage.js';
import LendPage from './pages/LendPage.js';
import ProductsListPage from './pages/ProductsList.js';
import Payment from './pages/Payment.js';
import '@fortawesome/fontawesome-free/css/all.min.css';
import Feedback from './pages/Feedback.js';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Signup />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/login" element={<Login />} />
      <Route path="/forgot-password" element={<ForgotPassword />} />

  );
}

export default App;
