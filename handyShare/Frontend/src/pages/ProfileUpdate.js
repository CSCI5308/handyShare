import React, { useState } from 'react';
import ProfileHeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar.js';
import { useLocation } from 'react-router-dom'; 

const ProfileUpdate = () => {
  const location = useLocation(); 
  const { userDetails } = location.state || {}; // Extract user details from state

  // State for form fields
  const [profile, setProfile] = useState({
    name: userDetails ? userDetails.name : '',
    profileImage: null,
    address: userDetails ? userDetails.address : '',
    pincode: userDetails ? userDetails.pincode : '',
    phone: userDetails ? userDetails.phone : '',
    password: '',
    email: userDetails ? userDetails.email : 'user@example.com', // Email remains non-editable
  });

  // Handle input change
  const handleChange = (e) => {
    const { name, value } = e.target;
    setProfile({
      ...profile,
      [name]: value,
    });
  };

  // Handle file input for profile image
  const handleImageChange = (e) => {
    setProfile({
      ...profile,
      profileImage: e.target.files[0],
    });
  };

  // Simulate form submission
  const handleSubmit = (e) => {
    e.preventDefault();

    // Simulate profile update logic
    console.log('Updated Profile:', profile);

    // You can show a success message or alert
    alert('Profile updated successfully!');
  };

  return (
    <div>
      <ProfileHeaderBar />
      <div className="container mx-auto p-6 max-w-lg bg-white shadow-lg rounded-lg mt-6">
        <h2 className="text-2xl font-bold mb-6">Update Profile</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Name */}
          <div>
            <label htmlFor="name" className="block text-sm font-medium text-gray-700">
              Name
            </label>
            <input
              type="text"
              name="name"
              id="name"
              value={profile.name}
              onChange={handleChange}
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
              required
            />
          </div>

          {/* Profile Image */}
          <div>
            <label htmlFor="profileImage" className="block text-sm font-medium text-gray-700">
              Profile Image
            </label>
            <input
              type="file"
              name="profileImage"
              id="profileImage"
              onChange={handleImageChange}
              className="mt-1 block w-full text-gray-700"
              accept="image/*"
            />
          </div>

          {/* Email (Non-editable) */}
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-700">
              Email
            </label>
            <input
              type="text"
              name="email"
              id="email"
              value={profile.email}
              readOnly
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md bg-gray-200"
            />
          </div>

          {/* Address */}
          <div>
            <label htmlFor="address" className="block text-sm font-medium text-gray-700">
              Address
            </label>
            <input
              type="text"
              name="address"
              id="address"
              value={profile.address}
              onChange={handleChange}
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
              required
            />
          </div>

          {/* Pincode */}
          <div>
            <label htmlFor="pincode" className="block text-sm font-medium text-gray-700">
              Pincode
            </label>
            <input
              type="text"
              name="pincode"
              id="pincode"
              value={profile.pincode}
              onChange={handleChange}
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
              required
            />
          </div>

          {/* Phone */}
          <div>
            <label htmlFor="phone" className="block text-sm font-medium text-gray-700">
              Phone
            </label>
            <input
              type="text"
              name="phone"
              id="phone"
              value={profile.phone}
              onChange={handleChange}
              className="mt-1 block w-full p-2 border border-gray-300 rounded-md"
              required
            />
          </div>

          {/* Submit Button */}
          <button type="submit" className="w-full py-2 px-4 bg-blue-600 text-white rounded-md">
            Update Profile
          </button>
        </form>
      </div>
    </div>
  );
};

export default ProfileUpdate;
