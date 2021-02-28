import axios from 'axios';

import { postSession } from './session';

jest.mock('axios');

describe('Session', () => {
  context('when success', () => {
    beforeEach(() => {
      axios.post.mockResolvedValue({
        data: {
          accessToken: 'token',
        },
      });
    });

    it('responses accessToken', async () => {
      const accessToken = await postSession({
        email: 'test@email.com',
        password: 'test1234',
      });

      expect(accessToken).toBe('token');
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
      await expect(postSession({ email: '', password: '' }))
        .rejects.toThrow(new Error('잘못된 요청입니다. 파라미터를 확인해 주세요'));
    });
  });

  context('with unknown error', () => {
    beforeEach(() => {
      axios.post.mockRejectedValue(new Error('some error'));
    });

    it('throws error with message', async () => {
      await expect(postSession({})).rejects
        .toThrow(new Error('some error'));
    });
  });
});
