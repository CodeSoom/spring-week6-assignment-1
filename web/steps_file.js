// in this file you can append custom step methods to 'I' object

const newUser = () => ({
  name: '홍길동',
  email: `${new Date().getTime()}@email.com`,
  password: 'test1234',
});

module.exports = () => actor({
  login() {
    const user = newUser();

    this.amOnPage('/signup');

    this.fillField('이름', user.name);
    this.fillField('이메일', user.email);
    this.fillField('비밀번호', user.password);

    this.click('등록');

    this.amOnPage('/signin');

    this.fillField('이메일', user.email);
    this.fillField('비밀번호', user.password);

    this.click('로그인');
  },
  // Define custom steps here, use 'this' to access default methods of I.
  // It is recommended to place a general 'login' function here.

});
