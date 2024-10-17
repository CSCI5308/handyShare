import React from 'react';

export function LenderInfo({ profilePicture, name, rating, location }) {
  return (
    <div>
      <img src={profilePicture} alt={`${name}'s profile`} className="profile-picture" />
      <h3>{name}</h3>
      <p>Rating: {rating}</p>
      <p>Location: {location}</p>
    </div>
  );
}
