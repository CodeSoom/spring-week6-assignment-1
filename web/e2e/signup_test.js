Feature('Sign up');

const newUser = () => ({
  name: '홍길동',
  email: `${new Date().getTime()}@email.com`,
  password: 'test1234',
});

Scenario('회원가입버튼을 클릭하면 회원가입 페이지로 이동합니다', async ({ I }) => {
  I.amOnPage('/products');

  I.click('회원가입');

  I.seeInCurrentUrl('/signup');
});

Scenario('회원가입을 하면 상품 목록 페이지로 이동합니다.', async ({ I }) => {
  const user = newUser();

  I.amOnPage('/signup');

  I.fillField('이름', user.name);
  I.fillField('이메일', user.email);
  I.fillField('비밀번호', user.password);

  I.click('등록');

  I.seeInCurrentUrl('/products');
});

Scenario('잘못된 값으로 회원가입을 하면, 경고창이 뜹니다.', async ({ I }) => {
  const user = newUser();

  I.amOnPage('/signup');

  I.fillField('이름', user.name);
  I.fillField('이메일', user.email);
  I.fillField('비밀번호', '');

  I.click('등록');

  I.see('잘못된 요청입니다. 파라미터를 확인해 주세요');
});
