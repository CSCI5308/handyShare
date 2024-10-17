import React from 'react';
import { Link } from 'react-router-dom';

export function Header() {
  return (
    <header className="flex justify-between items-center p-4 bg-[#333333] text-white">
      <div className="flex items-center">
        <Link to="/" className="mr-4 text-white hover:underline">
          <span className="material-icons">arrow_back</span>
        </Link>
        <div className="text-2xl font-bold">handyShare</div>
      </div>
      <nav className="space-x-4">
        <Link to="/lendings" className="hover:underline">Lendings</Link>
        <Link to="/borrowings" className="hover:underline">Borrowings</Link>
        <Link to="/profile" className="hover:underline">Profile</Link>
      </nav>
    </header>
  );
}
