import { useDispatch, useSelector } from 'react-redux';
import { getDefaultMiddleware } from '@reduxjs/toolkit';

import { render, fireEvent, waitFor } from '@testing-library/react';
import configureStore from 'redux-mock-store';

import SignInFormContainer from './SignInFormContainer';

import { success, updateSignInForm } from '../../redux/slice';

const mockStore = configureStore(getDefaultMiddleware());

jest.mock('react-redux');
jest.mock('../../api/products');
jest.mock('../../api/session');

describe('SignInFormContainer', () => {
  const email = 'test@email.com';
  const password = 'test1234';

  let store;

  const renderSignInFormContainer = () => render((
    <SignInFormContainer />
  ));

  beforeEach(() => {
    store = mockStore(() => ({
      signInForm: {
        email,
        password,
      },
    }));
    useSelector.mockImplementation((selector) => selector(store.getState()));
    useDispatch.mockImplementation(() => store.dispatch);
  });

  it('renders', () => {
    const { getByDisplayValue } = renderSignInFormContainer();

    expect(getByDisplayValue(email)).toBeInTheDocument();
    expect(getByDisplayValue(password)).toBeInTheDocument();
  });

  describe('Changing value', () => {
    const value = 'test@test.com';

    it('runs updateSignUpForm action', () => {
      const { getByLabelText } = renderSignInFormContainer();

      fireEvent.change(getByLabelText('이메일'), {
        target: { value },
      });

      const actions = store.getActions();

      expect(actions[0]).toEqual(updateSignInForm({
        name: 'email',
        value,
      }));
    });
  });

  describe('Clicking submit button', () => {
    it('runs registerProduct', async () => {
      const { getByText } = renderSignInFormContainer();

      fireEvent.click(getByText('로그인'));

      await waitFor(() => {
        const actions = store.getActions();

        expect(success.match(actions[0])).toBe(true);
      });
    });
  });
});
