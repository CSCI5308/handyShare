import React from 'react';

interface ProductImageProps {
  imageUrl: string;
  altText: string;
}

export function ProductImage({ imageUrl, altText }: ProductImageProps) {
  return (
    <div className="w-full h-64 bg-gray-200 flex items-center justify-center">
      <img src={imageUrl} alt={altText} className="max-h-full max-w-full" />
    </div>
  );
}
