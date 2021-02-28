import axios from 'axios';

import { postUser } from './users';

import { userFixture } from '../fixtures/user';

jest.mock('axios');

describe('API', () => {
  describe('postUser', () => {
    context('when successfully user created', () => {
      beforeEach(() => {
        axios.post.mockResolvedValue({
          data: userFixture,
        });
      });

      it('responses user', async () => {
        const user = await postUser(userFixture);

        expect(user).toEqual(userFixture);
      });
    });

    context('when wrong parameter', () => {
      beforeEach(() => {
        axios.post.mockRejectedValue({
          response: {
            status: 400,
          },
        });
      });

      it('throws wrong parameter error', async () => {
        await expect(postUser({ email: '', password: '' }))
          .rejects.toThrow(new Error('잘못된 요청입니다. 파라미터를 확인해 주세요'));
      });
    });

    context('with unknown error', () => {
      beforeEach(() => {
        axios.post.mockRejectedValue(new Error('some error'));
      });

      it('throws error with message', async () => {
        await expect(postUser({})).rejects
          .toThrow(new Error('some error'));
      });
    });
  });
});
