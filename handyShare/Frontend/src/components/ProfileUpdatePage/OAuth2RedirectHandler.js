import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function OAuth2RedirectHandler() {
  const navigate = useNavigate();
  const [isTokenStored, setIsTokenStored] = useState(false);

  useEffect(() => {
    const searchParams = new URLSearchParams(window.location.search);
    const token = searchParams.get('token');

    if (token) {
      // Store the JWT token in localStorage
      localStorage.setItem('token', token);

      if (localStorage.getItem('token')) {
        setIsTokenStored(true);  // Set state after storing the token
      }
    } else {
      navigate('/login');  // Redirect to login page if no token is found
    }
  }, [navigate]);

  useEffect(() => {
    if (isTokenStored) {
      navigate('/homepage');  // Navigate to homepage after storing the token
    }
  }, [isTokenStored, navigate]);

  return <div>Redirecting...</div>;
}
