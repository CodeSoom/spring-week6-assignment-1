# 로그인 만들기

![demo mov](https://user-images.githubusercontent.com/14071105/109416536-2e492180-7a02-11eb-9085-2b352d9a222f.gif)

## 과제 목표

지금은 모든 기능을 로그인을 하지 않고 사용할 수 있습니다. 고양이 장난감을 새로 등록하거나 수정, 삭제하는 기능은 인증된 사용자만이 사용할 수 있어야 합니다. JWT를 이용해서 인증을 구현하여 로그인을 만들고 인증된 사람만 기능을 사용할 수 있도록 만들어주세요.

### 로그인이 필요없는 API

* 로그인 - `POST /session`
* 회원 생성하기 - `POST /users`
* 회원 수정하기 - `POST /users/{id}`
* 회원 삭제하기 - `DELETE /users/{id}`
* 고양이 장난감 목록 얻기 - `GET /products`
* 고양이 장난감 상세 조회하기 - `GET /products/{id}`

### 로그인이 필요한 API

* 고양이 장난감 등록하기 - `POST /products`
* 고양이 장난감 수정하기 - `PATCH /products/{id}`
* 고양이 장난감 삭제하기 - `DELETE /products/{id}`

## 요구 사항

- 테스트 커버리지 100%를 달성해야 합니다.
- 모든 API 테스트를 통과해야 합니다.
- 모든 E2E 테스트를 통과해야 합니다.

## API 실행하기

```bash
./gradlew run
```

## 웹 실행하기

### 설치

```bash
$ cd web
$ npm install
```

### 실행

```bash
$ npm start
```

브라우저 `http://localhost:9000`로 열면 실행된 것을 확인할 수 있습니다.

## 테스트

### Spring 테스트 실행

```bash
$ ./gradlew test
```

### 커버리지 확인하기

테스트를 실행하면 자동으로 커버리지 정보를 수집하여 저장합니다. 커버리지 정보는 `app/build/reports`
폴더에 저장됩니다. 커버리지 정보를 확인하려면 `app/build/reports/jacoco/test/html/index.html`파일을
브라우저에서 열면 확인할 수 있습니다.

### API 테스트 설치하기

```bash
$ cd tests
$ npm install
```

### API 테스트 실행

테스트는 실제로 동작하는 서버에 테스트하므로 서버가 동작하고 있는 상태여야 올바르게 동작합니다.

```bash
$ npm test
```

### E2E 테스트 실행하기

E2E테스트는 실제로 동작하는 서버와 실제로 동작하는 웹이 필요한 테스트하므로 서버가 동작하고, 웹 서버가 동작하고 있는 상태여야 올바르게 동작합니다.

```bash
$ cd web
$ npm run e2e
```
