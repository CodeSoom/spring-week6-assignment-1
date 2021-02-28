import { useSelector, useDispatch } from 'react-redux';
import { getDefaultMiddleware } from '@reduxjs/toolkit';

import {
  fireEvent, render, waitFor, screen,
} from '@testing-library/react';
import configureStore from 'redux-mock-store';

import ProductsPage from './ProductsPage';

import {
  setProducts, clearSelectedProductId, initializeStatus, logout,
} from '../redux/slice';

import { productsFixture } from '../fixtures/products';

const mockStore = configureStore(getDefaultMiddleware());

const mockPush = jest.fn();

jest.mock('react-redux');
jest.mock('../api/products');
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useHistory() {
    return { push: mockPush };
  },
}));

describe('ProductsPage', () => {
  const store = mockStore(() => ({
    status: given.status,
    selectedProductId: given.selectedProductId,
    products: productsFixture,
    accessToken: given.accessToken,
  }));

  const renderProductsPage = () => render((
    <ProductsPage />
  ));

  beforeEach(() => {
    given('status', () => null);
    given('selectedProductId', () => null);
    given('accessToken', () => '');
  });

  beforeEach(() => {
    useSelector.mockImplementation((selector) => selector(store.getState()));
    useDispatch.mockImplementation(() => store.dispatch);
  });

  it('dispatches', async () => {
    renderProductsPage();

    await waitFor(() => {
      const actions = store.getActions();

      expect(setProducts.match(actions[0])).toBe(true);
    });
  });

  it('renders', () => {
    const { container, getByText } = renderProductsPage();

    expect(container).toHaveTextContent('고양이 장난감 목록');
    expect(container).toHaveTextContent(productsFixture[0].name);
    expect(container).toHaveTextContent(productsFixture[0].maker);
    expect(container).toHaveTextContent(`${productsFixture[0].price}원`);
    expect(getByText('등록하기')).toBeInTheDocument();
  });

  context('when selectedProductId is exist', () => {
    const id = 1;

    beforeEach(() => {
      given('selectedProductId', () => id);
    });

    it('goes to product detail page', () => {
      renderProductsPage();

      const actions = store.getActions();

      expect(mockPush).toBeCalledWith(`/products/${id}`);

      const action = actions.find(clearSelectedProductId.match);

      expect(action).not.toBeUndefined();
    });
  });

  context('when status is fail', () => {
    beforeEach(() => {
      given('status', () => ({ type: 'FAIL', message: 'Error message' }));
    });

    it('shows messge alert', () => {
      renderProductsPage();

      const actions = store.getActions();

      expect(screen.getByText('Error message')).toBeInTheDocument();
      expect(actions.find(initializeStatus.match)).not.toBeUndefined();
    });
  });

  describe('Clicking 등록하기 button', () => {
    it('calls push', () => {
      const { getByText } = renderProductsPage();

      fireEvent.click(getByText('등록하기'));

      expect(mockPush).toBeCalledWith('/products/product');
    });
  });

  describe('Clicking 회원가입 button', () => {
    it('calls push', () => {
      const { getByText } = renderProductsPage();

      fireEvent.click(getByText('회원가입'));

      expect(mockPush).toBeCalledWith('/signup');
    });
  });

  describe('Clicking 로그인 button', () => {
    it('calls push', () => {
      const { getByText } = renderProductsPage();

      fireEvent.click(getByText('로그인'));

      expect(mockPush).toBeCalledWith('/signin');
    });
  });

  describe('Clicking loguout button', () => {
    beforeEach(() => {
      given('accessToken', () => 'token');
    });

    it('calls logout action', async () => {
      const { getByText } = renderProductsPage();

      fireEvent.click(getByText('로그아웃'));

      await waitFor(() => {
        const actions = store.getActions();

        expect(actions.find(logout.match)).not.toBeUndefined();
      });
    });
  });
});
