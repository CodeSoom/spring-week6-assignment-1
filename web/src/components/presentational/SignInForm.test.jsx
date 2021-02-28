import { render, fireEvent } from '@testing-library/react';

import { userFixture } from '../../fixtures/user';

import SignInForm from './SignInForm';

describe('SignInForm', () => {
  const handleChange = jest.fn();
  const handleSubmit = jest.fn();

  const renderSignInForm = () => render((
    <SignInForm
      user={given.user}
      onChange={handleChange}
      onSubmit={handleSubmit}
    />
  ));

  beforeEach(() => {
    jest.clearAllMocks();
  });

  beforeEach(() => {
    given('user', () => userFixture);
  });

  it('renders', () => {
    const { getByLabelText } = renderSignInForm();

    expect(getByLabelText('이메일')).toBeInTheDocument();
    expect(getByLabelText('비밀번호')).toBeInTheDocument();
  });

  it('renders value', () => {
    const { getByDisplayValue } = renderSignInForm();

    expect(getByDisplayValue(given.user.email)).toBeInTheDocument();
    expect(getByDisplayValue(given.user.password)).toBeInTheDocument();
  });

  describe('Changing value', () => {
    const value = 'user@email.com';

    it('calls onChange handler', () => {
      const { getByLabelText } = renderSignInForm();

      fireEvent.change(getByLabelText('이메일'), {
        target: { value },
      });

      expect(handleChange).toBeCalledWith({
        name: 'email',
        value,
      });
    });
  });

  describe('Clicking submit button', () => {
    it('calls onSubmit handler', () => {
      const { getByText } = renderSignInForm();

      fireEvent.click(getByText('로그인'));

      expect(handleSubmit).toBeCalled();
    });
  });
});
