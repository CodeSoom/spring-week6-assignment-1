JWT: JSON Web Token
- 여러 암호화 토큰 중 한 종료

jwt 토큰(난수): Data 를 품고 있다. 

로그인 했다는 세션을 보관

256 / 8 = 32 글


`assertThat(claims.get("userId", Long.class)).isEqualTo(1L);` : userId 값이 Long 으로 지정돼있기에, Long type 써준다.


token을 헤더로 전달한다.


bearer Token


### @Value annotation
- `@Value` 어노테이션은 별도 파일에 지정된 값을 가져와, field, setter, constructor 등에 주입할 수 있습니다.
- 이때 별도 파일은 application.yml(혹은 application.properties) 에 해당됩니다.
- 즉 `@Value` 의 인자를 key 로 사용하여 application.yml 의 특정 값을 호출 할 수 있습니다.

<hr/>

- 왜 굳이 별도 파일에서 값을 지정하느냐, 곧 바로 field, setter, constructor 에 정의하면 되는 것 아니냐? 는 의문이 들 수 있습니다.
- 가장 큰 이유 중 하나는, 프로퍼티 값을 공개적으로 드러내면 안되기 때문입니다. 아래 예제를 보겠습니다.
``` java
private final Key key;
public JwtUtil(@Value("${jwt.secret}") String secret) {
    key = Keys.hmacShaKeyFor(secret.getBytes());
}
```
- `@Value` 어노테이션 내부는 JWT 값을 나타내는 변수로 지정돼 있습니다. (이 변수는 application.yml 에서 정의됨) 
- 즉 JWT 값을 그대로 드러내지 않고, jwt.secret 변수에 정의된 값을 가져와 사용할 수 있습니다.
<hr/>
정리하자면, 
- `@Value` 어노테이션을 사용하면
    1) 프로퍼티 값을 주입할 때 편리한 방법을 제공합니다.
    2) 또한 jwt 처럼 민감한 정보가 담긴 값을 `은닉`하여 제공할 수 있습니다.



``` java
// pass
if (accessToken == null || accessToken.isBlank()) {
    throw new InvalidAccessTokenException(accessToken);
}
       
// failed 
if (accessToken.isBlank() || accessToken == null) {
    throw new InvalidAccessTokenException(accessToken);
}
```

isEmpty, isBlank 차이 확실히

자바독스 작성


새삼 느끼는데, 웹 화면은 일절 띄우지 않고, 모조리 테스트 코드로만 성능을 테스트 하고 있다. 신기하네


``` java
public Long parseToken(String accessToken) {
    return null;
}// 이 상태에서 parseToken() test 돌리면 null 이 뜬다. 이걸 이해하자.
```


// authenticationService.test
``` java
// 여긴 이제 지워도 된다.
@Test
void parseTokenWithBlankToken(){
    assertThatThrownBy(() -> authenticationService.parseToken("")
    ).isInstanceOf(InvalidTokenException.class);

    assertThatThrownBy(() -> authenticationService.parseToken(" ")
    ).isInstanceOf(InvalidTokenException.class);

    assertThatThrownBy(() -> authenticationService.parseToken(null)
    ).isInstanceOf(InvalidTokenException.class);
}
```


``` java
@MockBean
AuthenticationService authenticationService; // ProductControllerTest :  Failed to load ApplicationContext 의 원인
```


BDD, `@Nested` 구조 사용해서 테스트 코드 작성할 것. 