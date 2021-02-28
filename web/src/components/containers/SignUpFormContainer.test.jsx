import { useDispatch, useSelector } from 'react-redux';
import { getDefaultMiddleware } from '@reduxjs/toolkit';

import { render, fireEvent, waitFor } from '@testing-library/react';
import configureStore from 'redux-mock-store';

import SignUpFormContainer from './SignUpFormContainer';

import { clearSignUpForm, success, updateSignUpForm } from '../../redux/slice';

const mockStore = configureStore(getDefaultMiddleware());

jest.mock('react-redux');
jest.mock('../../api/products');
jest.mock('../../api/users');

describe('SignUpFormContainer', () => {
  const name = '홍길동';
  const email = 'test@email.com';
  const password = 'test1234';

  let store;

  const renderSignUpFormContainer = () => render((
    <SignUpFormContainer />
  ));

  beforeEach(() => {
    store = mockStore(() => ({
      signUpForm: {
        name,
        email,
        password,
      },
    }));
    useSelector.mockImplementation((selector) => selector(store.getState()));
    useDispatch.mockImplementation(() => store.dispatch);
  });

  it('renders', () => {
    const { getByDisplayValue } = renderSignUpFormContainer();

    expect(getByDisplayValue(name)).toBeInTheDocument();
    expect(getByDisplayValue(email)).toBeInTheDocument();
    expect(getByDisplayValue(password)).toBeInTheDocument();
  });

  describe('Changing value', () => {
    const value = '철수';

    it('runs updateSignUpForm action', () => {
      const { getByLabelText } = renderSignUpFormContainer();

      fireEvent.change(getByLabelText('이름'), {
        target: { value },
      });

      const actions = store.getActions();

      expect(actions[0]).toEqual(updateSignUpForm({
        name: 'name',
        value,
      }));
    });
  });

  describe('Clicking submit button', () => {
    it('runs registerProduct', async () => {
      const { getByText } = renderSignUpFormContainer();

      fireEvent.click(getByText('등록'));

      await waitFor(() => {
        const actions = store.getActions();

        expect(success.match(actions[0])).toBe(true);
        expect(clearSignUpForm.match(actions[1])).toBe(true);
      });
    });
  });
});
