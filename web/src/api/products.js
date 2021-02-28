import axios from 'axios';

export const fetchProducts = async () => {
  const { data } = await axios.get('http://localhost:8080/products');
  return data;
};

export const fetchProduct = async (id) => {
  const { data } = await axios.get(`http://localhost:8080/products/${id}`);
  return data;
};

export const postProduct = async (product, accessToken) => {
  try {
    const { data } = await axios
      .post('http://localhost:8080/products', product, {
        headers: {
          ...(accessToken && {
            Authorization: `Bearer ${accessToken}`,
          }),
        },
      });
    return data;
  } catch (err) {
    if (err.response?.status === 400) {
      throw new Error('잘못된 요청입니다. 파라미터를 확인해 주세요');
    }

    if (err.response?.status === 401) {
      throw new Error('로그인이 필요한 기능입니다');
    }

    throw err;
  }
};

export const deleteProduct = async (id, accessToken) => {
  try {
    await axios.delete(`http://localhost:8080/products/${id}`, {
      headers: {
        ...(accessToken && {
          Authorization: `Bearer ${accessToken}`,
        }),
      },
    });
  } catch (err) {
    if (err.response?.status === 401) {
      throw new Error('로그인이 필요한 기능입니다');
    }

    throw err;
  }
};

export const putProduct = async (product, accessToken) => {
  try {
    const { data } = await axios
      .patch(`http://localhost:8080/products/${product.id}`, product, {
        headers: {
          ...(accessToken && {
            Authorization: `Bearer ${accessToken}`,
          }),
        },
      });
    return data;
  } catch (err) {
    if (err.response?.status === 400) {
      throw new Error('잘못된 요청입니다. 파라미터를 확인해 주세요');
    }

    if (err.response?.status === 401) {
      throw new Error('로그인이 필요한 기능입니다');
    }

    throw err;
  }
};
