import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import SignUpForm from '../presentational/SignUpForm';

import { registerUser, updateSignUpForm } from '../../redux/slice';

export default function SignUpFormContainer() {
  const { signUpForm } = useSelector((state) => state);

  const dispatch = useDispatch();

  const handleChange = (value) => {
    dispatch(updateSignUpForm(value));
  };

  const handleSubmit = () => {
    dispatch(registerUser());
  };

  return (
    <SignUpForm
      user={signUpForm}
      onChange={handleChange}
      onSubmit={handleSubmit}
    />
  );
}
