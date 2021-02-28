import { useEffect } from 'react';

import { useParams } from 'react-router-dom';

import { useDispatch, useSelector } from 'react-redux';

import Swal from 'sweetalert2';

import ProductContainer from '../components/containers/ProductContainer';

import { loadProduct, initializeStatus } from '../redux/slice';

export default function ProductDetailPage() {
  const dispatch = useDispatch();
  const { status } = useSelector((state) => state);

  const { id } = useParams();

  useEffect(() => {
    dispatch(loadProduct(id));
  }, []);

  useEffect(() => {
    if (status?.type === 'FAIL') {
      Swal.fire(status.message);
      dispatch(initializeStatus());
    }
  }, [status]);

  return (
    <>
      <h2>고양이 장난감 상세</h2>
      <ProductContainer />
    </>
  );
}
