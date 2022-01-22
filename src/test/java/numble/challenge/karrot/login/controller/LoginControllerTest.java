package numble.challenge.karrot.login.controller;

import numble.challenge.karrot.common.session.SessionConst;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.repository.MemberRepository;
import numble.challenge.karrot.member.utils.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DisplayName("LoginController 테스트")
public class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberRepository memberRepository;

    private MultiValueMap<String, String> params;

    @BeforeEach
    void beforeEach() {
        this.params = new LinkedMultiValueMap<>();
    }

    @Nested
    @DisplayName("GET:/login")
    class LoginFormTest {
        @Test
        @DisplayName("성공 테스트")
        void loginFormTest() throws Exception {
            mvc.perform(MockMvcRequestBuilders.post("/login"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attributeExists("loginForm"))
                    .andExpect(view().name("login/login-form"));
        }
    }

    @Nested
    @DisplayName("POST:/login")
    class LoginTest {
        @Test
        @DisplayName("성공 테스트")
        void loginSuccessTest() throws Exception {
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
                                    .status(MemberStatus.인증완료)
                                    .uuid("uuid")
                                    .place("서울시 강서구")
                                    .build()
                    )
            ).when(memberRepository).findByEmail(anyString());

            params.add("email", "test@user.com");
            params.add("password", "1234");

            mvc.perform(MockMvcRequestBuilders.post("/login").params(params))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(header().string("Location", "/"));
        }

        @Test
        @DisplayName("실패 테스트 - 아이디 / 비밀번호를 입력하지 않음")
        void loginFailNotIdPasswordTest() throws Exception {
            mvc.perform(MockMvcRequestBuilders.post("/login"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attributeHasFieldErrors("loginForm", "email", "password"))
                    .andExpect(view().name("login/login-form"));

            mvc.perform(MockMvcRequestBuilders.post("/login"))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeHasFieldErrors("loginForm", "email", "password"))
                    .andExpect(view().name("login/login-form"));
        }

        @Test
        @DisplayName("실패 테스트 - 올바르지 않은 비밀번호")
        void loginFailNotPasswordCorrectTest() throws Exception {
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
                                    .status(MemberStatus.인증완료)
                                    .uuid("uuid")
                                    .place("서울시 강서구")
                                    .build()
                    )
            ).when(memberRepository).findByEmail(anyString());

            params.add("email", "test@user.com");
            params.add("password", "잘못입력한비밀번호");

            mvc.perform(MockMvcRequestBuilders.post("/login").params(params))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("error", "loginError"))
                    .andExpect(view().name("login/login-form"));
        }

        @Test
        @DisplayName("실패 테스트 - 없는 이메일")
        void loginFailNotEmailTest() throws Exception {
            doReturn(Optional.ofNullable(null)).when(memberRepository).findByEmail(anyString());

            params.add("email", "test@user.com");
            params.add("password", "1234");

            mvc.perform(MockMvcRequestBuilders.post("/login").params(params))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("error", "loginError"))
                    .andExpect(view().name("login/login-form"));
        }

        @Test
        @DisplayName("실패 테스트 - 메일 인증되지 않은 이메일")
        void loginFailNotMailValidateTest() throws Exception {
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
                                    .uuid("uuid")
                                    .place("서울시 강서구")
                                    .build()
                    )
            ).when(memberRepository).findByEmail(anyString());

            params.add("email", "test@user.com");
            params.add("password", "1234");

            mvc.perform(MockMvcRequestBuilders.post("/login").params(params))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("member/mail-not-check"));
        }
    }

    @Nested
    @DisplayName("/logout")
    class LogoutTest {
        @Test
        @DisplayName("성공 테스트")
        void logoutSuccessTest() throws Exception {
            LoginMemberForm form = LoginMemberForm.builder()
                    .id(1L)
                    .email("test@user.com")
                    .nickname("a")
                    .profile(null)
                    .status(MemberStatus.인증완료)
                    .verifyDate(LocalDateTime.now())
                    .build();

            MockHttpSession session = new MockHttpSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, form);

            mvc.perform(MockMvcRequestBuilders.get("/logout").session(session))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(header().string("Location", "/"));
        }
    }
}
