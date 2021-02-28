import { useEffect } from 'react';

import { useHistory } from 'react-router-dom';

import { useDispatch, useSelector } from 'react-redux';

import Swal from 'sweetalert2';

import SignUpFormContainer from '../components/containers/SignUpFormContainer';

import { initializeStatus } from '../redux/slice';

export default function SignUpPage() {
  const history = useHistory();

  const dispatch = useDispatch();
  const { status } = useSelector((state) => state);

  useEffect(() => {
    if (status?.type === 'FAIL') {
      Swal.fire(status.message);
      dispatch(initializeStatus());
    }

    if (status?.type === 'SUCCESS') {
      history.push('/products');
      dispatch(initializeStatus());
    }
  }, [status]);

  return (
    <>
      <h2>회원가입</h2>

      <SignUpFormContainer />
    </>
  );
}
