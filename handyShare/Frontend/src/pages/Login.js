import React, { useState } from 'react';
import InputField from "../components/Input-field.js";
import Button from '../components/Button.js';
import { Link, useNavigate } from 'react-router-dom';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null); // To handle login errors
  const navigate = useNavigate(); // To redirect after login

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Prepare login data
    const loginData = new URLSearchParams({
      'username': email,
      'password': password,
    });

    try {
      const response = await fetch('http://localhost:8080/api/v1/all/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',  // Use this for form login
        },
        body: loginData,
        credentials: 'include', // Ensures cookies are sent for session-based auth
      });

      if (response.ok) {
        // On successful login
        console.log('Login successful');
        navigate('/dashboard'); // Redirect to the dashboard after successful login
      } else {
        // Handle login error
        setError('Login failed. Please check your email and password.');
      }
    } catch (error) {
      setError('An error occurred during login. Please try again.');
    }
  };

  const handleGoogleLogin = () => {
    // Handle Google login logic here
  };

  return (
    <div
      className="min-h-screen flex items-center justify-center"
      style={{
        backgroundColor: '#f2f2f2',
      }}
    >
      <div className="flex w-full max-w-6xl bg-white rounded-lg shadow-2xl overflow-hidden">
        <div
          className="w-2/5 flex items-center justify-center"
          style={{
            backgroundColor: '#fff',
          }}
        >
          <img src="Assets/Logo.png" alt="Logo" />
        </div>

        <div className="w-3/5 p-10">
          <h1 className="text-4xl font-semibold text-left text-[#333333]">
            Log In
          </h1>
          <p className="text-left text-[#808080] mt-2 mb-8">
            Access your handyShare account and manage rentals easily!
          </p>
          {error && <p className="text-red-500 mb-4">{error}</p>}
          <form onSubmit={handleSubmit} className="space-y-6">
            <InputField
              label="Email Address"
              type="email"
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <InputField
              label="Password"
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            <div className="text-right">
              <Link to="/forgot-password" className="text-sm text-[#0295db] hover:underline">
                Forgot Password?
              </Link>
            </div>

            <Button type="submit" className="w-full bg-[#333333] text-white py-3 rounded-lg">
              Log In
            </Button>
          </form>

          <div className="mt-8 text-center">
            <p className="text-sm text-gray-500">Or continue with</p>
            <Button
              variant="secondary"
              onClick={handleGoogleLogin}
              className="flex items-center justify-center w-full border border-[#333333] text-[#333333] mt-4 py-3 rounded-lg"
            >
              <svg
                className="w-6 h-6 mr-2"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  d="M12.24 10.285V14.4h6.806c-.275 1.765-2.056 5.174-6.806 5.174-4.095 0-7.439-3.389-7.439-7.574s3.345-7.574 7.439-7.574c2.33 0 3.891.989 4.785 1.849l3.254-3.138C18.189 1.186 15.479 0 12.24 0c-6.635 0-12 5.365-12 12s5.365 12 12 12c6.926 0 11.52-4.869 11.52-11.726 0-.788-.085-1.39-.189-1.989H12.24z"
                  fill="#4285F4"
                />
              </svg>
              Log in with Google
            </Button>
          </div>

          <p className="mt-6 text-sm text-left text-[#4f4f4f]">
            Don't have an account yet?{' '}
            <Link to="/signup" className="font-medium text-[#333333] hover:underline">
              Sign up here
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
