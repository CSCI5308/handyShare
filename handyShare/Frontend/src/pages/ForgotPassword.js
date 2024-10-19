import React, { useState } from 'react';
import InputField from "../components/Input-field.js";
import Button from '../components/Button.js';
import { Link } from "react-router-dom";

export default function ForgotPassword() {
  const [email, setEmail] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle password reset logic here
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#f2f2f2]">
      <div className="w-full max-w-md bg-white rounded-lg shadow-2xl p-8">
        <h1 className="text-3xl font-semibold text-center text-[#333333] mb-6">
          Forgot Password
        </h1>
        <p className="text-center text-[#808080] mb-6">
          Enter your email address and we'll send you a link to reset your password.
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
         
        </form>
      </div>
    </div>
  );
}
