import Input from './Input';

export default function SignUpForm({
  buttonText = '등록',
  user,
  onChange,
  onSubmit,
}) {
  const { name, email, password } = user;

  const handleSubmit = (e) => {
    e.preventDefault();

    onSubmit();
  };

  return (
    <form onSubmit={handleSubmit}>
      <Input
        id="name"
        label="이름"
        placeholder="이름을 입력해주세요"
        value={name}
        onChange={onChange}
      />
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
      <button type="submit">{buttonText}</button>
    </form>
  );
}
