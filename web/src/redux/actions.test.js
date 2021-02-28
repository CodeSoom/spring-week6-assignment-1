import configureStore from 'redux-mock-store';

import { getDefaultMiddleware } from '@reduxjs/toolkit';

import {
  setProducts, loadProducts, registerProduct, success, clearForm, setProduct,
  loadProduct, removeProduct, setMode, updateProduct, fail, registerUser,
  clearSignUpForm, signIn, setAccessToken,
} from './slice';

import { productsFixture } from '../fixtures/products';

import {
  deleteProduct, postProduct, putProduct,
} from '../api/products';
import { postUser } from '../api/users';
import { postSession } from '../api/session';

import { userFixture } from '../fixtures/user';

const mockStore = configureStore(getDefaultMiddleware());

jest.mock('../api/products');
jest.mock('../api/users');
jest.mock('../api/session');

describe('slice', () => {
  let store;

  beforeEach(() => {
    store = mockStore(() => ({
      product: productsFixture[0],
    }));
  });

  describe('loadProducts', () => {
    it('runs setProducts', async () => {
      await store.dispatch(loadProducts());

      const actions = store.getActions();

      expect(setProducts.match(actions[0])).toBe(true);
    });
  });

  describe('loadProduct', () => {
    const id = 1;

    it('runs setProduct', async () => {
      await store.dispatch(loadProduct(id));

      const actions = store.getActions();

      expect(setProduct.match(actions[0])).toBe(true);
    });
  });

  describe('registerProducts', () => {
    context('when successfully registered', () => {
      beforeEach(() => {
        postProduct.mockResolvedValue(productsFixture[0]);
      });

      it('runs success', async () => {
        await store.dispatch(registerProduct());

        const actions = store.getActions();

        expect(clearForm.match(actions[0])).toBe(true);
        expect(success.match(actions[1])).toBe(true);
      });
    });

    context('when fail', () => {
      beforeEach(() => {
        postProduct.mockRejectedValue(new Error('Some error'));
      });

      it('runs fail', async () => {
        await store.dispatch(registerProduct());

        const actions = store.getActions();

        expect(fail.match(actions[0])).toBe(true);
      });
    });
  });

  describe('deleteProduct', () => {
    const id = 1;

    context('when success', () => {
      it('runs setProducts', async () => {
        await store.dispatch(removeProduct(id));

        const actions = store.getActions();

        expect(setProducts.match(actions[0])).toBe(true);
      });
    });

    context('when fail', () => {
      beforeEach(() => {
        deleteProduct.mockRejectedValue(new Error('Some error'));
      });

      it('runs fail', async () => {
        await store.dispatch(removeProduct(id));

        const actions = store.getActions();

        expect(fail.match(actions[0])).toBe(true);
      });
    });
  });

  describe('updateProduct', () => {
    context('when successfully updated', () => {
      beforeEach(() => {
        putProduct.mockResolvedValue(productsFixture[0]);
      });

      it('runs setProduct', async () => {
        await store.dispatch(updateProduct());

        const actions = store.getActions();

        expect(setProduct.match(actions[0])).toBe(true);
        expect(setMode.match(actions[1])).toBe(true);
      });
    });

    context('when fail', () => {
      beforeEach(() => {
        putProduct.mockRejectedValue(new Error('Some error'));
      });

      it('runs fail', async () => {
        await store.dispatch(updateProduct());

        const actions = store.getActions();

        expect(fail.match(actions[0])).toBe(true);
      });
    });
  });

  describe('registerUser', () => {
    context('when successfully registered', () => {
      beforeEach(() => {
        postUser.mockResolvedValue(userFixture);
      });

      it('runs success', async () => {
        await store.dispatch(registerUser());

        const actions = store.getActions();

        expect(success.match(actions[0])).toBe(true);
        expect(clearSignUpForm.match(actions[1])).toBe(true);
      });
    });

    context('when fail', () => {
      beforeEach(() => {
        postUser.mockRejectedValue(new Error('Some error'));
      });

      it('runs fail', async () => {
        await store.dispatch(registerUser());

        const actions = store.getActions();

        expect(fail.match(actions[0])).toBe(true);
      });
    });
  });

  describe('signIn', () => {
    context('when successfully login', () => {
      beforeEach(() => {
        postSession.mockResolvedValue('token');
      });

      it('runs setAccessToken', async () => {
        await store.dispatch(signIn());

        const actions = store.getActions();

        expect(success.match(actions[0])).toBe(true);
        expect(setAccessToken.match(actions[1])).toBe(true);
      });
    });

    context('when fail', () => {
      beforeEach(() => {
        postSession.mockRejectedValue(new Error('Some error'));
      });

      it('runs fail', async () => {
        await store.dispatch(signIn());

        const actions = store.getActions();

        expect(fail.match(actions[0])).toBe(true);
      });
    });
  });
});
