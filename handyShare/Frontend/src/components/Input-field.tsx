import React from 'react';

interface InputFieldProps {
  label: string;
  type: "text" | "email" | "password";
  id: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  required?: boolean;
}

export function InputField({
  label,
  type,
  id,
  value,
  onChange,
  required = false,
}: InputFieldProps) {
  return (
    <div>
      <label
        htmlFor={id}
        className="block text-sm font-medium text-[#262626] mb-1"
      >
        {label}
      </label>
      <input
        type={type}
        id={id}
        value={value}
        onChange={onChange}
        className="mt-1 block w-full px-3 py-2 bg-white border-b-2 border-[#9d9da1] shadow-sm focus:border-[#0295db] focus:ring-0 sm:text-sm"
        required={required}
      />
    </div>
  );
}
