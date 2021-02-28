import React from 'react';

export default function Input({
  id,
  label,
  type = 'text',
  placeholder,
  value,
  onChange,
}) {
  const handleChange = ({ target }) => {
    onChange({
      name: target.id,
      value: target.value,
    });
  };

  return (
    <div>
      <label htmlFor={id}>{label}</label>
      <input
        id={id}
        value={value}
        onChange={handleChange}
        type={type}
        placeholder={placeholder}
      />
    </div>
  );
}
