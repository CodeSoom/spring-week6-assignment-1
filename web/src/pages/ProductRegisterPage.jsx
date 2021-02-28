import { useEffect } from 'react';

import { useHistory } from 'react-router-dom';

import { useDispatch, useSelector } from 'react-redux';

import Swal from 'sweetalert2';

import FormContainer from '../components/containers/FormContainer';

import { initializeStatus } from '../redux/slice';

export default function ProductRegisterPage() {
  const history = useHistory();

  const dispatch = useDispatch();
  const { status } = useSelector((state) => state);

  useEffect(() => {
    if (status?.type === 'SUCCESS') {
      history.push('/products');
      dispatch(initializeStatus());
    }
  }, [status]);

  useEffect(() => {
    if (status?.type === 'FAIL') {
      Swal.fire(status.message);
      dispatch(initializeStatus());
    }
  }, [status]);

  return (
    <>
      <h2>고양이 장난감 등록</h2>

      <FormContainer />
    </>
  );
}
