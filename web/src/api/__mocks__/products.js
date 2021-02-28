import { productsFixture } from '../../fixtures/products';

export const fetchProducts = async () => productsFixture;

export const fetchProduct = async () => productsFixture[0];

export const postProduct = jest.fn();

export const deleteProduct = jest.fn();

export const putProduct = jest.fn();
