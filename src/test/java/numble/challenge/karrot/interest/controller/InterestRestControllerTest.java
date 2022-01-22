package numble.challenge.karrot.interest.controller;

import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.repository.BoardRepository;
import numble.challenge.karrot.board.utils.BoardStatus;
import numble.challenge.karrot.board.utils.Category;
import numble.challenge.karrot.common.session.SessionConst;
import numble.challenge.karrot.interest.entity.Interest;
import numble.challenge.karrot.interest.repository.InterestRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DisplayName("InterestRestController 테스트")
public class InterestRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private BoardRepository boardRepository;

    @MockBean
    private InterestRepository interestRepository;

    private LoginMemberForm loginMemberform;
    private Board board;
    private Member member;
    private Interest interest;
    private MockHttpSession session;

    @BeforeEach
    void beforeEach() {
        this.board = Board.builder()
                .id(1L)
                .title("게시글 1")
                .place("서울시 강서구")
                .price(0)
                .status(BoardStatus.판매중)
                .category(Category.C0)
                .content("게시글 내용")
                .thumbnail("썸네일 1")
                .interestCount(1)
                .images(new ArrayList<>())
                .build();

        this.member = Member.builder()
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
                .build();

        this.loginMemberform = LoginMemberForm.builder()
                .id(1L)
                .email("test@user.com")
                .nickname("a")
                .profile(null)
                .status(MemberStatus.인증완료)
                .verifyDate(LocalDateTime.now())
                .build();

        this.interest = Interest.builder()
                .board(board)
                .member(member)
                .build();

        this.session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMemberform);
    }

    @Nested
    @DisplayName("POST:/interest/{boardId}/add")
    class AddInterestTest {
        @Test
        @DisplayName("성공 테스트")
        void addInterestSuccessTest() throws Exception {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());
            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());
            doReturn(interest).when(interestRepository).save(any(Interest.class));

            mvc.perform(MockMvcRequestBuilders.post("/interest/{boardId}/add", 1).session(session))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", "text/plain;charset=UTF-8"))
                    .andExpect(content().string(equalTo("ok")));
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 회원 ID")
        void addInterestFailNotMemberIdTest() throws Exception {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());
            doReturn(Optional.ofNullable(null)).when(memberRepository).findById(anyLong());
            doReturn(interest).when(interestRepository).save(any(Interest.class));

            mvc.perform(MockMvcRequestBuilders.post("/interest/{boardId}/add", 1).session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("message", "존재하지 않는 회원 아이디입니다."))
                    .andExpect(view().name("error/exception-page"));
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void addInterestFailNotBoardIdTest() throws Exception {
            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());
            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());
            doReturn(interest).when(interestRepository).save(any(Interest.class));

            mvc.perform(MockMvcRequestBuilders.post("/interest/{boardId}/add", -115).session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("message", "존재하지 않는 게시글입니다."))
                    .andExpect(view().name("error/exception-page"));
        }
    }

    @Nested
    @DisplayName("POST:/interest/{boardId}/delete")
    class DeleteInterestTest {
        @Test
        @DisplayName("성공 테스트")
        void deleteInterestSuccessTest() throws Exception {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());
            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());
            doReturn(interest).when(interestRepository).save(any(Interest.class));

            mvc.perform(MockMvcRequestBuilders.post("/interest/{boardId}/delete", 1).session(session))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", "text/plain;charset=UTF-8"))
                    .andExpect(content().string(equalTo("ok")));
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 회원 ID")
        void deleteInterestFailNotMemberIdTest() throws Exception {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());
            doReturn(Optional.ofNullable(null)).when(memberRepository).findById(anyLong());
            doReturn(interest).when(interestRepository).save(any(Interest.class));

            mvc.perform(MockMvcRequestBuilders.post("/interest/{boardId}/delete", 1).session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("message", "존재하지 않는 회원 아이디입니다."))
                    .andExpect(view().name("error/exception-page"));
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void deleteInterestFailNotBoardIdTest() throws Exception {
            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());
            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());
            doReturn(interest).when(interestRepository).save(any(Interest.class));

            mvc.perform(MockMvcRequestBuilders.post("/interest/{boardId}/delete", -115).session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("message", "존재하지 않는 게시글입니다."))
                    .andExpect(view().name("error/exception-page"));
        }
    }
}
