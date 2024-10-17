import React, { useState } from 'react';

export function Transaction() {
  const [fromDate, setFromDate] = useState('');
  const [toDate, setToDate] = useState('');

  const handleTransaction = () => {
    // Handle transaction logic here
  };

  return (
    <div className="p-4">
      <div className="flex space-x-4">
        <div>
          <label htmlFor="fromDate" className="block text-sm font-medium text-gray-700">
            From
          </label>
          <input
            type="date"
            id="fromDate"
            value={fromDate}
            onChange={(e) => setFromDate(e.target.value)}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm"
          />
        </div>
        <div>
          <label htmlFor="toDate" className="block text-sm font-medium text-gray-700">
            To
          </label>
          <input
            type="date"
            id="toDate"
            value={toDate}
            onChange={(e) => setToDate(e.target.value)}
            className="mt-1 block w-full border-gray-300 rounded-md shadow-sm"
          />
        </div>
      </div>
      <button
        onClick={handleTransaction}
        className="mt-4 w-full bg-blue-500 text-white py-2 rounded-lg"
      >
        Transact
      </button>
    </div>
  );
}

