import { Switch, Route, Redirect } from 'react-router-dom';

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
      <Switch>
        <Route exact path="/products" component={ProductsPage} />
        <Route path="/products/product" component={ProductRegisterPage} />
        <Route path="/products/:id" component={ProductDetailPage} />
        <Route path="/signup" component={SignUpPage} />
        <Route path="/signin" component={SignInPage} />
        <Redirect to="/products" />
      </Switch>
    </>
  );
}
