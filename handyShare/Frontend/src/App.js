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

function App() {
  return (
    <Routes>
      <Route path="/" element={<Signup />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/login" element={<Login />} />
      <Route path="/forgot-password" element={<ForgotPassword />} />
      <Route path="/homepage" element={<HomeScreen />} /> 
      <Route path="/profile" element={<Profile />} />
      <Route path="/profile-update" element={<ProfileUpdate />} />
      <Route path="/admin" element={<AdminDashboard />} />
      <Route path="/product-page" element={<ProductPage />} />
    </Routes>
  );
}

export default App;
