Feature: 유저 인증하기
  Scenario: 올바른 유저가 인증을 하는 경우
    Given 이미 생성된 유저의 email와 password가 주어졌을 때
    When 인증을 하게된다면
    Then 올바른 유저로 인증된다
