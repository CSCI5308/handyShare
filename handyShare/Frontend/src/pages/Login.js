import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import { motion } from 'framer-motion';
import { FiMail, FiLock, FiEye, FiEyeOff, FiRefreshCcw } from 'react-icons/fi';
import { FcGoogle } from 'react-icons/fc';
import { SERVER_URL } from "../constants";

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    const loginData = { email, password };

    try {
      const response = await axios.post(`${SERVER_URL}/api/v1/all/login`, loginData);

      if (response.status === 200) {
        const { token, role, userId } = response.data; // Extract userId

        if (token === "Bad credentials!") {
          setError('Invalid email or password. Please try again.');
          return;
        }

        // Save token, role, and userId to localStorage
        localStorage.setItem('token', token);
        localStorage.setItem('role', role);
        localStorage.setItem('userId', userId);

        navigate(role === "admin" ? '/admin' : '/homepage');
      } else {
        setError('Login failed. Please check your email and password.');
      }
    } catch (error) {
      if (error.response && error.response.status === 401) {
        setError('Invalid email or password. Please try again.');
      } else {
        setError('An error occurred during login. Please try again.');
      }
    } finally {
      setIsLoading(false);
    }
  };

  const handleGoogleLogin = async () => {
    try {
      window.location.href = `${SERVER_URL}/oauth2/authorization/google`;
    } catch (err) {
      setError('Failed to initiate Google login. Please try again.');
    }
  };

  const buttonVariants = {
    hover: { scale: 1.05 },
    tap: { scale: 0.95 },
  };

  const LoadingScreen = () => (
    <div className="fixed inset-0 bg-white bg-opacity-80 z-50 flex items-center justify-center">
      <motion.div
        animate={{
          rotate: 360,
        }}
        transition={{
          duration: 1,
          repeat: Infinity,
          ease: "linear",
        }}
      >
        <FiRefreshCcw className="text-blue-300 text-4xl" />
      </motion.div>
    </div>
  );

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 via-blue-100 to-blue-200">
      {isLoading && <LoadingScreen />}
      
      <motion.div 
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="bg-white p-12 rounded-2xl shadow-2xl flex w-full max-w-5xl mx-4 relative overflow-hidden"
      >
        <motion.div 
          className="w-1/2 pr-12 relative z-10"
          initial={{ x: -50, opacity: 0 }}
          animate={{ x: 0, opacity: 1 }}
          transition={{ duration: 0.8, delay: 0.2 }}
        >
          <motion.div 
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: 0.3 }}
            className="mb-10"
          >
            <h1 className="font-display text-4xl font-bold text-gray-900 mb-3">
              Welcome to HandyShare
            </h1>
            <p className="text-gray-600 text-lg">
              Your premium lending and borrowing marketplace
            </p>
          </motion.div>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-2">
              <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                Email Address
              </label>
              <div className="relative group">
                <FiMail className="absolute top-1/2 left-4 transform -translate-y-1/2 text-gray-400 group-hover:text-blue-400 transition-colors duration-200" />
                <input
                  id="email"
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  className="w-full h-12 pl-12 pr-4 border-2 border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-300 focus:border-transparent transition-all duration-200 bg-gray-50 hover:bg-white"
                  placeholder="Enter your email"
                />
              </div>
            </div>

            <div className="space-y-2">
              <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                Password
              </label>
              <div className="relative group">
                <FiLock className="absolute top-1/2 left-4 transform -translate-y-1/2 text-gray-400 group-hover:text-blue-400 transition-colors duration-200" />
                <input
                  id="password"
                  type={showPassword ? 'text' : 'password'}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  className="w-full h-12 pl-12 pr-12 border-2 border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-300 focus:border-transparent transition-all duration-200 bg-gray-50 hover:bg-white"
                  placeholder="Enter your password"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-4 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-blue-400 transition-colors duration-200"
                  aria-label={showPassword ? "Hide password" : "Show password"}
                >
                  {showPassword ? <FiEyeOff size={20} /> : <FiEye size={20} />}
                </button>
              </div>
            </div>

            {error && (
              <motion.div
                initial={{ opacity: 0, y: -10 }}
                animate={{ opacity: 1, y: 0 }}
                className="bg-red-50 text-red-500 text-sm p-4 rounded-lg"
                role="alert"
              >
                {error}
              </motion.div>
            )}

            <motion.button
              type="submit"
              className="w-full h-12 bg-blue-400 text-white rounded-xl hover:bg-blue-500 transition-all duration-200 font-medium text-sm focus:outline-none focus:ring-2 focus:ring-blue-300 focus:ring-offset-2"
              variants={buttonVariants}
              whileHover="hover"
              whileTap="tap"
            >
              Sign In
            </motion.button>

            <div className="relative my-8">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-gray-200"></div>
              </div>
              <div className="relative flex justify-center">
                <span className="px-4 text-sm text-gray-500 bg-white">
                  Or continue with
                </span>
              </div>
            </div>

            <motion.button
              type="button"
              onClick={handleGoogleLogin}
              className="w-full h-12 bg-white border-2 border-gray-200 rounded-xl hover:bg-gray-50 transition-all duration-200 flex items-center justify-center gap-3 text-sm font-medium text-gray-700"
              variants={buttonVariants}
              whileHover="hover"
              whileTap="tap"
            >
              <FcGoogle size={20} />
              Sign in with Google
            </motion.button>
          </form>

          <div className="mt-8 text-center space-y-4">
            <motion.div
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
            >
              <Link
                to="/forgot-password"
                className="text-sm text-blue-600 hover:text-blue-700 transition-colors duration-200"
              >
                Forgot your password?
              </Link>
            </motion.div>
            <p className="text-sm text-gray-600">
              Don't have an account?{' '}
              <motion.span
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
              >
                <Link
                  to="/signup"
                  className="text-blue-600 hover:text-blue-700 font-medium transition-colors duration-200"
                >
                  Sign up
                </Link>
              </motion.span>
            </p>
          </div>
        </motion.div>

        <div className="w-1/2 pl-12 border-l border-gray-100 relative flex items-center justify-center">
          <div className="absolute inset-0 bg-gradient-to-br from-blue-100 to-blue-200 opacity-50"></div>
          <motion.div
            className="relative z-10 flex flex-col items-center justify-center space-y-8"
            initial={{ opacity: 0, scale: 0.5 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.8, delay: 0.5 }}
          >
            <motion.img 
              src="/Assets/Logo.png" 
              alt="HandyShare Logo" 
              className="w-48 h-auto"
              whileHover={{ scale: 1.05 }}
              transition={{ type: "spring", stiffness: 300, damping: 10 }}
            />
            <div className="text-center">
              <h2 className="text-2xl font-bold text-gray-900 mb-4">
                Premium Lending and Borrowing Marketplace
              </h2>
              <p className="text-gray-600">
                Share and access high-quality items with our trusted community
              </p>
            </div>
            <motion.div
              className="bg-white bg-opacity-80 p-6 rounded-xl text-center max-w-sm"
              initial={{ y: 20, opacity: 0 }}
              animate={{ y: 0, opacity: 1 }}
              transition={{ delay: 0.8 }}
            >
              <blockquote className="text-blue-700 italic">
                "The best marketplace for lending and borrowing premium items. Highly recommended!"
              </blockquote>
              <p className="text-sm font-medium text-blue-500 mt-4">
                — Featured in TechDaily
              </p>
            </motion.div>
          </motion.div>
        </div>
      </motion.div>
    </div>
  );
}
