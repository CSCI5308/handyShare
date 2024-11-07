import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';
import InputField from "../components/Input-field.js";
import Button from '../components/Button.js';
import { EyeInvisibleOutlined, EyeTwoTone } from '@ant-design/icons';
import { SERVER_URL } from "../constants.js";

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  // Check URL for token after OAuth login
  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const token = params.get("token");
    const role = params.get("role"); // Optional if role is also passed

    if (token) {
      // Save token and role in localStorage
      localStorage.setItem("token", token);
      if (role) localStorage.setItem("role", role);

      // Redirect to the homepage or admin page based on role
      if (role === "admin") {
        navigate('/admin');
      } else {
        navigate('/homepage');
      }
    }
  }, [location, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const loginData = { email, password };

    try {
      const response = await axios.post(SERVER_URL + '/api/v1/all/login', loginData);
      if (response.status === 200) {
        const { token, role } = response.data;

        if (token === "Bad credentials!") {
          setError('Invalid email or password. Please try again.');
          return;
        }

        // Save token and role in localStorage
        localStorage.setItem('token', token);
        localStorage.setItem('role', role);

        // Redirect to the homepage or admin page based on role
        if (role === "admin") {
          navigate('/admin');
        } else {
          navigate('/homepage');
        }
      } else {
        setError('Login failed. Please check your email and password.');
      }
    } catch (error) {
      if (error.response && error.response.status === 401) {
        setError('Invalid email or password. Please try again.');
      } else {
        setError('An error occurred during login. Please try again.');
      }
    }
  };

  const handleGoogleLogin = () => {
    window.location.href = SERVER_URL + "/oauth2/authorization/google";
  };

  return (
    <div className="min-h-screen flex items-center justify-center" style={{ backgroundColor: '#f2f2f2' }}>
      <div className="flex w-full max-w-6xl bg-white rounded-lg shadow-2xl overflow-hidden">
        <div className="w-2/5 flex items-center justify-center" style={{ backgroundColor: '#fff' }}>
          <img src="Assets/Logo.png" alt="Logo" />
        </div>
        <div className="w-3/5 p-10">
          <h1 className="text-4xl font-semibold text-left text-[#333333]">Log In</h1>
          <p className="text-left text-[#808080] mt-2 mb-8">Access your handyShare account and manage rentals easily!</p>
          {error && <p className="text-red-500 mb-4">{error}</p>}
          <form onSubmit={handleSubmit} className="space-y-6">
            <InputField label="Email Address" type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
            <div className="relative">
              <InputField label="Password" type={showPassword ? 'text' : 'password'} id="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
              <span className="absolute right-2 top-7 cursor-pointer" onClick={() => setShowPassword(!showPassword)}>
                {showPassword ? <EyeTwoTone /> : <EyeInvisibleOutlined />}
              </span>
            </div>
            <div className="text-right">
              <a to="/forgot-password" className="text-sm text-[#0295db] hover:underline">Forgot Password?</a>
            </div>
            <Button type="submit" className="w-full bg-[#333333] text-white py-3 rounded-lg">Log In</Button>
          </form>

          <div className="mt-8 text-center">
            <p className="text-sm text-gray-500">Or continue with</p>
            <Button onClick={handleGoogleLogin} className="flex items-center justify-center w-full border border-[#333333] text-[#333333] mt-4 py-3 rounded-lg">
              <svg className="w-6 h-6 mr-2" viewBox="0 0 24 24">
                <path d="M12.24 10.285V14.4h6.806c-.275 1.765-2.056 5.174-6.806 5.174-4.095 0-7.439-3.389-7.439-7.574s3.345-7.574 7.439-7.574c2.33 0 3.891.989 4.785 1.849l3.254-3.138C18.189 1.186 15.479 0 12.24 0c-6.635 0-12 5.365-12 12s5.365 12 12 12c6.926 0 11.52-4.869 11.52-11.726 0-.788-.085-1.39-.189-1.989H12.24z" fill="#4285F4" />
              </svg>
              Log in with Google
            </Button>
          </div>

          <p className="mt-6 text-sm text-left text-[#4f4f4f]">
            Don't have an account yet? <a to="/signup" className="font-medium text-[#333333] hover:underline">Sign up here</a>
          </p>
        </div>
      </div>
    </div>
  );
}
