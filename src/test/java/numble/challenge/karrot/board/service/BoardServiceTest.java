package numble.challenge.karrot.board.service;

import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.form.*;
import numble.challenge.karrot.board.repository.BoardRepository;
import numble.challenge.karrot.board.utils.BoardStatus;
import numble.challenge.karrot.board.utils.Category;
import numble.challenge.karrot.common.exception.BoardNotExistException;
import numble.challenge.karrot.common.exception.MemberNotExistException;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("BoardService 테스트")
public class BoardServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private InterestRepository interestRepository;

    private BoardService boardService;
    private Board board;
    private Member member;
    private LoginMemberForm loginMemberForm;
    private List<Board> boardList;

    @BeforeEach
    void beforeEach() {
        this.boardService = new BoardServiceImpl(memberRepository, boardRepository, interestRepository);

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

        Board board = Board.builder()
                .id(1L)
                .title("게시글 1")
                .place("서울시 강서구")
                .price(0)
                .status(BoardStatus.판매중)
                .category(Category.C0)
                .content("게시글 내용")
                .thumbnail("썸네일 1")
                .interestCount(0)
                .replyCount(0)
                .images(new ArrayList<>())
                .member(this.member)
                .build();

        board.setCreatedDate(LocalDateTime.now());

        this.board = board;

        this.loginMemberForm = LoginMemberForm.builder()
                .id(1L)
                .email("test@user.com")
                .nickname("a")
                .profile(null)
                .status(MemberStatus.인증완료)
                .build();

        this.boardList = List.of(
                Board.builder()
                        .id(1L)
                        .title("게시글 1")
                        .place("서울시 강서구")
                        .price(0)
                        .status(BoardStatus.판매중)
                        .category(Category.C0)
                        .content("게시글 내용")
                        .thumbnail("썸네일 1")
                        .interestCount(0)
                        .replyCount(0)
                        .images(new ArrayList<>())
                        .build(),

                Board.builder()
                        .id(2L)
                        .title("게시글 2")
                        .place("서울시 강서구")
                        .price(0)
                        .status(BoardStatus.판매중)
                        .category(Category.C0)
                        .content("게시글 내용")
                        .thumbnail("썸네일 2")
                        .interestCount(0)
                        .replyCount(0)
                        .images(new ArrayList<>())
                        .build()
        );
    }

    @Nested
    @DisplayName("게시글 추가 테스트")
    class AddBoardTest {
        @Test
        @DisplayName("성공 테스트")
        void AddBoardSuccessTest() {
            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());
            doReturn(board).when(boardRepository).save(any(Board.class));

            AddBoardForm addBoardForm = AddBoardForm.builder()
                    .title("게시글 1")
                    .price(0)
                    .category(Category.C0)
                    .content("게시글 내용")
                    .images(new ArrayList<>())
                    .build();

            Board board = boardService.addBoard(addBoardForm, loginMemberForm);

            assertThat(board.getTitle()).isEqualTo(addBoardForm.getTitle());
            assertThat(board.getContent()).isEqualTo(addBoardForm.getContent());
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 회원 ID")
        void AddBoardFailTest() {
            AddBoardForm addBoardForm = AddBoardForm.builder()
                    .title("게시글 1")
                    .price(0)
                    .category(Category.C0)
                    .content("게시글 내용")
                    .images(new ArrayList<>())
                    .build();

            assertThrows(MemberNotExistException.class, () -> boardService.addBoard(addBoardForm, loginMemberForm));
        }
    }

    @Test
    @DisplayName("로그인한 회원이 작성한 글을 제외한 게시글 조회 테스트")
    void getBoardListSuccessTest() {
        doReturn(boardList).when(boardRepository).findAll(any(Specification.class));

        List<BoardListForm> boardList = boardService.getBoardList(member.getId());
        assertThat(boardList.size()).isSameAs(2);
    }

    @Nested
    @DisplayName("게시글 상세조회 테스트")
    class DetailBoardTest {
        @Test
        @DisplayName("성공 테스트")
        void detailBoardSuccessTest() {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());
            doReturn(new ArrayList()).when(interestRepository).findAllByMemberId(anyLong());
            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());

            DetailBoardForm detailBoard = boardService.getDetailBoard(1L, loginMemberForm);
            assertThat(board.getId()).isSameAs(detailBoard.getId());
            assertThat(board.getTitle()).isEqualTo(detailBoard.getTitle());
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void detailBoardFailNotBoardIdTest() {
            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());

            assertThrows(BoardNotExistException.class, () -> boardService.getDetailBoard(1L, loginMemberForm));
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 회원 ID")
        void detailBoardFailNotMemberIdTest() {
            doReturn(Optional.ofNullable(null)).when(memberRepository).findById(anyLong());
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());

            assertThrows(MemberNotExistException.class, () -> boardService.getDetailBoard(1L, loginMemberForm));
        }
    }

    @Test
    @DisplayName("해당 게시글 작성자의 다른 게시글 간략 조회 테스트")
    void DetailBoardListTest() {
        doReturn(
                List.of(
                        Board.builder()
                                .id(2L)
                                .title("게시글 2")
                                .place("서울시 강서구")
                                .price(0)
                                .status(BoardStatus.판매중)
                                .category(Category.C0)
                                .content("게시글 내용")
                                .thumbnail("썸네일 2")
                                .interestCount(0)
                                .replyCount(0)
                                .images(new ArrayList<>())
                                .build()
                )
        ).when(boardRepository).findAllByMemberId(anyLong());
        doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());

        List<BoardListOnDetailForm> boardList = boardService.getDetailBoardList(loginMemberForm.getId(), 1L);

        assertThat(boardList.size()).isSameAs(1);
    }

    @Test
    @DisplayName("해당 게시글 작성자의 다른 게시글 조회 테스트")
    void getOtherBoardListTest() {
        doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());
        doReturn(boardList).when(boardRepository).findAllByMemberId(anyLong());

        List<OtherBoardListForm> boardList = boardService.getOtherBoardList(loginMemberForm.getId());

        assertThat(boardList.size()).isSameAs(2);
    }

    @Test
    @DisplayName("게시글 상태에 따른 게시글 목록 조회 테스트")
    void getOtherStatusBoardListTest() {
        doReturn(boardList).when(boardRepository).findAll(any(Specification.class));
        doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());

        List<OtherBoardListForm> boardList = boardService.getOtherStatusBoardList(loginMemberForm.getId(), BoardStatus.판매중);

        assertThat(boardList.size()).isSameAs(2);
    }

    @Nested
    @DisplayName("게시글 상태 변경 테스트")
    class ChangeBoardStatusTest {
        @Test
        @DisplayName("성공 테스트")
        void changeBoardStatusSuccessTest() {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());
            doReturn(board).when(boardRepository).save(any(Board.class));

            Board board = boardService.changeBoardStatus(1L, BoardStatus.거래완료);

            assertThat(board.getId()).isSameAs(1L);
            assertThat(board.getStatus()).isSameAs(BoardStatus.거래완료);
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void changeBoardStatusFailTest() {
            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());

            assertThrows(BoardNotExistException.class, () -> boardService.getDetailBoard(1L, loginMemberForm));
        }
    }

    @Nested
    @DisplayName("게시글 삭제 테스트")
    class DeleteBoardTest {
        @Test
        @DisplayName("성공 테스트")
        void deleteBoardSuccessTest() {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());
            doNothing().when(boardRepository).deleteById(anyLong());

            boolean flag = boardService.deleteBoard(1L);

            assertThat(flag).isTrue();
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void deleteBoardFailTest() {
            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());

            assertThrows(BoardNotExistException.class, () -> boardService.deleteBoard(-115L));
        }
    }

    @Nested
    @DisplayName("게시글 수정 데이터 폼 테스트")
    class GetUpdateBoardFormTest {
        @Test
        @DisplayName("성공 테스트")
        void getUpdateBoardFormSuccessTest() {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());

            UpdateBoardForm updateBoardForm = boardService.getUpdateBoardForm(1L);

            assertThat(updateBoardForm.getTitle()).isEqualTo(board.getTitle());
            assertThat(updateBoardForm.getContent()).isEqualTo(board.getContent());
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void getUpdateBoardFormFailTest() {
            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());

            assertThrows(BoardNotExistException.class, () -> boardService.getUpdateBoardForm(-115L));
        }
    }


    @Nested
    @DisplayName("관심 목록 게시글 조회 테스트")
    class GetInterestBoardListTest {
        @Test
        @DisplayName("성공 테스트")
        void getInterestBoardListSuccessTest() {
            doReturn(
                    List.of(
                            Interest.builder()
                                    .id(1L)
                                    .board(board)
                                    .member(member)
                                    .build()
                    )
            ).when(interestRepository).findAllByMemberId(anyLong());

            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());

            List<BoardListForm> interestBoardList = boardService.getInterestBoardList(1L);

            assertThat(interestBoardList.size()).isSameAs(1);
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 회원 ID")
        void getInterestBoardListFailTest() {
            doReturn(Optional.ofNullable(null)).when(memberRepository).findById(anyLong());

            assertThrows(MemberNotExistException.class, () -> boardService.getInterestBoardList(-115L));
        }
    }

    @Nested
    @DisplayName("게시글 댓글 목록 조회 테스트")
    class GetBoardReplyTest {
        @Test
        @DisplayName("성공 테스트")
        void getBoardReplySuccessTest() {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());

            BoardReplyForm boardReply = boardService.getBoardReply(1L);

            assertThat(boardReply.getId()).isSameAs(board.getId());
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void getBoardReplyFailTest() {
            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());

            assertThrows(BoardNotExistException.class, () -> boardService.getBoardReply(-115L));
        }
    }

    @Nested
    @DisplayName("게시글 생략 조회 테스트")
    class GetSimpleBoardTest {
        @Test
        @DisplayName("성공 테스트")
        void getSimpleBoardSuccessTest() {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());

            SimpleBoardForm simpleBoard = boardService.getSimpleBoard(1L);

            assertThat(simpleBoard.getId()).isSameAs(board.getId());
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void getBoardWriterFailTest() {
            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());

            assertThrows(BoardNotExistException.class, () -> boardService.getSimpleBoard(-115L));
        }
    }

}
