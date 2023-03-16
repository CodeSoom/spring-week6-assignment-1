package com.codesoom.assignment.interceptor;

import com.codesoom.assignment.annotation.CheckJwtToken;
import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.errors.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.assertThat;

class LoginCheckInterceptorTest {

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjF9.ZZ3CUl0jxeLGvQ1Js5nG2Ty5qGTlqai5ubDMXZOdaDk";
    private static final String INVALID_TOKEN = VALID_TOKEN + "WRONG";

    AuthenticationService authenticationService;

    LoginCheckInterceptor loginCheckInterceptor;
    

    class MockHandler{
        @CheckJwtToken
        public void handleRequest(){

        }
    }

    @BeforeEach
    void setUp(){
        authenticationService = mock(AuthenticationService.class);
        loginCheckInterceptor = new LoginCheckInterceptor(authenticationService);

        given(authenticationService.parseToken(VALID_TOKEN)).willReturn(1L);
        given(authenticationService.parseToken(INVALID_TOKEN)).willThrow(InvalidTokenException.class);
    }

    @Nested
    class getAccessToken{

        @Nested
        class WhenAuthorizationIsEmpty{

            @Test
            void throwsInvalidTokenException(){

                // Mock HttpServletRequest 생성
                MockHttpServletRequest request = new MockHttpServletRequest();

                // 빈 값으로 요청 시 인터셉터에서 InvalidTokenException 예외처리
                assertThatThrownBy(() -> loginCheckInterceptor.getAccessToken(request))
                        .isInstanceOf(InvalidTokenException.class);
            }
        }

        @Nested
        class WhenAuthorizationIsExists{
            @Test
            void getAccessToken(){
                // Mock HttpServletRequest 생성
                MockHttpServletRequest request = new MockHttpServletRequest();

                //INVALID_TOKEN SET
                request.addHeader("Authorization","Bearer "+VALID_TOKEN);

                String token = loginCheckInterceptor.getAccessToken(request);

                assertThat(token).isEqualTo(VALID_TOKEN);
            }
        }

        @Nested
        class preHandle{

            @Nested
            class WhenMethodIsOPTIONS{

                @Test
                void returnTrue() throws Exception {
                    MockHttpServletRequest request = new MockHttpServletRequest();
                    MockHttpServletResponse response = new MockHttpServletResponse();
                    ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();

                    request.setMethod("OPTIONS");

                    boolean result = loginCheckInterceptor.preHandle(request,response,handler);

                    assertThat(result).isTrue();
                }
            }


            @Nested
            class WithHandlerHasCheckJwtToken{

                @Test
                void withValidTokenReturnTrue() throws Exception {
                    MockHttpServletRequest request = new MockHttpServletRequest();
                    MockHttpServletResponse response = new MockHttpServletResponse();

                    // 핸들러가 매핑할 클래스와 메서드명을 지정하며, 여기서는 @CheckJwtToken 어노테이션을 갖는 MockHandler 클래스의 handlerRequest를 지정하였다.
                    HandlerMethod handler = new HandlerMethod(new MockHandler(), "handleRequest");

                    request.setMethod("GET");
                    request.addHeader("Authorization", "Bearer "+VALID_TOKEN);

                    boolean result = loginCheckInterceptor.preHandle(request, response, handler);

                    assertThat(result).isTrue();
                }

                @Test
                void withInvalidTokenReturnFalse() throws Exception {
                    MockHttpServletRequest request = new MockHttpServletRequest();
                    MockHttpServletResponse response = new MockHttpServletResponse();

                    // 핸들러가 매핑할 클래스와 메서드명을 지정하며, 여기서는 @CheckJwtToken 어노테이션을 갖는 MockHandler 클래스의 handlerRequest를 지정하였다.
                    HandlerMethod handler = new HandlerMethod(new MockHandler(), "handleRequest");

                    request.setMethod("GET");
                    request.addHeader("Authorization", "Bearer "+INVALID_TOKEN);

                    // 유효하지 않은 토큰일 경우 PreHandle 메서드에서 예외가 발생하며 최종적으로 false를 리턴함.
                    boolean result = loginCheckInterceptor.preHandle(request, response, handler);

                    assertThat(result).isFalse();


                }
            }
        }
    }
}