import frisby from 'frisby';

frisby.baseUrl('http://localhost:8080');

const { Joi } = frisby;

const tokenSchema = Joi.object({ accessToken: Joi.string() });

describe('Session', () => {
  let user;

  beforeEach(async () => {
    const mockUser = {
      email: `${new Date().getTime()}@test.com`,
      name: 'testuser',
      password: 'password',
    };

    const { json } = await frisby.post('/users', mockUser);
    user = { id: json.id, ...mockUser };
  });

  describe('POST /session', () => {
    context('with correct user', () => {
      it('responses token', async () => {
        await frisby.post('/session', {
          email: user.email,
          password: user.password,
        })
          .expect('status', 201)
          .expect('jsonTypes', tokenSchema);
      });
    });

    context('with wrong password', () => {
      it('responses token', async () => {
        await frisby.post('/session', {
          email: user.email,
          password: 'wrong password',
        })
          .expect('status', 400);
      });
    });

    context('with not exists user', () => {
      it('responses token', async () => {
        await frisby.post('/session', {
          email: 'notexists@email.com',
          password: 'wrong password',
        })
          .expect('status', 400);
      });
    });
  });
});
