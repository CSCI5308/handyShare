import React from 'react';
import { Routes, Route } from 'react-router-dom'; 
import Signup from './pages/Signup.tsx'; 
import Login from './pages/Login.tsx';
import ForgotPassword from './pages/ForgotPassword.tsx';
import ProductPage from './pages/ProductPage.js';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Signup />} /> {/* Add a route for the root path */}
      <Route path="/signup" element={<Signup />} />
      <Route path="/login" element={<Login />} />
      <Route path="/forgot-password" element={<ForgotPassword />} />
      <Route path="/product" element={<ProductPage />} />
    </Routes>
  );
}

export default App;
