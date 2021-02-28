import Input from './Input';

export default function SignInForm({
  user,
  onChange,
  onSubmit,
}) {
  const { email, password } = user;

  const handleSubmit = (e) => {
    e.preventDefault();

    onSubmit();
  };

  return (
    <form onSubmit={handleSubmit}>
      <Input
        id="email"
        label="이메일"
        type="email"
        placeholder="이메일을 입력해주세요"
        value={email}
        onChange={onChange}
      />
      <Input
        id="password"
        label="비밀번호"
        type="password"
        placeholder="비밀번호를 입력해주세요"
        value={password}
        onChange={onChange}
      />
      <button type="submit">로그인</button>
    </form>
  );
}
