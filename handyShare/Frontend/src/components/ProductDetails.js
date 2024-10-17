import React from 'react';

export function ProductDetails({ name, price, transactionTime }) {
  return (
    <div>
      <h2>{name}</h2>
      <p>Price: ${price}</p>
      <p>Transaction Time: {transactionTime}</p>
    </div>
  );
}
