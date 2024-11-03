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
import RentSummaryPage from './pages/RentSummaryPage.js';
import OAuth2RedirectHandler from "../src/components/ProfileUpdatePage/OAuth2RedirectHandler.js"

function App() {
  return (
    <Routes>
      <Route path="/" element={<Signup />} /> {/* Add a route for the root path */}
      <Route path="/signup" element={<Signup />} />
      <Route path="/login" element={<Login />} />
      <Route path="/forgot-password" element={<ForgotPassword />} />
      <Route path = "/homepage" element={<HomeScreen/>}/>
      <Route path="/profile" element={<Profile/>} />
      <Route path = "/profile-update" element={<ProfileUpdate/>}/>
      <Route path="/admin" element={<AdminDashboard/>} />
      <Route path="/product/:id" element={<ProductPage />} />
      <Route path="/lend" element={<LendPage />} />
      <Route path="/products" element={<ProductsListPage />} />
      <Route path="/payment" element={<Payment/>} />
      <Route path="/feedback" element={<Feedback/>} />
      <Route path="/rent-summary" element={<RentSummaryPage/>} />
      <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler/>}/>

    </Routes>
  );
}

export default App;
