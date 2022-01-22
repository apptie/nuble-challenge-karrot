package numble.challenge.karrot.reply.service;

import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.repository.BoardRepository;
import numble.challenge.karrot.board.utils.BoardStatus;
import numble.challenge.karrot.board.utils.Category;
import numble.challenge.karrot.common.exception.BoardNotExistException;
import numble.challenge.karrot.common.exception.MemberNotExistException;
import numble.challenge.karrot.common.exception.ReplyNotExistException;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.repository.MemberRepository;
import numble.challenge.karrot.member.utils.MemberStatus;
import numble.challenge.karrot.reply.entity.Reply;
import numble.challenge.karrot.reply.form.ReplyForm;
import numble.challenge.karrot.reply.form.ReplyListForm;
import numble.challenge.karrot.reply.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReplyService 테스트")
public class ReplyServiceTest {

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    private ReplyService replyService;
    private Board board;
    private Member member;
    private Reply reply;

    @BeforeEach
    void beforeEach() {
        this.replyService = new ReplyServiceImpl(replyRepository, boardRepository, memberRepository);

        this.board = Board.builder()
                .id(1L)
                .title("게시글 1")
                .place("서울시 강서구")
                .price(0)
                .status(BoardStatus.판매중)
                .category(Category.C0)
                .content("게시글 내용")
                .thumbnail("썸네일 1")
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
                .status(MemberStatus.인증대기)
                .uuid(UUID.randomUUID().toString())
                .place("서울시 강서구")
                .build();

        this.reply = Reply.builder()
                .id(1L)
                .content("댓글 내용")
                .build();
    }

    @Nested
    @DisplayName("댓글 작성 테스트")
    class ReplyAddTest {
        @Test
        @DisplayName("성공 테스트")
        void replyAddSuccessTest() {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());

            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());

            ReplyForm replyForm = ReplyForm.builder()
                    .content("댓글 내용")
                    .build();

            doReturn(reply).when(replyRepository).save(any(Reply.class));

            LoginMemberForm loginMemberForm = LoginMemberForm.builder()
                    .id(1L)
                    .build();

            Reply reply = replyService.addReply(replyForm, 1L, loginMemberForm);
            assertThat(reply.getId()).isSameAs(1L);
            assertThat(reply.getContent()).isEqualTo("댓글 내용");
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void replyAddFailWrongBoardIdTest() {
            doThrow(BoardNotExistException.class).when(boardRepository).findById(anyLong());

            ReplyForm replyForm = ReplyForm.builder()
                    .content("댓글 내용")
                    .build();

            LoginMemberForm loginMemberForm = LoginMemberForm.builder()
                    .id(1L)
                    .build();

            assertThrows(BoardNotExistException.class, () -> replyService.addReply(replyForm, 1L, loginMemberForm));
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 회원")
        void replyAddFailWrongMemberTest() {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());

            doThrow(MemberNotExistException.class).when(memberRepository).findById(anyLong());

            ReplyForm replyForm = ReplyForm.builder()
                    .content("댓글 내용")
                    .build();

            LoginMemberForm loginMemberForm = LoginMemberForm.builder()
                    .id(1L)
                    .build();

            assertThrows(MemberNotExistException.class, () -> replyService.addReply(replyForm, 1L, loginMemberForm));
        }

        @Nested
        @DisplayName("댓글 목록 조회 테스트")
        class findReplyListTest {
            @Test
            @DisplayName("성공 테스트")
            void findReplyListSuccessTest() {
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

                doReturn(
                        List.of(reply1, reply2, reply3)
                ).when(replyRepository).findAllByBoardId(anyLong());

                List<ReplyListForm> replyList = replyService.findReplyList(1L);
                assertThat(replyList.size()).isSameAs(3);
            }

            @Test
            @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
            void findReplyListFailTest() {
                doReturn(List.of()).when(replyRepository).findAllByBoardId(anyLong());

                List<ReplyListForm> replyList = replyService.findReplyList(1L);
                assertThat(replyList.size()).isSameAs(0);
            }
        }

        @Nested
        @DisplayName("댓글 수정 테스트")
        class UpdateReplyTest {
            @Test
            @DisplayName("성공 테스트")
            void replyUpdateSuccessTest() {
                doReturn(Optional.of(reply)).when(replyRepository).findById(anyLong());

                ReplyForm replyForm = ReplyForm.builder()
                        .content("댓글 수정 내용")
                        .build();

                doReturn(
                        Reply.builder()
                                .id(1L)
                                .content("댓글 수정 내용")
                                .build()
                ).when(replyRepository).save(any(Reply.class));

                Reply reply = replyService.updateReply(replyForm, 1L);
                assertThat(reply.getId()).isSameAs(1L);
                assertThat(reply.getContent()).isEqualTo("댓글 수정 내용");
            }

            @Test
            @DisplayName("실패 테스트 - 존재하지 않는 댓글 ID")
            void replyUpdateFailTest() {
                doReturn(Optional.ofNullable(null)).when(replyRepository).findById(anyLong());

                ReplyForm replyForm = ReplyForm.builder()
                        .content("댓글 수정 내용")
                        .build();

                assertThrows(ReplyNotExistException.class, () -> replyService.updateReply(replyForm, 1L));
            }
        }

        @Nested
        @DisplayName("댓글 조회 테스트")
        class GetReplyTest {
            @Test
            @DisplayName("성공 테스트")
            void getReplySuccessTest() {
                doReturn(Optional.of(reply)).when(replyRepository).findById(anyLong());

                ReplyForm form = replyService.getReply(1L);
                assertThat(form.getContent()).isEqualTo("댓글 내용");
            }

            @Test
            @DisplayName("실패 테스트 - 존재하지 않는 댓글 ID")
            void replyUpdateFailTest() {
                doReturn(Optional.ofNullable(null)).when(replyRepository).findById(anyLong());

                assertThrows(ReplyNotExistException.class, () -> replyService.getReply(1L));
            }
        }
    }

}
