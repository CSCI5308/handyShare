// ContentHomeScreen.js
import React, { useEffect, useState } from 'react';

import axios from 'axios';
import { SERVER_URL } from '../../constants';
import ProductCard from './ProductCard.js'; // Importing the reusable card component

function ContentHomeScreen({ category }) {
  const [trending, setTrending] = useState([]);
  const [newlyAdded, setNewlyAdded] = useState([{}]);

  useEffect(() => {
    const fetchTrending = async () => {
      try {
        const response = await axios.get(SERVER_URL + `/api/v1/all/getTrendingByCategory?category=${category}`);
        setTrending(response.data?.body || []);
      } catch (error) {
        console.log("Error while loading trending topics", error);
      }
    };

    const fetchNewListings = async () => {
      try {
        const response = await axios.get(SERVER_URL + `/api/v1/all/newly-added?category=${category}`);
        setNewlyAdded(response.data || []);
        console.log(newlyAdded);
      } catch (error) {
        console.log(error);
      }
    };

    if (category) {
      fetchTrending();
      fetchNewListings();
    }
  }, [category]);

  return (
    <div style={{ display: 'flex', flex: 1, flexDirection: 'column', height: '100%', width: '100%' }}>
      {category ? (
        <div>
          <div style={{
            justifyContent: 'center',
            alignItems: 'center',
            display: 'flex',
            width: '100%',
            fontWeight: 'bold',
            fontSize: '170%',
          }}>
            <span>{category}</span>
          </div>

          <span style={{ fontSize: '150%', fontWeight: '500', marginBottom: '1%' }}>Trending products</span>
          <div style={{ display: 'flex', flexDirection: 'row', gap: '2%', overflowX: 'auto', height: '40%' }}>


          <span style={{ fontSize: '150%', fontWeight: '500', marginBottom: '1%' }}>Newly Listed</span>
          <div style={{ display: 'flex', flexDirection: 'row', gap: '2%', overflowX: 'auto', height: '40%' }}>
            {newlyAdded?.map((item, index) => (
              <ProductCard 
                key={index}
                name={item?.name} 
                description={item?.description} 
                rentalPrice={item?.rentalPrice} 
              />
            ))}
          </div>
        </div>
      ) : (
        <div>Select a category to see products</div>
      )}
    </div>
  );
}

export default ContentHomeScreen;
