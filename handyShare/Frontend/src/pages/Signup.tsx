import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Import useNavigate
import { InputField } from "../components/Input-field.tsx";
import { Button } from '../components/Button.tsx';

export default function Signup() {
  const navigate = useNavigate(); // Initialize useNavigate
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [acceptTerms, setAcceptTerms] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Handle signup logic here
  };

  const handleGoogleSignup = () => {
    // Handle Google signup logic here
  };

  // Function to navigate to the login page
  const goToLogin = () => {
    navigate('/login'); // Navigate to the login page
  };

  return (
    <div
      className="min-h-screen flex items-center justify-center"
      style={{
        backgroundColor: '#f2f2f2', // Light grey background
      }}
    >
      <div className="flex w-full max-w-6xl bg-white rounded-lg shadow-2xl overflow-hidden">
        {/* Left Side - Greyish Background */}
        <div
          className="w-2/5 flex items-center justify-center"
          style={{
            backgroundColor: '#d9d9d9', // Greyish tone for the left side
          }}
        >
          {/* Placeholder for content if needed */}
          <p className="text-[#4f4f4f] font-semibold text-lg">
            Welcome to handyShare!
          </p>
        </div>

        {/* Right Side - Signup Form */}
        <div className="w-3/5 p-10">
          <h1 className="text-4xl font-semibold text-left text-[#333333]">
            Create an Account
          </h1>
          <p className="text-left text-[#808080] mt-2 mb-8">
            Join handyShare to explore unique rental opportunities!
          </p>
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
              label="Create Password"
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />

            <div className="flex items-center">
              <input
                type="checkbox"
                id="terms"
                checked={acceptTerms}
                onChange={(e) => setAcceptTerms(e.target.checked)}
                className="h-5 w-5 text-[#333333] border-gray-300 rounded"
                required
              />
              <label htmlFor="terms" className="ml-2 text-sm text-[#4f4f4f]">
                I agree to the{' '}
                <a
                  href="/terms"
                  className="text-[#333333] hover:underline"
                >
                  Terms of Service
                </a>{' '}
                and{' '}
                <a
                  href="/privacy"
                  className="text-[#333333] hover:underline"
                >
                  Privacy Policy
                </a>
              </label>
            </div>

            <Button type="submit" className="w-full bg-[#333333] text-white py-3 rounded-lg">
              Sign Up
            </Button>
          </form>

          <div className="mt-8 text-center">
            <p className="text-sm text-gray-500">Or continue with</p>
            <Button
              variant="secondary"
              onClick={handleGoogleSignup}
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
              Sign up with Google
            </Button>
          </div>

          <p className="mt-6 text-sm text-left text-[#4f4f4f]">
            Already have an account?{' '}
            <span
              onClick={goToLogin} // Use the goToLogin function
              className="font-medium text-[#333333] hover:underline cursor-pointer"
            >
              Log in here
            </span>
          </p>
        </div>
      </div>
    </div>
  );
}
