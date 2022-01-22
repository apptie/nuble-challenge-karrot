package numble.challenge.karrot.reply.controller;

import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.repository.BoardRepository;
import numble.challenge.karrot.board.utils.BoardStatus;
import numble.challenge.karrot.board.utils.Category;
import numble.challenge.karrot.common.session.SessionConst;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.repository.MemberRepository;
import numble.challenge.karrot.member.utils.MemberStatus;
import numble.challenge.karrot.reply.entity.Reply;
import numble.challenge.karrot.reply.repository.ReplyRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@DisplayName("ReplyController 테스트")
public class ReplyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReplyRepository replyRepository;

    @MockBean
    private BoardRepository boardRepository;

    @MockBean
    private MemberRepository memberRepository;

    private Board board;
    private Reply reply;
    private LoginMemberForm form;
    private Member member;

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
                .replyCount(0)
                .images(new ArrayList<>())
                .build();

        this.reply = Reply.builder()
                .id(1L)
                .content("댓글 내용 1")
                .build();

        this.form = LoginMemberForm.builder()
                .id(1L)
                .email("test@user.com")
                .nickname("a")
                .profile(null)
                .status(MemberStatus.인증완료)
                .verifyDate(LocalDateTime.now())
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
    }

    @Nested
    @DisplayName("GET:/boards/{id}/replies")
    class GetReplyListTest {
        @Test
        @DisplayName("성공 테스트")
        void getReplyListSuccessTest() throws Exception {
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, form);

            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());

            Reply reply1 = Reply.builder()
                    .id(1L)
                    .content("댓글 내용 1")
                    .member(member)
                    .build();

            reply1.setCreatedDate(LocalDateTime.now());

            Reply reply2 = Reply.builder()
                    .id(2L)
                    .content("댓글 내용 2")
                    .member(member)
                    .build();

            reply2.setCreatedDate(LocalDateTime.now());

            Reply reply3 = Reply.builder()
                    .id(3L)
                    .content("댓글 내용 3")
                    .member(member)
                    .build();

            reply3.setCreatedDate(LocalDateTime.now());

            doReturn(List.of(reply1, reply2, reply3)).when(replyRepository).findAllByBoardId(anyLong());

            mvc.perform(MockMvcRequestBuilders.get("/boards/{id}/replies", 1).session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attributeExists("board", "list", "nickname"))
                    .andExpect(model().attribute("nickname", form.getNickname()))
                    .andExpect(view().name("reply/reply-list"));
        }

        @Test
        @DisplayName("실패 테스트 - 로그인 하지 않음")
        void getReplyListFailNotLoginTest() throws Exception {
            mvc.perform(MockMvcRequestBuilders.get("/boards/{id}/replies", 1))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(header().string("Location", "/login?redirectURL=/boards/1/replies"));
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void getReplyListFailNotBoardIdTest() throws Exception {
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, form);

            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());

            mvc.perform(MockMvcRequestBuilders.get("/boards/{id}/replies", 115).session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("message", "존재하지 않는 게시글입니다."))
                    .andExpect(view().name("error/exception-page"));
        }
    }

    @Nested
    @DisplayName("GET:/boards/{id}/replies/new")
    class AddReplyFormTest {
        @Test
        @DisplayName("성공 테스트")
        void addReplyFormSuccessTest() throws Exception {
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, form);

            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());

            mvc.perform(MockMvcRequestBuilders.get("/boards/{id}/replies/new", 1).session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attributeExists("board", "replyForm"))
                    .andExpect(view().name("reply/reply-form"));
        }

        @Test
        @DisplayName("실패 테스트 - 로그인 하지 않음")
        void addReplyFormFailNotLoginTest() throws Exception {
            mvc.perform(MockMvcRequestBuilders.get("/boards/{id}/replies/new", 1))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(header().string("Location", "/login?redirectURL=/boards/1/replies/new"));
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void addReplyFormFailNotBoardIdTest() throws Exception {
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, form);

            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());

            mvc.perform(MockMvcRequestBuilders.get("/boards/{id}/replies/new", 1).session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("message", "존재하지 않는 게시글입니다."))
                    .andExpect(view().name("error/exception-page"));
        }
    }

    @Nested
    @DisplayName("POST:/boards/{id}/replies/new")
    class AddReplyTest {
        @Test
        @DisplayName("성공 테스트")
        void addReplySuccessTest() throws Exception {
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, form);

            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());

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
            ).when(memberRepository).findById(anyLong());

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("content", "댓글 내용");

            mvc.perform(MockMvcRequestBuilders.post("/boards/{id}/replies/new", 1).session(session).params(params))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(header().string("Location", "/boards/1/replies"))
                    .andExpect(view().name("redirect:/boards/{id}/replies"));
        }

        @Test
        @DisplayName("실패 테스트 - 로그인 하지 않음")
        void addReplyFailNotLoginTest() throws Exception {
            mvc.perform(MockMvcRequestBuilders.post("/boards/{id}/replies/new", 1))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(header().string("Location", "/login?redirectURL=/boards/1/replies/new"));
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void addReplyFailNotBoardIdTest() throws Exception {
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, form);

            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("content", "댓글 내용");

            mvc.perform(MockMvcRequestBuilders.post("/boards/{id}/replies/new", 1).session(session).params(params))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("message", "존재하지 않는 게시글입니다."))
                    .andExpect(view().name("error/exception-page"));
        }
    }

    @Nested
    @DisplayName("GET:/boards/{id}/replies/update")
    class updateReplyFormTest {
        @Test
        @DisplayName("성공 테스트")
        void updateReplyFormSuccessTest() throws Exception {
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, form);

            doReturn(Optional.of(reply)).when(replyRepository).findById(anyLong());

            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());

            mvc.perform(MockMvcRequestBuilders.get("/boards/{id}/replies/update/{replyId}", 1, 1).session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attributeExists("board", "replyForm"))
                    .andExpect(view().name("reply/reply-update-form"));
        }

        @Test
        @DisplayName("실패 테스트 - 로그인 하지 않음")
        void addReplyFailNotLoginTest() throws Exception {
            mvc.perform(MockMvcRequestBuilders.get("/boards/{id}/replies/update/{replyId}", 1, 1))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(header().string("Location", "/login?redirectURL=/boards/1/replies/update/1"));
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void addReplyFailNotBoardIdTest() throws Exception {
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, form);

            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());

            doReturn(Optional.of(reply)).when(replyRepository).findById(anyLong());

            mvc.perform(MockMvcRequestBuilders.get("/boards/{id}/replies/update/{replyId}", 1, 1).session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("message", "존재하지 않는 게시글입니다."))
                    .andExpect(view().name("error/exception-page"));
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 댓글 ID")
        void addReplyFailNotReplyIdTest() throws Exception {
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, form);

            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());

            doReturn(Optional.ofNullable(null)).when(replyRepository).findById(anyLong());

            mvc.perform(MockMvcRequestBuilders.get("/boards/{id}/replies/update/{replyId}", 1, 1).session(session))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(model().attribute("message", "존재하지 않는 댓글입니다."))
                    .andExpect(view().name("error/exception-page"));
        }
    }

    @Nested
    @DisplayName("POST:/boards/{id}/replies/update")
    class updateReplyTest {
        @Test
        @DisplayName("성공 테스트")
        void updateReplySuccessTest() throws Exception {
            MockHttpSession session = new MockHttpSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, form);

            doReturn(Optional.of(reply)).when(replyRepository).findById(anyLong());

            doReturn(
                    Reply.builder()
                            .id(1L)
                            .content("댓글 수정된 내용")
                            .build()
            ).when(replyRepository).save(any(Reply.class));

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("content", "댓글 수정된 내용");

            mvc.perform(MockMvcRequestBuilders.post("/boards/{id}/replies/update/{replyId}", 1, 1).session(session).params(params))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(header().string("Location", "/boards/1/replies"));
        }

        @Test
        @DisplayName("실패 테스트 - 로그인 하지 않음")
        void updateReplyFailNotLoginTest() throws Exception {
            mvc.perform(MockMvcRequestBuilders.post("/boards/{id}/replies/update/{replyId}", 1, 1))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(header().string("Location", "/login?redirectURL=/boards/1/replies/update/1"));
        }
    }
}
