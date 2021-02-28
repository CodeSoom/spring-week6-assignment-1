const { deleteAll, create } = require('./api');
const mockProduct = require('./fixtures/product');

const product = {
  name: '털뭉치',
  maker: '애옹이네 장난감',
  price: 2000,
  imageUrl: 'https://cdn.pixabay.com/photo/2018/10/05/12/09/animal-3725762_960_720.jpg',
};

Feature('Product detail');

Before(async () => {
  await deleteAll();
});

Scenario('로그인을 안한 상태에서 수정하려고 하면, 경고창이 뜹니다', async ({ I }) => {
  const { id } = await create(mockProduct);

  I.amOnPage(`/products/${id}`);

  I.click('수정하기');

  I.fillField('이름', product.name);
  I.fillField('메이커', product.maker);
  I.fillField('가격', product.price);
  I.fillField('이미지', product.imageUrl);

  I.click('수정');

  I.see('로그인이 필요한 기능입니다');
});

Scenario('장난감 상세 페이지에서 수정 버튼을 누르면 상품을 입력할 수 있다.', async ({ I }) => {
  const { id } = await create(mockProduct);

  I.login();

  I.amOnPage(`/products/${id}`);

  I.click('수정하기');

  I.fillField('이름', product.name);
  I.fillField('메이커', product.maker);
  I.fillField('가격', product.price);
  I.fillField('이미지', product.imageUrl);

  I.click('수정');

  I.see(product.name);
});
