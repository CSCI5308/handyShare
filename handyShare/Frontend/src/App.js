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

import ProtectedRoute from './components/ProtectedRoute.js';
import { AuthProvider } from './context/AuthContext.js';

import BorrowingPage from './pages/BorrowingPage.js';

import ChangePasswordPage from './pages/ChangePassword.js';


function App() {
  return (
    <AuthProvider>
    <Routes>
      <Route path="/" element={<Signup />} /> {/* Add a route for the root path */}
      <Route path="/signup" element={<Signup />} />
      <Route path="/login" element={<Login />} /> 
      <Route path="/forgot-password" element={<ForgotPassword />} />
      <Route path = "/homepage" element={<ProtectedRoute><HomeScreen/></ProtectedRoute>}/>
      <Route path="/profile" element={<ProtectedRoute><Profile/></ProtectedRoute>} />
      <Route path = "/profile-update" element={<ProtectedRoute><ProfileUpdate/></ProtectedRoute>}/>
      <Route path="/admin" element={<AdminDashboard/>} />

      <Route path="/product/:id" element={<ProtectedRoute><ProductPage /></ProtectedRoute>} />
      <Route path="/lend" element={<ProtectedRoute><LendPage /></ProtectedRoute>} />
      <Route path="/products" element={<ProtectedRoute><ProductsListPage /></ProtectedRoute>} />
      <Route path="/payment" element={<ProtectedRoute><Payment/></ProtectedRoute>} />
      <Route path="/feedback" element={<ProtectedRoute><Feedback/></ProtectedRoute>} />
      <Route path="/rent-summary" element={<ProtectedRoute><RentSummaryPage/></ProtectedRoute>} />
      
      <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler/>}/>

      <Route path="/borrow" element={<ProtectedRoute><BorrowingPage/></ProtectedRoute>} />
      <Route path="/change-password" element={<ChangePasswordPage/>}/>

    </Routes>
    </AuthProvider>
  );
}

export default App;
