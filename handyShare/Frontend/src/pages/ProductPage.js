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
    <div className="min-h-screen flex flex-col">
      <header className="bg-gray-800 text-white p-4 flex justify-between items-center">
        <div className="text-xl font-bold">handyShare</div>
        <nav className="space-x-4">
          <a href="/lendings" className="hover:underline">Lendings</a>
          <a href="/borrowings" className="hover:underline">Borrowings</a>
          <a href="/profile" className="hover:underline">Profile</a>
        </nav>
      </header>
      <main className="flex-grow p-8 flex">
        <div className="w-1/3 bg-gray-100 p-4">
          <img src="/path/to/image.jpg" alt="Product Image" className="w-full h-64 object-cover" />
          <div className="mt-4">
            <h3 className="text-lg font-bold">Description</h3>
            <p>Product description goes here.</p>
          </div>
          <Review reviews={reviews} />
        </div>
        <div className="w-1/3 px-4">
          <h2 className="text-2xl font-bold">Product Name</h2>
          <p className="text-lg">Price: $20/hour</p>
          <p>Transaction Time: 2 hours</p>
          <div className="mt-4">
            <label>From</label>
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
