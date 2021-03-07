Feature: 유저 저장하기 & 유저 가져오기
  Scenario: 유저 저장하기
    Given 올바른 유저가 주어졌을 때
    When 유저를 저장하는 경우
    Then 유저를 찾을 수 있다

  Scenario: 아이디로 유저 가져오기
    Given 유저가 저장되었을 때
    When 아이디로 active한 유저를 가져오는 경우
    Then 유저를 가져올 수 있다

  Scenario: 아이디로 존재하지 않는 유저 가져오기
    Given 유저가 존재하지 않을 때
    When 존재하지 않는 id로 active한 유저를 가져오는 경우
    Then 유저를 가져올 수 없다

  Scenario: 이메일로 유저 가져오기
    Given 유저가 저장되었을 때
    When 이메일로 유저를 가져오는 경우
    Then 유저를 가져올 수 있다

  Scenario: 이메일로 유저 가져오기
    Given 유저가 저장되었을 때
    When 이메일로 active한 유저를 가져오는 경우
    Then 유저를 가져올 수 있다

  Scenario: 이메일로 deactive한 유저 가져오기
    Given active하지 않은 유저가 저장되었을 때
    When 이메일로 active한 유저를 가져오는 경우
    Then 유저를 가져올 수 없다

  Scenario: 아이디 deactive한 유저 가져오기
    Given active하지 않은 유저가 저장되었을 때
    When 아이디로 active한 유저를 가져오는 경우
    Then 유저를 가져올 수 없다

  Scenario: 이메일이 등록되었는지 확인하기
    Given 유저가 저장되었을 때
    When 저장된 user 의 이메일이 존재하는지 확인한다면
    Then 이메일이 존재한다

  Scenario: 이메일이 등록되었는지 확인하기
    Given 유저가 존재하지 않을 때
    When 이메일이 존재하는지 확인한다면
    Then 이메일이 존재하지 않는다
