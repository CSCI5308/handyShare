import React, { useEffect, useState } from 'react';
import axios from "axios";
import { SERVER_URL } from '../../constants';
import { Button } from 'antd';

export default function CategoriesNavigation({ onCategorySelect }) { // Accept the prop
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        const fetchCategories = async () => {
            const response = await axios.get(SERVER_URL + "/categories");
            setCategories(response.data);
            console.log(response.data); // Log the response data
        };
        fetchCategories();
    }, []);

    return (
        <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between', height: '100%', alignItems: 'center' }}>
            {categories.map((category, index) => (
                <div key={index} style={{ paddingBottom: '5%' }}>
                    <Button 
                        style={{ width: '100%', height: '77px' }} 
                        color="primary" 
                        variant="solid"
                        onClick={() => onCategorySelect(category.name)} // Call the prop function
                    >
                        {category.name}
                    </Button>
                </div>
            ))}
        </div>
    );
}

