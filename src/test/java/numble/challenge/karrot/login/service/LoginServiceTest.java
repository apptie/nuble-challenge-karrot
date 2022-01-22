package numble.challenge.karrot.login.service;

import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.repository.MemberRepository;
import numble.challenge.karrot.member.utils.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginService 테스트")
public class LoginServiceTest {
    @Mock
    private MemberRepository memberRepository;

    private LoginService loginService;

    @BeforeEach
    void beforeEach() {
        this.loginService = new LoginServiceImpl(memberRepository);
    }

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {
        @Test
        @DisplayName("성공 테스트")
        void loginSuccessTest() {
            doReturn(
                    Optional.of(
                            Member.builder()
                                    .id(1L)
                                    .email("test@user.com")
                                    .password("1234")
                                    .name("a")
                                    .phoneNumber("01012345678")
                                    .nickname("a")
                                    .profile(null)
                                    .status(MemberStatus.인증대기)
                                    .uuid(UUID.randomUUID().toString())
                                    .place("서울시 강서구")
                                    .build()
                    )
            ).when(memberRepository).findByEmail(anyString());

            LoginMemberForm login = loginService.login("test@user.com", "1234");

            assertThat(login.getId()).isSameAs(1L);
            assertThat(login.getEmail()).isEqualTo("test@user.com");
            assertThat(login.getNickname()).isEqualTo("a");
        }

        @Test
        @DisplayName("실패 테스트 - 없는 이메일")
        void loginFailNotEmailTest() {
            doReturn(Optional.ofNullable(null)).when(memberRepository).findByEmail(anyString());

            LoginMemberForm login = loginService.login("noUserId", "1234");
            assertThat(login).isSameAs(null);
        }

        @Test
        @DisplayName("실패 테스트 - 일치하지 않는 비밀번호")
        void loginFailNotPasswordTest() {
            doReturn(Optional.ofNullable(null)).when(memberRepository).findByEmail(anyString());

            LoginMemberForm login = loginService.login("test@user.com", "notUserPassword");
            assertThat(login).isSameAs(null);
        }
    }

}
