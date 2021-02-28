import { render, fireEvent } from '@testing-library/react';

import { userFixture } from '../../fixtures/user';

import SignUpForm from './SignUpForm';

describe('SignUpForm', () => {
  const handleChange = jest.fn();
  const handleSubmit = jest.fn();

  const renderSignUpForm = () => render((
    <SignUpForm
      buttonText={given.buttonText}
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
    const { getByLabelText } = renderSignUpForm();

    expect(getByLabelText('이름')).toBeInTheDocument();
    expect(getByLabelText('이메일')).toBeInTheDocument();
    expect(getByLabelText('비밀번호')).toBeInTheDocument();
  });

  it('renders value', () => {
    const { getByDisplayValue } = renderSignUpForm();

    expect(getByDisplayValue(given.user.name)).toBeInTheDocument();
    expect(getByDisplayValue(given.user.email)).toBeInTheDocument();
    expect(getByDisplayValue(given.user.password)).toBeInTheDocument();
  });

  context('when button text is given', () => {
    beforeEach(() => {
      given('buttonText', () => '수정');
    });

    it('renders button with text', () => {
      const { getByText } = renderSignUpForm();

      expect(getByText('수정')).toBeInTheDocument();
    });
  });

  describe('Changing value', () => {
    it('calls onChange handler', () => {
      const { getByLabelText } = renderSignUpForm();

      fireEvent.change(getByLabelText('이름'), {
        target: { value: '떼껄룩' },
      });

      expect(handleChange).toBeCalledWith({
        name: 'name',
        value: '떼껄룩',
      });
    });
  });

  describe('Clicking submit button', () => {
    it('calls onSubmit handler', () => {
      const { getByText } = renderSignUpForm();

      fireEvent.click(getByText('등록'));

      expect(handleSubmit).toBeCalled();
    });
  });
});
