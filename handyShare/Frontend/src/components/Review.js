import React from 'react';

export function Review({ reviews }) {
  return (
    <div>
      <h3>Reviews</h3>
      <ul>
        {reviews.map((review, index) => (
          <li key={index}>
            <strong>{review.user}:</strong> {review.comment}
          </li>
        ))}
      </ul>
    </div>
  );
}
