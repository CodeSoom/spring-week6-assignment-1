import frisby from 'frisby';

frisby.baseUrl('http://localhost:8080');

const { Joi } = frisby;

const product = Joi.object({
  id: Joi.number(),
  name: Joi.string(),
  maker: Joi.string(),
  price: Joi.number(),
  imageUrl: Joi.string().optional().allow(null),
});

describe('Products', () => {
  const toy = {
    name: '뱀 장난감',
    maker: '애용이네 장난감',
    price: 10000,
    imageUrl: 'http://localhost:8080/snake',
  };

  beforeAll(async () => {
    const user = {
      email: `${new Date().getTime()}@test.com`,
      name: 'testuser',
      password: 'password',
    };
    await frisby.post('/users', user);
    const { json } = await frisby.post('/session', {
      email: user.email,
      password: user.password,
    });

    const { accessToken } = json;

    frisby.globalSetup({
      request: {
        baseUrl: 'http://localhost:8080',
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      },
    });
  });

  describe('GET /products', () => {
    beforeEach(async () => {
      await frisby.post('/products', toy);
    });

    it('responses products', async () => {
      await frisby.get('/products')
        .expect('status', 200)
        .expect('jsonTypes', Joi.array().items(product.required()));
    });
  });

  describe('GET /products/{id}', () => {
    let id;

    context('with existing product', () => {
      beforeEach(async () => {
        const { json } = await frisby.post('/products', toy);
        id = json.id;
      });

      it('response product', async () => {
        const { json } = await frisby.get(`/products/${id}`)
          .expect('status', 200);

        expect(json).toMatchObject(toy);
      });
    });

    context('with not existing product', () => {
      beforeEach(() => {
        id = 9999;
      });

      it('response not found', async () => {
        await frisby.get(`/products/${id}`)
          .expect('status', 404);
      });
    });
  });

  describe('POST /products', () => {
    context('with correct product', () => {
      it('responses created product', async () => {
        await frisby.post('/products', toy)
          .expect('status', 201)
          .expect('jsonTypes', product);
      });
    });

    context('with invalid params', () => {
      const params = [
        { ...toy, name: '' },
        { ...toy, maker: '' },
        { ...toy, price: undefined },
        { ...toy, price: null },
      ];

      it('responses bad request', async () => {
        await Promise.all(
          params.map((body) => frisby.post('/products', body)
            .expect('status', 400)),
        );
      });
    });
  });

  describe('PATCH /products/{id}', () => {
    let id;

    context('with correct product', () => {
      const body = {
        name: '물고기 장난감',
        maker: '멍멍이네 장난감가게',
        price: 50000,
        imageUrl: 'http://localhost:8080/fish',
      };

      beforeEach(async () => {
        const { json } = await frisby.post('/products', toy);
        id = json.id;
      });

      it('responses updated product', async () => {
        const { json } = await frisby.patch(`/products/${id}`, body)
          .expect('status', 200);

        expect(json).toMatchObject(body);
      });
    });

    context('with not existing product', () => {
      beforeEach(() => {
        id = 99999;
      });

      it('responses bad request', async () => {
        await frisby.patch(`/products/${id}`, toy)
          .expect('status', 404);
      });
    });

    context('with invalid params', () => {
      const params = [
        { ...toy, name: '' },
        { ...toy, maker: '' },
        { ...toy, price: undefined },
        { ...toy, price: null },
      ];

      it('responses bad request', async () => {
        await Promise.all(
          params.map((body) => frisby.patch(`/products/${id}`, body)
            .expect('status', 400)),
        );
      });
    });
  });

  describe('DELETE /products/{id}', () => {
    let id;

    context('with existing product', () => {
      beforeEach(async () => {
        const { json } = await frisby.post('/products', toy);
        id = json.id;
      });

      it('responses No Content', async () => {
        await frisby.del(`/products/${id}`)
          .expect('status', 204);
      });
    });

    context('with not existing product', () => {
      beforeEach(() => {
        id = 99999;
      });

      it('responses Not Found', async () => {
        await frisby.del(`/products/${id}`)
          .expect('status', 404);
      });
    });
  });
});
