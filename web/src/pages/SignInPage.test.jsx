import { useDispatch, useSelector } from 'react-redux';
import { getDefaultMiddleware } from '@reduxjs/toolkit';

import { render, screen } from '@testing-library/react';
import configureStore from 'redux-mock-store';

import SignInPage from './SignInPage';

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

describe('SignInPage', () => {
  const store = mockStore(() => ({
    signInForm: {
      email: '',
      password: '',
    },
    status: given.status,
  }));

  const renderSignInPage = () => render((
    <SignInPage />
  ));

  beforeEach(() => {
    given('status', () => null);
  });

  beforeEach(() => {
    useSelector.mockImplementation((selector) => selector(store.getState()));
    useDispatch.mockImplementation(() => store.dispatch);
  });
  it('renders', () => {
    const { container, getByLabelText } = renderSignInPage();

    expect(container).toHaveTextContent('로그인');
    expect(getByLabelText('이메일')).toBeInTheDocument();
    expect(getByLabelText('비밀번호')).toBeInTheDocument();
  });

  context('when status is succss', () => {
    beforeEach(() => {
      given('status', () => ({ type: 'SUCCESS' }));
    });

    it('goes to products page', () => {
      renderSignInPage();

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
      renderSignInPage();

      const actions = store.getActions();

      expect(screen.getByText('Error message')).toBeInTheDocument();
      expect(initializeStatus.match(actions[0])).toBe(true);
    });
  });
});
