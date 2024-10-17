import React from 'react';

interface ButtonProps {
  children: React.ReactNode;
  type?: 'button' | 'submit' | 'reset';
  variant?: 'primary' | 'secondary';
  onClick?: () => void;
  className?: string;
}

export function Button({
  children,
  type = 'button',
  variant = 'primary',
  onClick,
  className = ''
}: ButtonProps) {
  const baseClasses =
    "w-full py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium focus:outline-none focus:ring-2 focus:ring-offset-2";
  const variantClasses = {
    primary: "text-white bg-[#0295db] hover:bg-[#026fa8] focus:ring-[#0295db]",
    secondary: "text-[#0295db] bg-white border-gray-300 hover:bg-[#e0e5e9] focus:ring-[#0295db]"
  };

  return (
    <button
      type={type}
      onClick={onClick}
      className={`${baseClasses} ${variantClasses[variant]} ${className}`}
    >
      {children}
    </button>
  );
}
