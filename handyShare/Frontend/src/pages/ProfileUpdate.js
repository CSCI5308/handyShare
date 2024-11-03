import React, { useState } from 'react';
import ProfileHeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar.js';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { notification } from 'antd'; // Import notification from Ant Design
import { SERVER_URL } from '../constants.js';

const ProfileUpdate = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { userDetails } = location.state || {};

  const [profile, setProfile] = useState({
    name: userDetails ? userDetails.name : '',
    profileImage: null,
    address: userDetails ? userDetails.address : '',
    pincode: userDetails ? userDetails.pincode : '',
    phone: userDetails ? userDetails.phone : '',
    email: userDetails ? userDetails.email : 'user@example.com',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProfile({
      ...profile,
      [name]: value,
    });
  };

  const handleImageChange = (e) => {
    setProfile({
      ...profile,
      profileImage: e.target.files[0],
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append('name', profile.name);
    formData.append('address', profile.address);
    formData.append('pincode', profile.pincode);
    formData.append('phone', profile.phone);
    formData.append('email', profile.email);
    if (profile.profileImage) {
      formData.append('profileImage', profile.profileImage);
    }

    try {
      const token = localStorage.getItem('token');
      const response = await axios.put(`${SERVER_URL}/api/v1/user/update-profile`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': `Bearer ${token}`,
        },
      });

      // Display success notification
      notification.success({
        message: 'Profile Updated',
        description: 'Your profile has been updated successfully!',
      });

      navigate('/profile'); // Redirect after successful update
    } catch (error) {
      console.error('Error updating profile:', error);
      // Display error notification
      notification.error({
        message: 'Update Failed',
        description: 'Failed to update profile. Please try again later.',
      });
    }
  };

  return (
    <div>
      <ProfileHeaderBar />
      <div className="container mx-auto p-6 max-w-lg bg-white shadow-lg rounded-lg mt-6">
        <h2 className="text-2xl font-bold mb-6">Update Profile</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          {/* Form fields */}
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

          <div>
            <label htmlFor="profileImage" className="block text-sm font-medium text-gray-700">
              Profile Image
            </label>
            <div className="flex items-center">
              <input
                type="file"
                name="profileImage"
                id="profileImage"
                onChange={handleImageChange}
                className="mt-1 block w-full text-gray-700"
                accept="image/*"
              />
              {/* Display current profile image */}
              <img
                src={profile.profileImage ? URL.createObjectURL(profile.profileImage) : userDetails.imageData}
                alt="Profile"
                className="ml-4 h-12 w-12 rounded-full border-2 border-gray-300"
              />
            </div>
          </div>

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
            />
          </div>

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
            />
          </div>

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
            />
          </div>

          {/* Back and Update Profile buttons */}
          <div className="flex justify-between">
            <button
              type="button"
              onClick={() => navigate('/profile')}
              className="bg-gray-500 hover:bg-gray-600 text-white p-2 rounded-md"
            >
              Back
            </button>
            <button
              type="submit"
              className="bg-blue-600 hover:bg-blue-700 text-white p-2 rounded-md"
            >
              Update Profile
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ProfileUpdate;
