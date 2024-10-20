import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

import axios from 'axios';
import { SERVER_URL } from '../../constants';

function ContentHomeScreen({ category }) {
  const [trending, setTrending] = useState([]);
  const [newlyAdded, setNewlyAdded] = useState([]);

  useEffect(() => {
    const fetchTrending = async () => {
      try {
        const response = await axios.get(SERVER_URL+ `/api/v1/all/getTrendingByCategory?category=${category}`);
        setTrending(response.data.body);
      } catch (error) {
        console.log("Error while loading trending topics", error);
      }
    };
  
    const fetchNewListings = async () => {
      try {
        const response = await axios.get(SERVER_URL + "/api/v1/all/newlistings");
        setNewlyAdded(response.data);
      } catch (error) {
        console.log(error);
      }
    };
  
    if (category) { // Fetch data only if category exists
      fetchTrending();
      fetchNewListings();
    }
  }, [category]); // Add 'category' to the dependency array
  

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
            {trending.map((item, index) => (
              <Link to={`/product/${item.product.id}`} key={index} style={{ textDecoration: 'none' }}>
                <div 
                  style={{
                    minHeight: '100%', 
                    width: "25%", 
                    background: '#3B7BF8', 
                    borderRadius: '15px',
                    justifyContent: 'space-between', 
                    alignItems: 'center',
                    display: 'flex', 
                    flexShrink: 0, 
                    flexDirection: 'column',
                    padding: '20px',
                    boxShadow: '0 6px 12px rgba(0, 0, 0, 0.1)',
                    color: 'white'
                  }}
                >
                  {/* Space for Product Image */}
                  <div style={{
                    height: '500px', 
                    width: '100%', 
                    background: '#d9d9d9', 
                    borderRadius: '15px', 
                    marginBottom: '20px',
                    display: 'flex', 
                    justifyContent: 'center', 
                    alignItems: 'center',
                  }}>
                    <span style={{ color: '#3B7BF8', fontSize: '18px' }}>Image Placeholder</span>
                  </div>

                  {/* Product Name */}
                  <div style={{ fontWeight: 'bold', fontSize: '24px', marginBottom: '5px', textAlign: 'left' }}>
                    {item.product.name}
                    <p style={{ textAlign: 'center', fontSize: '90%' , fontWeight:'normal'}}>{item.product.description}</p> {/* Add description */}
                  </div>

                  {/* Product Price */}
                  <div style={{ fontSize: '22px', textAlign: 'center' }}>
                  Hourly Price :   ${item.product.rentalPrice}
                  </div>
                </div>
              </Link>
            ))}
          </div>


          <span style={{ fontSize: '150%', fontWeight: '500', marginBottom: '1%' }}>Newly Listed</span>
          <div style={{ display: 'flex', flexDirection: 'row', gap: '2%', overflowX: 'auto', height: '33.33%' }}>
            {newlyAdded.map((item, index) => (
              <div key={index} style={{
                minHeight: '100%', width: "25%", background: 'green', borderRadius: '10px',
                justifyContent: 'center', alignItems: 'center',
                display: 'flex', flexShrink: 0
              }}>
                {item.title}
              </div>
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
