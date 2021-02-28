const axios = require('axios');

const baseUrl = 'http://localhost:8080/products';

const signUp = async () => {
  const { data: user } = await axios.post('http://localhost:8080/users', {
    email: `${new Date().getTime()}@email.com`,
    name: 'testuser',
    password: 'test1234',
  });
  return user;
};

const signIn = async () => {
  const user = await signUp();
  const { data } = await axios.post('http://localhost:8080/session', {
    email: user.email,
    password: 'test1234',
  });
  return data.accessToken;
};

const deleteAll = async () => {
  const accessToken = await signIn();
  const { data: products } = await axios.get(baseUrl);
  await Promise.all(
    products
      .map(({ id }) => axios.delete(`${baseUrl}/${id}`, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      })),
  );
};

const create = async (product) => {
  const accessToken = await signIn();
  const { data } = await axios.post(baseUrl, product, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });
  return data;
};

module.exports = {
  deleteAll,
  create,
  signUp,
};
