import axios from 'axios';

export const postUser = async (user) => {
  try {
    const { data } = await axios
      .post('http://localhost:8080/users', user);
    return data;
  } catch (err) {
    if (err.response?.status === 400) {
      throw new Error('잘못된 요청입니다. 파라미터를 확인해 주세요');
    }

    throw err;
  }
};

export const xxx = '';
