import { Routes, Route, Navigate } from 'react-router-dom';

import ProductsPage from './pages/ProductsPage';
import ProductRegisterPage from './pages/ProductRegisterPage';
import ProductDetailPage from './pages/ProductDetailPage';
import SignUpPage from './pages/SignUpPage';
import SignInPage from './pages/SignInPage';

export default function App() {
  return (
    <>
      <header>
        <h1>고양이 장난감 가게</h1>
      </header>
      <Routes>
        <Route exact path="/products" element={<ProductsPage />} />
        <Route path="/products/product" element={<ProductRegisterPage />} />
        <Route path="/products/:id" element={<ProductDetailPage />} />
        <Route path="/signup" element={<SignUpPage />} />
        <Route path="/signin" element={<SignInPage />} />
        <Route path="*" element={<Navigate to="/products" />} />
      </Routes>
    </>
  );
}
