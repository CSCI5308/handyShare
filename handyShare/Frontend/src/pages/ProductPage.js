import React from 'react';
import { Header } from '../components/Header.js';
import { ProductImage } from '../components/ProductImage.js';
import { ProductDetails } from '../components/ProductDetails.js';
import { Transaction } from '../components/Transaction.js';
import { LenderInfo } from '../components/LenderInfo.js';
import { Review } from '../components/Review.js';
import { ActionButtons } from '../components/ActionButtons.js';

export default function ProductPage() {
  const reviews = [
    { user: 'Alice', comment: 'Great product!' },
    { user: 'Bob', comment: 'Very useful and affordable.' },
  ];

  return (
    <div>
      <Header />
      <main className="p-8 flex">
        <div className="w-1/3">
          <ProductImage imageUrl="/path/to/image.jpg" altText="Product Image" />
          <div className="mt-4">
            <h3 className="text-lg font-bold">Description</h3>
            <p>Product description goes here.</p>
          </div>
          <Review reviews={reviews} />
        </div>
        <div className="w-1/3 px-4">
          <ProductDetails name="Product Name" price={20} transactionTime="2 hours" />
          <Transaction />
          <div className="flex space-x-4 mt-4">
            <ActionButtons />
          </div>
        </div>
        <div className="w-1/3">
          <LenderInfo profilePicture="/path/to/profile.jpg" name="John Doe" rating={4.5} location="New York, NY" />
          <div className="mt-4">
            <h3 className="text-lg font-bold">Lender's Location</h3>
            <p>Google map location goes here.</p>
          </div>
        </div>
      </main>
    </div>
  );
}
