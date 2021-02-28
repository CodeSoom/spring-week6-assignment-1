import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import SignInForm from '../presentational/SignInForm';

import { signIn, updateSignInForm } from '../../redux/slice';

export default function SignInFormContainer() {
  const { signInForm } = useSelector((state) => state);

  const dispatch = useDispatch();

  const handleChange = (value) => {
    dispatch(updateSignInForm(value));
  };

  const handleSubmit = () => {
    dispatch(signIn());
  };

  return (
    <SignInForm
      user={signInForm}
      onChange={handleChange}
      onSubmit={handleSubmit}
    />
  );
}
