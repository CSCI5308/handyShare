import React, { useEffect, useState } from 'react';
import { SERVER_URL } from "./../../constants.js";
import axios from 'axios';

function ContentHomeScreen({ category }) {
  const [trending, setTrending] = useState([]);
  const [newlyAdded, setNewlyAdded] = useState([]);

  useEffect(() => {
    const fetchTrending = async () => {
      try {
        const response = await axios.get(SERVER_URL + "/trending");
        setTrending(response.data);
      } catch (error) {
        console.log("Error while loading trending topics");
      }
    };

    const fetchNewListings = async () => {
      try {
        const response = await axios.get(SERVER_URL + "/newlistings");
        setNewlyAdded(response.data);
      } catch (error) {
        console.log(error);
      }
    };

    fetchTrending();
    fetchNewListings();
  }, []);

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
          <div style={{ display: 'flex', flexDirection: 'row', gap: '2%', overflowX: 'auto', height: '33.33%' }}>
            {trending.map((item, index) => (
              <div key={index} style={{
                minHeight: '100%', width: "25%", background: 'green', borderRadius: '10px',
                justifyContent: 'center', alignItems: 'center',
                display: 'flex', flexShrink: 0
              }}>
                {item.title}
              </div>
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
