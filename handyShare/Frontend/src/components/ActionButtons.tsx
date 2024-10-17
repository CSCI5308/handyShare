import React from 'react';

export function ActionButtons() {
  const handleShare = () => {
    // Handle share logic here
  };

  const handleAddForLater = () => {
    // Handle add for later logic here
  };

  return (
    <div className="p-4 flex space-x-4">
      <button onClick={handleShare} className="bg-blue-500 text-white py-2 px-4 rounded-lg">
        Share
      </button>
      <button onClick={handleAddForLater} className="bg-gray-500 text-white py-2 px-4 rounded-lg">
        Add for later
      </button>
    </div>
  );
}
