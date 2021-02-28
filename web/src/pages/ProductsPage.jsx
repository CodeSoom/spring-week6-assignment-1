import { useEffect } from 'react';

import { useHistory } from 'react-router-dom';

import { useDispatch, useSelector } from 'react-redux';

import Swal from 'sweetalert2';

import ProductsContainer from '../components/containers/ProductsContainer';

import {
  loadProducts, clearSelectedProductId, initializeStatus, logout,
} from '../redux/slice';

export default function ProductsPage() {
  const history = useHistory();
  const dispatch = useDispatch();

  const {
    status, selectedProductId, accessToken,
  } = useSelector((state) => state);

  const handleClick = () => {
    history.push('/products/product');
  };

  const handleClickSignUp = () => {
    history.push('/signup');
  };

  const handleClickSignIn = () => {
    history.push('/signin');
  };

  const handleClickLogout = () => {
    dispatch(logout());
  };

  useEffect(() => {
    dispatch(loadProducts());
  }, []);

  useEffect(() => {
    if (selectedProductId) {
      history.push(`/products/${selectedProductId}`);
      dispatch(clearSelectedProductId());
    }
  }, [selectedProductId]);

  useEffect(() => {
    if (status?.type === 'FAIL') {
      Swal.fire(status.message);
      dispatch(initializeStatus());
    }
  }, [status]);

  return (
    <>
      <h2>고양이 장난감 목록</h2>
      <button type="button" onClick={handleClickSignUp}>회원가입</button>
      { accessToken && (
        <button type="button" onClick={handleClickLogout}>로그아웃</button>
      )}
      { !accessToken && (
        <button type="button" onClick={handleClickSignIn}>로그인</button>
      )}
      <button type="button" onClick={handleClick}>
        등록하기
      </button>
      <ProductsContainer />
    </>
  );
}
