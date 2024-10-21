// ProductCard.js
import React from 'react';

function ProductCard({ name, description, rentalPrice }) {
  return (
    <div 
      style={{
        minHeight: '100%', 
        width: "25%", 
        background: '#3B7BF8', 
        borderRadius: '10px', 
        justifyContent: 'space-between', 
        alignItems: 'center',
        display: 'flex', 
        flexShrink: 0, 
        flexDirection: 'column',
        padding: '10px',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        color: 'white'
      }}
    >
      <div style={{
        height: '250px', 
        width: '100%', 
        background: '#d9d9d9', 
        borderRadius: '10px', 
        marginBottom: '10px',
        display: 'flex', 
        justifyContent: 'center', 
        alignItems: 'center',
      }}>
        <span style={{ color: '#3B7BF8', fontSize: '16px' }}>Image Placeholder</span>
      </div>

      <div style={{ fontWeight: 'bold', fontSize: '20px', marginBottom: '5px', textAlign: 'center' }}>
        {name}
      </div>

      <p style={{ textAlign: 'center', fontSize: '14px', fontWeight: 'normal', marginBottom: '5px' }}>
        {description}
      </p>

      <div style={{ fontSize: '18px', textAlign: 'center' }}>
        ${rentalPrice}/hr
      </div>
    </div>
  );
}

export default ProductCard;
