Feature: 유저 인증하기
  Scenario: 올바른 유저가 인증을 하는 경우
    Given 이미 생성된 유저의 email와 password가 주어졌을 때
    When 인증을 하게된다면
    Then 올바른 유저로 인증된다
  Scenario: 올바른 유저가 잘못된 비밀번호로 인증을 하는 경우
    Given 이미 생성된 유저의 email와 password가 주어졌을 때
    When 잘못된 password로 인증을 하게된다면
    Then 에러가 발생한다
  Scenario: 올바르지 않은 유저가 인증을 하는 경우
    Given 올바르지 않은 email와 password가 주어졌을 때
    When 인증을 하게된다면
    Then 에러가 발생한다
  Scenario: 올바르게 발급된 토큰을 인증한다
    Given 올바른 토큰이 주어졌을 때
    When 토큰을 인증한다면
    Then 에러가 발생하지 않는다
  Scenario: 올바르지 않은 형태의 토큰을 인증한다
    Given 올바르지 않은 형태의 토큰이 주어졌을 때
    When 토큰을 인증한다면
    Then 에러가 발생한다
  Scenario: 발급되지 않은 토큰을 인증한다
    Given 발급되지 않은 토큰이 주어졌을 때
    When 토큰을 인증한다면
    Then 에러가 발생한다
