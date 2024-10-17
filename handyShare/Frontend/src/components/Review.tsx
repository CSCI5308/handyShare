import React from 'react';

interface ReviewProps {
  reviews: { user: string; comment: string }[];
}

export function Review({ reviews }: ReviewProps) {
  return (
    <div className="p-4">
      <h3 className="text-lg font-bold">Reviews</h3>
      <ul className="space-y-2">
        {reviews.map((review, index) => (
          <li key={index} className="border-b border-gray-200 pb-2">
            <p className="text-sm font-semibold">{review.user}</p>
            <p className="text-sm text-gray-600">{review.comment}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}
