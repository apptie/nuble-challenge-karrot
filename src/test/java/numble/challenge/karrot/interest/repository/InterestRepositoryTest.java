package numble.challenge.karrot.interest.repository;

import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.repository.BoardRepository;
import numble.challenge.karrot.board.utils.BoardStatus;
import numble.challenge.karrot.board.utils.Category;
import numble.challenge.karrot.interest.entity.Interest;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.repository.MemberRepository;
import numble.challenge.karrot.member.utils.MemberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@DisplayName("InterestRepository 테스트")
public class InterestRepositoryTest {

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    private Member member;
    private Board board;

    @BeforeEach
    void beforeEach() {
        Member member = Member.builder()
                .email("testuser@user.com")
                .password("1234")
                .name("a")
                .phoneNumber("01012345678")
                .nickname("aaaaa")
                .profile(null)
                .status(MemberStatus.인증완료)
                .uuid("uuid")
                .place("서울시 강서구")
                .build();

        this.member = memberRepository.save(member);

        Board board = Board.builder()
                .title("게시글 1")
                .place("서울시 강서구")
                .price(0)
                .status(BoardStatus.판매중)
                .category(Category.C0)
                .member(member)
                .content("게시글 내용")
                .thumbnail("썸네일 1")
                .images(new ArrayList<>())
                .build();

        this.board = boardRepository.save(board);

        Interest interest = Interest.builder()
                .board(board)
                .member(member)
                .build();

        interestRepository.save(interest);
    }

    @Test
    @DisplayName("관심물품 추가 테스트")
    void addInterestSuccessTest() {
        Board saveBoard = boardRepository.save(board);

        Interest interest = Interest.builder()
                .board(saveBoard)
                .member(member)
                .build();

        Interest saveInterest = interestRepository.save(interest);
        assertThat(saveInterest.getMember()).isEqualTo(this.member);
        assertThat(saveInterest.getBoard()).isEqualTo(saveBoard);
    }

    @Test
    @DisplayName("관심목록 삭제 테스트")
    void deleteInterestTest() {
        List<Interest> interestList = interestRepository.findAllByMemberId(member.getId());
        assertThat(interestList.size()).isSameAs(1);

        interestRepository.deleteByMemberIdAndBoardId(member.getId(), board.getId());
        interestList = interestRepository.findAllByMemberId(member.getId());
        assertThat(interestList.size()).isSameAs(0);
    }

    @Nested
    @DisplayName("관심물품 조회 테스트")
    class InterestFindALlTest {
        @Test
        @DisplayName("성공 테스트")
        void findAllByMemberIdSuccessTest() {
            List<Interest> interestList = interestRepository.findAllByMemberId(member.getId());

            assertThat(interestList.size()).isSameAs(1);
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 회원 ID")
        void findAllByMemberIdFailTest() {
            List<Interest> interestList = interestRepository.findAllByMemberId(-115L);

            assertThat(interestList.size()).isSameAs(0);
        }
    }

}
