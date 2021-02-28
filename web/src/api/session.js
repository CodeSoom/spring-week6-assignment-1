import axios from 'axios';

export const postSession = async (user) => {
  try {
    const { data } = await axios
      .post('http://localhost:8080/session', user);
    return data.accessToken;
  } catch (err) {
    if (err.response?.status === 400) {
      throw new Error('잘못된 요청입니다. 파라미터를 확인해 주세요');
    }

    throw err;
  }
};

export const xxx = '';
