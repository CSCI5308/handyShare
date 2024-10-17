import React from 'react';

export function ProductImage({ imageUrl, altText }) {
  return <img src={imageUrl} alt={altText} className="product-image" />;
}
