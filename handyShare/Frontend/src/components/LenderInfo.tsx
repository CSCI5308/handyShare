import React from 'react';

interface LenderInfoProps {
  profilePicture: string;
  name: string;
  rating: number;
  location: string;
}

export function LenderInfo({ profilePicture, name, rating, location }: LenderInfoProps) {
  return (
    <div className="p-4 flex items-center">
      <img src={profilePicture} alt={`${name}'s profile`} className="w-16 h-16 rounded-full" />
      <div className="ml-4">
        <h3 className="text-lg font-bold">{name}</h3>
        <p className="text-sm text-gray-500">Rating: {rating} / 5</p>
        <p className="text-sm text-gray-500">{location}</p>
      </div>
    </div>
  );
}
