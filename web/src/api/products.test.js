import axios from 'axios';

import {
  fetchProducts, fetchProduct, postProduct, deleteProduct, putProduct,
} from './products';

import { productsFixture } from '../fixtures/products';

jest.mock('axios');

describe('API', () => {
  describe('fetchProducts', () => {
    beforeEach(() => {
      axios.get.mockResolvedValue({
        data: productsFixture,
      });
    });

    it('responses products', async () => {
      const products = await fetchProducts();

      expect(products).toEqual(productsFixture);
    });
  });

  describe('fetchProduct', () => {
    const id = 1;

    beforeEach(() => {
      axios.get.mockResolvedValue({
        data: productsFixture[0],
      });
    });

    it('responses product', async () => {
      const product = await fetchProduct(id);

      expect(product).toEqual(productsFixture[0]);
    });
  });

  describe('postProduct', () => {
    context('with valid parameters', () => {
      beforeEach(() => {
        axios.post.mockResolvedValue({
          data: productsFixture[0],
        });
      });

      it('responses product', async () => {
        const product = await postProduct(productsFixture[0]);

        expect(product).toEqual(productsFixture[0]);
      });
    });

    context('with invalid parameters', () => {
      beforeEach(() => {
        axios.post.mockRejectedValue({
          response: {
            status: 400,
          },
        });
      });

      it('throws error with message', async () => {
        await expect(postProduct({})).rejects
          .toThrow(new Error('잘못된 요청입니다. 파라미터를 확인해 주세요'));
      });
    });

    context('without login', () => {
      beforeEach(() => {
        axios.post.mockRejectedValue({
          response: {
            status: 401,
          },
        });
      });

      it('throws error with message', async () => {
        await expect(postProduct({})).rejects
          .toThrow(new Error('로그인이 필요한 기능입니다'));
      });
    });

    context('with unknown error', () => {
      beforeEach(() => {
        axios.post.mockRejectedValue(new Error('some error'));
      });

      it('throws error with message', async () => {
        await expect(postProduct({})).rejects
          .toThrow(new Error('some error'));
      });
    });
  });

  describe('deleteProduct', () => {
    context('when successfully requested', () => {
      beforeEach(() => {
        axios.delete.mockResolvedValue({
          data: productsFixture[0],
        });
      });

      it('responses nothing', async () => {
        const result = await deleteProduct(productsFixture[0]);

        expect(result).toBeUndefined();
      });
    });

    context('without login', () => {
      beforeEach(() => {
        axios.delete.mockRejectedValue({
          response: {
            status: 401,
          },
        });
      });

      it('throws error with message', async () => {
        await expect(deleteProduct({})).rejects
          .toThrow(new Error('로그인이 필요한 기능입니다'));
      });
    });

    context('with unknown error', () => {
      beforeEach(() => {
        axios.delete.mockRejectedValue(new Error('some error'));
      });

      it('throws error with message', async () => {
        await expect(deleteProduct({})).rejects
          .toThrow(new Error('some error'));
      });
    });
  });

  describe('putProduct', () => {
    context('with valid parameters', () => {
      beforeEach(() => {
        axios.patch.mockResolvedValue({
          data: productsFixture[0],
        });
      });

      it('responses updatedProduct', async () => {
        const product = await putProduct(productsFixture[0]);

        expect(product).toEqual(productsFixture[0]);
      });
    });

    context('with invalid parameters', () => {
      beforeEach(() => {
        axios.patch.mockRejectedValue({
          response: {
            status: 400,
          },
        });
      });

      it('throws error with message', async () => {
        await expect(putProduct({})).rejects
          .toThrow(new Error('잘못된 요청입니다. 파라미터를 확인해 주세요'));
      });
    });

    context('without login', () => {
      beforeEach(() => {
        axios.patch.mockRejectedValue({
          response: {
            status: 401,
          },
        });
      });

      it('throws error with message', async () => {
        await expect(putProduct({})).rejects
          .toThrow(new Error('로그인이 필요한 기능입니다'));
      });
    });

    context('with unknown error', () => {
      beforeEach(() => {
        axios.patch.mockRejectedValue(new Error('some error'));
      });

      it('throws error with message', async () => {
        await expect(putProduct({})).rejects
          .toThrow(new Error('some error'));
      });
    });
  });
});
