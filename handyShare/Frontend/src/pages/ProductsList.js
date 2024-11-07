import React, { useState, useEffect } from 'react';
import axios from 'axios';
import HeaderBar from '../components/ProfileUpdatePage/ProfileHeaderBar.js';
import { Link, useNavigate } from 'react-router-dom';

const ProductsList = () => {
  const navigate = useNavigate(); // useNavigate for navigation
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [filters, setFilters] = useState({ priceRange: '', availability: '', category: '' });
  const [sortOption, setSortOption] = useState('newest');
  const [searchQuery, setSearchQuery] = useState('');

  // Pagination state
  const [currentPage, setCurrentPage] = useState(1);
  const productsPerPage = 6;

  useEffect(() => {
    // Fetch products and categories data from API
    const fetchData = async () => {
      try {
        const token = localStorage.getItem('token');
        const productsResponse = await axios.get('http://localhost:8080/api/v1/user/allProducts', {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        });
        const categoriesResponse = await axios.get('http://localhost:8080/api/v1/user/allCategories', {
          headers: { Authorization: `Bearer ${token}` },
          withCredentials: true,
        });
        console.log(productsResponse.data);
        setProducts(productsResponse.data);
        setCategories(categoriesResponse.data.map(cat => cat.name));
      } catch (error) {
        console.error('Error fetching products or categories:', error);
      }
    };
    fetchData();
  }, []);

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({ ...filters, [name]: value });
    setCurrentPage(1); // Reset to first page when filters change
  };

  const handleSortChange = (e) => {
    setSortOption(e.target.value);
  };

  const handleSearchChange = (e) => {
    setSearchQuery(e.target.value);
    setCurrentPage(1); // Reset to first page when search query changes
  };

  const sortedProducts = products.sort((a, b) => {
    if (sortOption === 'newest') {
      return b.id - a.id;
    } else if (sortOption === 'highest') {
      return b.rentalPrice - a.rentalPrice;
    } else {
      return a.rentalPrice - b.rentalPrice;
    }
  });

  const filteredProducts = sortedProducts.filter(product => {
    return (
      (!filters.priceRange || product.rentalPrice <= filters.priceRange) &&
      (!filters.availability || product.availability >= filters.availability) &&
      (!filters.category || product.category === filters.category) &&
      (product.name.toLowerCase().includes(searchQuery.toLowerCase()) || 
       product.category.toLowerCase().includes(searchQuery.toLowerCase()))
    );
  });

  // Pagination logic
  const indexOfLastProduct = currentPage * productsPerPage;
  const indexOfFirstProduct = indexOfLastProduct - productsPerPage;
  const currentProducts = filteredProducts.slice(indexOfFirstProduct, indexOfLastProduct);

  const totalPages = Math.ceil(filteredProducts.length / productsPerPage);

  const handleNextPage = () => {
    setCurrentPage((prev) => (prev < totalPages ? prev + 1 : prev));
  };

  const handlePrevPage = () => {
    setCurrentPage((prev) => (prev > 1 ? prev - 1 : prev));
  };

  const handleClearFilters = () => {
    setFilters({ priceRange: '', availability: '', category: '' });
    setSortOption('newest');
    setSearchQuery('');
    setCurrentPage(1); // Reset to first page
  };

  return (
    <div>
      <HeaderBar />
      <div className="max-w-7xl mx-auto p-6">
        <h2 className="text-2xl font-semibold text-center mb-5">Items</h2>

        {/* Back Button */}
        <button
          onClick={() => navigate(-1)} // navigate back
          className="mb-4 px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-700"
        >
          Back
        </button>

        <div className="flex">
          {/* Sidebar for Filters and Sorting */}
          <div className="w-1/4 bg-gray-100 p-4 rounded-lg shadow-md">
            <h3 className="text-lg font-semibold mb-4">Filters</h3>

            <div className="mb-4">
              <label htmlFor="search" className="block text-md font-medium">Search</label>
              <input
                type="text"
                id="search"
                className="w-full mt-1 p-2 border rounded-md"
                placeholder="Search by name or category"
                value={searchQuery}
                onChange={handleSearchChange}
              />
            </div>

            <div className="mb-4">
              <label htmlFor="priceRange" className="block text-md font-medium">Max Price</label>
              <input
                type="number"
                name="priceRange"
                id="priceRange"
                className="w-full mt-1 p-2 border rounded-md"
                placeholder="Max Price"
                value={filters.priceRange}
                onChange={handleFilterChange}
              />
            </div>

            <div className="mb-4">
              <label htmlFor="availability" className="block text-md font-medium">Min Availability (hours)</label>
              <input
                type="number"
                name="availability"
                id="availability"
                className="w-full mt-1 p-2 border rounded-md"
                placeholder="Min Hours"
                value={filters.availability}
                onChange={handleFilterChange}
              />
            </div>

            <div className="mb-4">
              <label htmlFor="category" className="block text-md font-medium">Category</label>
              <select
                name="category"
                id="category"
                className="w-full mt-1 p-2 border rounded-md"
                value={filters.category}
                onChange={handleFilterChange}
              >
                <option value="">All Categories</option>
                {categories.map((category, index) => (
                  <option key={index} value={category}>{category}</option>
                ))}
              </select>
            </div>

            <div className="mb-4">
              <label htmlFor="sortOption" className="block text-md font-medium">Sort By</label>
              <select
                name="sortOption"
                id="sortOption"
                className="w-full mt-1 p-2 border rounded-md"
                value={sortOption}
                onChange={handleSortChange}
              >
                <option value="newest">Newest</option>
                <option value="highest">Highest Price</option>
                <option value="lowest">Lowest Price</option>
              </select>
            </div>

            <div className="mb-4">
              <button
                onClick={handleClearFilters}
                className="text-blue-600 underline hover:text-blue-800"
              >
                Clear Filters
              </button>
            </div>
          </div>

          {/* Product Grid */}
          <div className="w-3/4 grid grid-cols-3 gap-6 ml-6">
            {currentProducts.map((product) => (
              <Link to={`/product/${product.id}`} key={product.id}>  
                <div className="bg-white shadow-md rounded-lg p-4">
                  {product.productImage ? (
                    <img
                      src={product.productImage} 
                      alt={product.name}
                      className="w-full h-48 object-cover rounded-md mb-4"
                    />
                  ) : (
                    <div className="w-full h-48 bg-gray-300 rounded-md mb-4 flex items-center justify-center">
                      <span>No Image Available</span>
                    </div>
                  )}
                  <h3 className="text-xl font-semibold">{product.name}</h3>
                  <p className="mt-2">{product.description || 'No description available.'}</p>
                  <p className="mt-2 text-lg font-medium">Hourly Price: ${product.rentalPrice}</p>
                  <p className="mt-1 text-gray-500">Available for {product.availability} hours</p>
                </div>
              </Link>
            ))}
          </div>
        </div>

        {/* Pagination Controls */}
        <div className="flex justify-center mt-6">
          <button
            onClick={handlePrevPage}
            disabled={currentPage === 1}
            className={`px-4 py-2 mr-2 ${currentPage === 1 ? 'bg-gray-400 cursor-not-allowed' : 'bg-blue-500 hover:bg-blue-700'} text-white rounded-lg`}
          >
            Previous
          </button>
          <button
            onClick={handleNextPage}
            disabled={currentPage === totalPages}
            className={`px-4 py-2 ${currentPage === totalPages ? 'bg-gray-400 cursor-not-allowed' : 'bg-blue-500 hover:bg-blue-700'} text-white rounded-lg`}
          >
            Next
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProductsList;
