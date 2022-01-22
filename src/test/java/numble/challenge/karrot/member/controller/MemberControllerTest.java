package numble.challenge.karrot.member.controller;

import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.utils.BoardStatus;
import numble.challenge.karrot.common.session.SessionConst;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.repository.MemberRepository;
import numble.challenge.karrot.member.utils.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DisplayName("MemberController 테스트")
public class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberRepository memberRepository;

    private MultiValueMap<String, String> params;
    private LoginMemberForm form;
    private MockHttpSession session;

    @BeforeEach
    void beforeEach() {
        this.params = new LinkedMultiValueMap<>();

        this.form = LoginMemberForm.builder()
                .id(1L)
                .email("test@user.com")
                .nickname("a")
                .profile(null)
                .status(MemberStatus.인증완료)
                .verifyDate(LocalDateTime.now())
                .build();

        this.session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, form);
    }

    @Test
    @DisplayName("GET:/members/new")
    void joinGetTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/members/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("joinMemberForm"))
                .andExpect(view().name("member/join-form"));
    }

    @Nested
    @DisplayName("POST:/members/new")
    class joinPostTest {
        @Test
        @DisplayName("성공 테스트")
        void joinPostSuccessTest() throws Exception {
            params.add("email", "test@user.com");
            params.add("password", "1234");
            params.add("name", "b");
            params.add("phoneNumber", "01012345678");
            params.add("nickname", "b");
            params.add("place", "서울시 강서구");
            params.add("ip", "112.172.223.71");

            mvc.perform(MockMvcRequestBuilders.post("/members/new").params(params))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("email", "test@user.com"));
        }

        @Test
        @DisplayName("실패 테스트 - 정보 공백 입력")
        void joinPostFailDuplicateTest() throws Exception {
            params.add("ip", "112.172.223.71");

            mvc.perform(MockMvcRequestBuilders.post("/members/new").params(params))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attributeHasFieldErrors("joinMemberForm", "email", "password", "name", "phoneNumber", "nickname", "place"));
        }

        @Test
        @DisplayName("실패 테스트 - 6km 이상 떨어진 지역")
        void joinPostFailFarPlaceTest() throws Exception {
            params.add("email", "test@user.com");
            params.add("password", "1234");
            params.add("name", "b");
            params.add("phoneNumber", "01012345678");
            params.add("nickname", "b");
            params.add("place", "서울시 강남구");
            params.add("ip", "112.172.223.71");

            mvc.perform(MockMvcRequestBuilders.post("/members/new").params(params))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attributeHasFieldErrors("joinMemberForm", "place"));
        }

        @Test
        @DisplayName("실패 테스트 - 잘못되거나 인식할 수 없는 지역")
        void joinPostFailWrongPlaceTest() throws Exception {
            params.add("email", "test@user.com");
            params.add("password", "1234");
            params.add("name", "b");
            params.add("phoneNumber", "01012345678");
            params.add("nickname", "b");
            params.add("place", "아무데도없는그런지역");
            params.add("ip", "112.172.223.71");

            mvc.perform(MockMvcRequestBuilders.post("/members/new").params(params))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attributeExists("message"))
                    .andExpect(view().name("error/exception-page"));
        }
    }

    @Nested
    @DisplayName("GET:/members")
    class myPageTest {
        @Test
        @DisplayName("성공 테스트")
        void myPageSuccessTest() throws Exception {
            mvc.perform(MockMvcRequestBuilders.get("/members").session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attributeExists("form"))
                    .andExpect(view().name("member/my-page"));
        }

        @Test
        @DisplayName("실패 테스트 - 로그인 하지 않음")
        void myPageNotLoginTest() throws Exception {
            mvc.perform(MockMvcRequestBuilders.get("/members"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(header().string("Location", "/login?redirectURL=/members"));
        }
    }

    @Nested
    @DisplayName("GET:/members/profile")
    class profileGetTest {
        @Test
        @DisplayName("성공 테스트")
        void myPageTest() throws Exception {
            mvc.perform(MockMvcRequestBuilders.get("/members/profile").session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attributeExists("memberForm"))
                    .andExpect(view().name("member/profile-form"));
        }

        @Test
        @DisplayName("실패 테스트 - 로그인 하지 않음")
        void profileNotLoginTest() throws Exception {
            mvc.perform(MockMvcRequestBuilders.get("/members/profile"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(header().string("Location", "/login?redirectURL=/members/profile"));
        }
    }

    @Nested
    @DisplayName("POST:/members/profile")
    class profilePostTest {
        @Test
        @DisplayName("성공 테스트")
        void profilePostSuccessTest() throws Exception {
            doReturn(Optional.ofNullable(null)).when(memberRepository).findByNickname(anyString());

            doReturn(
                    Optional.of(
                            Member.builder()
                                    .id(2L)
                                    .email("test2@user.com")
                                    .password("1234")
                                    .name("b")
                                    .phoneNumber("01012345678")
                                    .nickname("b")
                                    .profile(null)
                                    .status(MemberStatus.인증완료)
                                    .uuid(UUID.randomUUID().toString())
                                    .place("서울시 강서구")
                                    .build()
                    )
            ).when(memberRepository).findById(anyLong());

            LoginMemberForm anotherForm = LoginMemberForm.builder()
                    .id(2L)
                    .email("test2@user.com")
                    .nickname("b")
                    .profile(null)
                    .status(MemberStatus.인증완료)
                    .verifyDate(LocalDateTime.now())
                    .build();

            session.setAttribute(SessionConst.LOGIN_MEMBER, anotherForm);

            params.add("profile", "profile.png");
            params.add("nickname", "c");
            params.add("email", "test2@user.com");

            mvc.perform(MockMvcRequestBuilders.post("/members/profile").session(session).params(params))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(header().string("Location", "/members"))
                    .andExpect(view().name("redirect:/members"));
        }

        @Test
        @DisplayName("실패 테스트 - 중복 닉네임")
        void profilePostFailEmailDuplicateTest() throws Exception {
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
                                .uuid(UUID.randomUUID().toString())
                                .place("서울시 강서구")
                                .build()
                    )
            ).when(memberRepository).findByNickname(anyString());

            doReturn(
                    Optional.of(
                            Member.builder()
                                    .id(2L)
                                    .email("test2@user.com")
                                    .password("1234")
                                    .name("b")
                                    .phoneNumber("01012345678")
                                    .nickname("b")
                                    .profile(null)
                                    .status(MemberStatus.인증완료)
                                    .uuid(UUID.randomUUID().toString())
                                    .place("서울시 강서구")
                                    .build()
                    )
            ).when(memberRepository).findById(anyLong());

            LoginMemberForm anotherForm = LoginMemberForm.builder()
                    .id(2L)
                    .email("test2@user.com")
                    .nickname("b")
                    .profile(null)
                    .status(MemberStatus.인증완료)
                    .verifyDate(LocalDateTime.now())
                    .build();

            session.setAttribute(SessionConst.LOGIN_MEMBER, anotherForm);

            params.add("profile", "profile.png");
            params.add("nickname", "a");
            params.add("email", "test2@user.com");

            mvc.perform(MockMvcRequestBuilders.post("/members/profile").session(session).params(params))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attributeExists("memberForm"))
                    .andExpect(model().attributeHasFieldErrors("memberForm", "nickname"))
                    .andExpect(view().name("member/profile-form"));
        }

        @Test
        @DisplayName("실패 테스트 - 닉네임 입력값 없음")
        void profilePostFailEmailNoValueTest() throws Exception {
            doReturn(null).when(memberRepository).findByNickname(anyString());

            doReturn(
                    Optional.of(
                            Member.builder()
                                    .id(2L)
                                    .email("test2@user.com")
                                    .password("1234")
                                    .name("b")
                                    .phoneNumber("01012345678")
                                    .nickname("b")
                                    .profile(null)
                                    .status(MemberStatus.인증완료)
                                    .uuid(UUID.randomUUID().toString())
                                    .place("서울시 강서구")
                                    .build()
                    )
            ).when(memberRepository).findById(anyLong());

            LoginMemberForm anotherForm = LoginMemberForm.builder()
                    .id(2L)
                    .email("test2@user.com")
                    .nickname("b")
                    .profile(null)
                    .status(MemberStatus.인증완료)
                    .verifyDate(LocalDateTime.now())
                    .build();

            session.setAttribute(SessionConst.LOGIN_MEMBER, anotherForm);

            params.add("profile", "profile.png");
            params.add("email", "test2@user.com");


            mvc.perform(MockMvcRequestBuilders.post("/members/profile").session(session).params(params))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attributeExists("memberForm"))
                    .andExpect(model().attributeHasFieldErrors("memberForm", "nickname"))
                    .andExpect(view().name("member/profile-form"));
        }
    }

    @Nested
    @DisplayName("GET:/members/boards")
    class MemberBoardTest {
        @Test
        @DisplayName("성공 테스트")
        void myBoardSuccessTest() throws Exception {
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
                                    .uuid(UUID.randomUUID().toString())
                                    .place("서울시 강서구")
                                    .boardList(
                                            List.of(
                                                    Board.builder()
                                                            .title("게시글 1")
                                                            .place("서울시 강서구")
                                                            .price(1)
                                                            .replyCount(0)
                                                            .interestCount(0)
                                                            .status(BoardStatus.판매중)
                                                            .id(1L)
                                                            .thumbnail("썸네일 1")
                                                            .build(),

                                                    Board.builder()
                                                            .title("게시글 2")
                                                            .place("서울시 강서구")
                                                            .price(2)
                                                            .replyCount(0)
                                                            .interestCount(0)
                                                            .status(BoardStatus.판매중)
                                                            .id(2L)
                                                            .thumbnail("썸네일 2")
                                                            .build(),

                                                    Board.builder()
                                                            .title("게시글 3")
                                                            .place("서울시 강서구")
                                                            .price(3)
                                                            .replyCount(0)
                                                            .interestCount(0)
                                                            .status(BoardStatus.판매중)
                                                            .id(3L)
                                                            .thumbnail("썸네일 3")
                                                            .build()
                                            )

                                    )
                                    .build()
                    )
            ).when(memberRepository).findById(anyLong());

            mvc.perform(MockMvcRequestBuilders.get("/members/boards").session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attributeExists("list", "memberId"))
                    .andExpect(view().name("member/my-board-list"));
        }

        @Test
        @DisplayName("실패 테스트 - 로그인 하지 않음")
        void myBoardFailNotLoginTest() throws Exception {
            mvc.perform(MockMvcRequestBuilders.get("/members/boards"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(header().string("Location", "/login?redirectURL=/members/boards"));
        }
    }

    @Nested
    @DisplayName("GET:/members/check-mail/{email}/{uuid}")
    class CheckEmailUUID {
        @Test
        @DisplayName("성공 테스트")
        void checkEmailUUIDSuccessTest() throws Exception {
            String uuid = UUID.randomUUID().toString();

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
                                    .uuid(uuid)
                                    .place("서울시 강서구")
                                    .build()
                    )
            ).when(memberRepository).findByEmail(anyString());

            mvc.perform(MockMvcRequestBuilders.get("/members/check-mail/{email}/{uuid}", "test@user.com", uuid))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("flag", true))
                    .andExpect(view().name("member/mail-check"));


        }

        @Test
        @DisplayName("실패 테스트 - uuid 일치하지 않음")
        void checkEmailUUIDFailTest() throws Exception {
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
                                    .uuid("다른 UUID")
                                    .place("서울시 강서구")
                                    .build()
                    )
            ).when(memberRepository).findByEmail(anyString());

            mvc.perform(MockMvcRequestBuilders.get("/members/check-mail/{email}/{uuid}", "test@user.com", "또다른 UUID"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("flag", false))
                    .andExpect(view().name("member/mail-check"));
        }
    }

    @Test
    @DisplayName("GET:/members/not-verify-place")
    void notVerifyPlacePageTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/members/not-verify-place"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("member/not-verify-place"));
    }

}
