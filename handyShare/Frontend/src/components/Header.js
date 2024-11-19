import React, { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { FaUser, FaSignOutAlt, FaUserCircle } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import { message } from 'antd';

const CartoonCloud = ({ className }) => (
    <svg className={className} viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
        <motion.path
            d="M25 60C25 71.0457 33.9543 80 45 80H70C81.0457 80 90 71.0457 90 60C90 48.9543 81.0457 40 70 40C70 28.9543 61.0457 20 50 20C38.9543 20 30 28.9543 30 40C18.9543 40 10 48.9543 10 60C10 71.0457 18.9543 80 30 80H45"
            stroke="currentColor"
            strokeWidth="4"
            strokeLinecap="round"
            initial={{ pathLength: 0 }}
            animate={{ pathLength: 1 }}
            transition={{ duration: 2, repeat: Infinity, repeatType: 'loop', ease: 'easeInOut' }}
        />
    </svg>
);

const CartoonSun = ({ className }) => (
    <svg className={className} viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
        <motion.circle
            cx="50"
            cy="50"
            r="20"
            stroke="currentColor"
            strokeWidth="4"
            initial={{ scale: 0.8, opacity: 0.5 }}
            animate={{ scale: 1, opacity: 1 }}
            transition={{ duration: 1.5, repeat: Infinity, repeatType: 'reverse', ease: 'easeInOut' }}
        />
        <motion.g initial={{ rotate: 0 }} animate={{ rotate: 360 }} transition={{ duration: 10, repeat: Infinity, ease: 'linear' }}>
            {[0, 45, 90, 135, 180, 225, 270, 315].map((angle) => (
                <motion.line
                    key={angle}
                    x1="50"
                    y1="25"
                    x2="50"
                    y2="15"
                    stroke="currentColor"
                    strokeWidth="4"
                    strokeLinecap="round"
                    transform={`rotate(${angle} 50 50)`}
                />
            ))}
        </motion.g>
    </svg>
);

function AnimatedHeader() {
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const navigate = useNavigate();
    const dropdownRef = useRef(null);

    const toggleDropdown = () => setIsDropdownOpen(!isDropdownOpen);

    const handleSignOut = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        message.success('Successfully signed out');
        navigate('/login');
    };

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsDropdownOpen(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    return (
        <motion.header
            className="bg-[#3B7BF8] text-white p-4 relative  "
            initial={{ opacity: 0, y: -50 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                height: '100%',
                flex: 1,
            }}
        >
            <div
                className="flex items-center space-x-2 cursor-pointer"
                onClick={() => navigate('/homepage')}
            >
                <motion.img
                    src="/Assets/Logo.png"
                    alt="HandyShare Logo"
                    className="h-10"
                    whileHover={{ rotate: 360 }}
                    transition={{ duration: 0.5 }}
                    style={{zIndex:3}}
                    onClick={() => navigate('/homepage')}
                />
                <motion.h1
                    className="text-2xl font-extrabold font-comic"
                    initial={{ x: -20, opacity: 0 }}
                    animate={{ x: 0, opacity: 1 }}
                    transition={{ delay: 0.2 }}
                >
                    HandyShare
                </motion.h1>
            </div>

            <div className="flex items-center space-x-4">
                <motion.button
                    className="bg-white bg-opacity-20 hover:bg-opacity-30 py-2 px-4 rounded-full font-medium transition duration-300"
                    whileHover={{ scale: 1.05 }}
                    whileTap={{ scale: 0.95 }}
                    onClick={() => navigate('/lendings')}
                >
                    Lendings
                </motion.button>

                <motion.button
                    className="bg-white bg-opacity-20 hover:bg-opacity-30 py-2 px-4 rounded-full font-medium transition duration-300"
                    whileHover={{ scale: 1.05 }}
                    whileTap={{ scale: 0.95 }}
                    onClick={() => navigate('/borrow')}
                >
                    Borrowings
                </motion.button>
            </div>

            <div className="relative" ref={dropdownRef} style={{ zIndex: 10 }}> {/* Increased z-index */}
                <motion.button
                    className="bg-white bg-opacity-20 hover:bg-opacity-30 p-2 rounded-full transition duration-300"
                    whileHover={{ scale: 1.05 }}
                    whileTap={{ scale: 0.95 }}
                    onClick={() => toggleDropdown()}
                >
                    <FaUser className="text-xl" />
                </motion.button>

                <AnimatePresence>
                    {isDropdownOpen && (
                        <motion.div
                            className="absolute right-0 mt-2 w-48 bg-white rounded-md shadow-lg py-1 text-gray-800 z-100"   
                            initial={{ opacity: 0, y: -10 }}
                            animate={{ opacity: 1, y: 0 }}
                            exit={{ opacity: 0, y: -10 }}
                            transition={{ duration: 0.2 }}
                            style={{ position: 'absolute', zIndex: 10 }}  
                        >
                            <div
                                className="block px-4 py-2 hover:bg-gray-100 cursor-pointer"
                                onClick={() => {
                                    navigate('/profile');
                                    setIsDropdownOpen(false);
                                }}
                            >
                                <FaUserCircle className="inline-block mr-2" /> Profile
                            </div>
                            <button
                                onClick={handleSignOut}
                                className="block w-full text-left px-4 py-2 hover:bg-gray-100"
                            >
                                <FaSignOutAlt className="inline-block mr-2" /> Sign Out
                            </button>
                        </motion.div>
                    )}
                </AnimatePresence>
            </div>


            <CartoonCloud className="absolute top-0 left-0 w-32 h-32 text-white opacity-20" style={{ zIndex: 0 }} />
            <CartoonCloud className="absolute bottom-0 right-0 w-40 h-40 text-white opacity-20" style={{ zIndex: 0 }} />
            <CartoonSun className="absolute top-4 right-4 w-16 h-16 text-yellow-300 opacity-70" style={{ zIndex: 0 }} />
        </motion.header>
    );
}

export default AnimatedHeader;
