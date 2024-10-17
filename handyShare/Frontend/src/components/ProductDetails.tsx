import React from 'react';

interface ProductDetailsProps {
  name: string;
  price: number;
  transactionTime: string;
}

export function ProductDetails({ name, price, transactionTime }: ProductDetailsProps) {
  return (
    <div className="p-4">
      <h2 className="text-2xl font-bold">{name}</h2>
      <p className="text-lg text-gray-700">Price: ${price}/hour</p>
      <p className="text-sm text-gray-500">Transaction Time: {transactionTime}</p>
    </div>
  );
}

