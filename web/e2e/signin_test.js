const { signUp } = require('./api');

Feature('Sign in');

Scenario('로그인을 하면 상품 목록 페이지로 이동합니다.', async ({ I }) => {
  const user = await signUp();

  I.amOnPage('/signin');

  I.fillField('이메일', user.email);
  I.fillField('비밀번호', 'test1234');

  I.click('로그인');

  I.seeInCurrentUrl('/products');
});

Scenario('잘못된 정보로 로그인을 하면 경고창이 뜹니다', async ({ I }) => {
  const user = await signUp();

  I.amOnPage('/signin');

  I.fillField('이메일', user.email);
  I.fillField('비밀번호', '1');

  I.click('로그인');

  I.see('잘못된 요청입니다. 파라미터를 확인해 주세요');
});
