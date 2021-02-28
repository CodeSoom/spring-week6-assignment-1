import { useDispatch, useSelector } from 'react-redux';
import { getDefaultMiddleware } from '@reduxjs/toolkit';

import { render, screen } from '@testing-library/react';
import configureStore from 'redux-mock-store';

import SignUpPage from './SignUpPage';

import { initializeStatus } from '../redux/slice';

const mockStore = configureStore(getDefaultMiddleware());

const mockPush = jest.fn();

jest.mock('react-redux');
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory() {
    return { push: mockPush };
  },
}));

describe('SignUpPage', () => {
  const store = mockStore(() => ({
    signUpForm: {
      name: '',
      email: '',
      password: '',
    },
    status: given.status,
  }));

  const renderSignUpPage = () => render((
    <SignUpPage />
  ));

  beforeEach(() => {
    given('status', () => null);
  });

  beforeEach(() => {
    useSelector.mockImplementation((selector) => selector(store.getState()));
    useDispatch.mockImplementation(() => store.dispatch);
  });
  it('renders', () => {
    const { container, getByLabelText } = renderSignUpPage();

    expect(container).toHaveTextContent('회원가입');
    expect(getByLabelText('이름')).toBeInTheDocument();
    expect(getByLabelText('이메일')).toBeInTheDocument();
    expect(getByLabelText('비밀번호')).toBeInTheDocument();
  });

  context('when status is succss', () => {
    beforeEach(() => {
      given('status', () => ({ type: 'SUCCESS' }));
    });

    it('goes to products page', () => {
      renderSignUpPage();

      const actions = store.getActions();

      expect(mockPush).toBeCalled();
      expect(initializeStatus.match(actions[0])).toBe(true);
    });
  });

  context('when status is fail', () => {
    beforeEach(() => {
      given('status', () => ({ type: 'FAIL', message: 'Error message' }));
    });

    it('shows messge alert', () => {
      renderSignUpPage();

      const actions = store.getActions();

      expect(screen.getByText('Error message')).toBeInTheDocument();
      expect(initializeStatus.match(actions[0])).toBe(true);
    });
  });
});
