package numble.challenge.karrot.interest.service;

import numble.challenge.karrot.board.entity.Board;
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

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@DisplayName("InterestService 테스트")
public class InterestServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private InterestRepository interestRepository;

    private InterestService interestService;
    private Board board;
    private Member member;
    private LoginMemberForm form;

    @BeforeEach
    void beforeEach() {
        this.interestService = new InterestServiceImpl(memberRepository, interestRepository, boardRepository);

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

        this.form = LoginMemberForm.builder()
                .id(1L)
                .email("test@user.com")
                .nickname("a")
                .profile(null)
                .status(MemberStatus.인증완료)
                .build();
    }

    @Nested
    @DisplayName("관심물품 추가 테스트")
    class AddInterestTest {
        @Test
        @DisplayName("성공 테스트")
        void addInterestSuccessTest() {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());
            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());

            doReturn(
                    Interest.builder()
                            .id(1L)
                            .board(board)
                            .member(member)
                            .build()
            ).when(interestRepository).save(any(Interest.class));

            Interest interest = interestService.addInterest(form, board.getId());
            assertThat(interest.getId()).isSameAs(1L);
            assertThat(interest.getBoard()).isEqualTo(board);
            assertThat(interest.getMember()).isEqualTo(member);
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void addInterestFailNotBoardIdTest() {
            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());

            assertThrows(BoardNotExistException.class, () -> interestService.addInterest(form, -115L));
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 회원 ID")
        void addInterestFailNotMemberIdTest() {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());
            doReturn(Optional.ofNullable(null)).when(memberRepository).findById(anyLong());

            assertThrows(MemberNotExistException.class, () -> interestService.addInterest(form, -115L));
        }
    }

    @Nested
    @DisplayName("관심물품 삭제 테스트")
    class DeleteInterestTest {
        @Test
        @DisplayName("성공 테스트")
        void deleteInterestSuccessTest() {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());
            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());

            doReturn(board).when(boardRepository).save(any(Board.class));

            doNothing().when(interestRepository).deleteByMemberIdAndBoardId(anyLong(), anyLong());

            boolean flag = interestService.deleteInterest(form, 1L);
            assertThat(flag).isSameAs(true);
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void deleteInterestFailNotBoardIdTest() {
            doReturn(Optional.ofNullable(null)).when(boardRepository).findById(anyLong());

            assertThrows(BoardNotExistException.class, () -> interestService.deleteInterest(form, -115L));
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 회원 ID")
        void deleteInterestFailNotMemberIdTest() {
            doReturn(Optional.of(board)).when(boardRepository).findById(anyLong());
            doReturn(Optional.ofNullable(null)).when(memberRepository).findById(anyLong());

            assertThrows(MemberNotExistException.class, () -> interestService.deleteInterest(form, -115L));
        }
    }
}
