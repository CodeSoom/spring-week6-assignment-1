import { createSlice } from '@reduxjs/toolkit';

import {
  fetchProducts, fetchProduct, postProduct, deleteProduct, putProduct,
} from '../api/products';
import { postUser } from '../api/users';
import { postSession } from '../api/session';

const initialState = {
  product: {},
  products: [],
  selectedProductId: null,
  accessToken: localStorage.getItem('accessToken'),
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
  signInForm: {
    email: '',
    password: '',
  },
  status: null,
  mode: 'READ',
};

const { actions, reducer } = createSlice({
  name: 'app',
  initialState,
  reducers: {
    setProducts: (state, { payload: products }) => ({
      ...state,
      products,
    }),
    setAccessToken: (state, { payload: accessToken }) => {
      localStorage.setItem('accessToken', accessToken);

      return {
        ...state,
        accessToken,
      };
    },
    logout: (state) => {
      localStorage.setItem('accessToken', '');

      return { ...state, accessToken: '' };
    },
    updateForm: (state, { payload: { name, value } }) => ({
      ...state,
      form: {
        ...state.form,
        [name]: value,
      },
    }),
    updateSignUpForm: (state, { payload: { name, value } }) => ({
      ...state,
      signUpForm: {
        ...state.signUpForm,
        [name]: value,
      },
    }),
    updateSignInForm: (state, { payload: { name, value } }) => ({
      ...state,
      signInForm: {
        ...state.signInForm,
        [name]: value,
      },
    }),
    setProduct: (state, { payload: product }) => ({
      ...state,
      product,
    }),
    initializeStatus: (state) => ({
      ...state,
      status: initialState.status,
    }),
    success: (state, { payload: message }) => ({
      ...state,
      status: {
        type: 'SUCCESS',
        message,
      },
    }),
    fail: (state, { payload: message }) => ({
      ...state,
      status: {
        type: 'FAIL',
        message,
      },
    }),
    clearForm: (state) => ({
      ...state,
      form: initialState.form,
    }),
    clearSignUpForm: (state) => ({
      ...state,
      signUpForm: initialState.signUpForm,
    }),
    clearSelectedProductId: (state) => ({
      ...state,
      selectedProductId: initialState.selectedProductId,
    }),
    selectProduct: (state, { payload: id }) => ({
      ...state,
      selectedProductId: id,
    }),
    setMode: (state, { payload: mode }) => ({
      ...state,
      form: {
        name: state.product.name,
        maker: state.product.maker,
        price: state.product.price,
        imageUrl: state.product.imageUrl,
      },
      mode,
    }),
  },
});

export const {
  setProducts,
  setAccessToken,
  logout,
  updateForm,
  updateSignUpForm,
  updateSignInForm,
  success,
  initializeStatus,
  clearForm,
  clearSignUpForm,
  selectProduct,
  clearSelectedProductId,
  setProduct,
  setMode,
  fail,
} = actions;

export const loadProducts = () => async (dispatch) => {
  const products = await fetchProducts();
  dispatch(setProducts(products));
};

export const registerProduct = () => async (dispatch, getState) => {
  const { form, accessToken } = getState();

  try {
    await postProduct(form, accessToken);
  } catch (err) {
    dispatch(fail(err.message));
    return;
  }

  dispatch(clearForm());
  dispatch(success('상품이 등록되었습니다.'));
};

export const loadProduct = (id) => async (dispatch) => {
  const product = await fetchProduct(id);

  dispatch(setProduct(product));
};

export const removeProduct = (id) => async (dispatch, getState) => {
  const { accessToken } = getState();

  try {
    await deleteProduct(id, accessToken);
    dispatch(loadProducts());
  } catch (err) {
    dispatch(fail(err.message));
  }
};

export const updateProduct = () => async (dispatch, getState) => {
  const { product, form, accessToken } = getState();

  try {
    const updatedProduct = await putProduct({
      ...product,
      ...form,
    }, accessToken);

    dispatch(setProduct(updatedProduct));
    dispatch(setMode('READ'));
  } catch (err) {
    dispatch(fail(err.message));
  }
};

export const registerUser = () => async (dispatch, getState) => {
  const { signUpForm } = getState();

  try {
    await postUser(signUpForm);
    dispatch(success('사용자가 등록되었습니다.'));
    dispatch(clearSignUpForm());
  } catch (err) {
    dispatch(fail(err.message));
  }
};

export const signIn = () => async (dispatch, getState) => {
  const { signInForm } = getState();

  try {
    const accessToken = await postSession(signInForm);
    dispatch(success('로그인 되었습니다.'));
    dispatch(setAccessToken(accessToken));
  } catch (err) {
    dispatch(fail(err.message));
  }
};

export default reducer;
