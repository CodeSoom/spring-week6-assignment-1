import reducer, {
  setProducts, updateForm, success, clearForm, initializeStatus, selectProduct,
  clearSelectedProductId, setProduct, setMode, fail, updateSignUpForm,
  updateSignInForm, clearSignUpForm, setAccessToken, logout,
} from './slice';

import { productsFixture } from '../fixtures/products';
import { userFixture } from '../fixtures/user';

describe('reducer', () => {
  const initialState = {
    selectedProductId: null,
    product: {},
    products: [],
    accessToken: '',
    form: {
      name: '',
      maker: '',
      price: '',
      imageUrl: '',
    },
    signUpForm: {
      name: '',
      email: '',
      password: '',
    },
    mode: 'READ',
  };

  describe('setProducts', () => {
    it('updates products', () => {
      const state = reducer(initialState, setProducts(productsFixture));

      expect(state.products).toEqual(productsFixture);
    });
  });

  describe('setAccessToken', () => {
    it('updates accessToken', () => {
      const state = reducer(initialState, setAccessToken('token'));

      expect(state.accessToken).toEqual('token');
    });
  });

  describe('logout', () => {
    it('updates accessToken to be empty', () => {
      const state = reducer({
        ...initialState,
        accessToken: 'token',
      }, logout());

      expect(state.accessToken).toEqual('');
    });
  });

  describe('updateForm', () => {
    const value = '장난감 뱀';

    it('updates form', () => {
      const state = reducer(initialState, updateForm({
        name: 'name',
        value,
      }));

      expect(state.form.name).toBe(value);
    });
  });

  describe('updateSignUpForm', () => {
    const value = '영희';

    it('updates signUpForm', () => {
      const state = reducer(initialState, updateSignUpForm({
        name: 'name',
        value,
      }));

      expect(state.signUpForm.name).toBe(value);
    });
  });

  describe('updateSignInForm', () => {
    const value = 'test@email.com';

    it('updates signInForm', () => {
      const state = reducer(initialState, updateSignInForm({
        name: 'email',
        value,
      }));

      expect(state.signInForm.email).toBe(value);
    });
  });

  describe('setProduct', () => {
    it('updates product', () => {
      const state = reducer(initialState, setProduct(productsFixture[0]));

      expect(state.product).toEqual(productsFixture[0]);
    });
  });

  describe('success', () => {
    it('updates status', () => {
      const state = reducer(initialState, success('성공'));

      expect(state.status).toEqual({
        type: 'SUCCESS',
        message: '성공',
      });
    });
  });

  describe('fail', () => {
    it('updates status', () => {
      const state = reducer(initialState, fail('실패'));

      expect(state.status).toEqual({
        type: 'FAIL',
        message: '실패',
      });
    });
  });

  describe('clearForm', () => {
    it('updates to initial form', () => {
      const state = reducer({
        ...initialState,
        form: {
          name: '장난감 뱀',
          maker: '애옹이네 장난감',
          price: 5000,
        },
      }, clearForm());

      expect(state.form).toEqual(initialState.form);
    });
  });

  describe('clearSignUpForm', () => {
    it('updates to initial form', () => {
      const state = reducer({
        ...initialState,
        signUpForm: userFixture,
      }, clearSignUpForm());

      expect(state.signUpForm).toEqual(initialState.signUpForm);
    });
  });

  describe('initilizeStatus', () => {
    it('updates initial status', () => {
      const state = reducer({
        ...initialState,
        status: {
          type: 'SUCCESS',
          message: 'Some message',
        },
      }, initializeStatus());

      expect(state.status).toBeNull();
    });
  });

  describe('selectProduct', () => {
    it('updates selectedProductId', () => {
      const state = reducer(initialState, selectProduct(1));

      expect(state.selectedProductId).toBe(1);
    });
  });

  describe('clearSelectedProductId', () => {
    it('updates selectedProductId to be null', () => {
      const state = reducer({
        ...initialState,
        selectedProductId: 1,
      }, clearSelectedProductId());

      expect(state.selectedProductId).toBeNull();
    });
  });

  describe('setMode', () => {
    const product = productsFixture[0];

    it('updates mode and form', () => {
      const state = reducer({
        ...initialState,
        product,
      }, setMode('UPDATE'));

      expect(state.mode).toBe('UPDATE');
      expect(state.form).toEqual({
        name: product.name,
        maker: product.maker,
        price: product.price,
        imageUrl: product.imageUrl,
      });
    });
  });
});
