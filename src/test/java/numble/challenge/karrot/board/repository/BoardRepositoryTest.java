package numble.challenge.karrot.board.repository;

import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.utils.BoardStatus;
import numble.challenge.karrot.board.utils.Category;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.repository.MemberRepository;
import numble.challenge.karrot.member.utils.MemberStatus;
import org.junit.jupiter.api.*;
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
@DisplayName("BoardRepository 테스트")
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

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
                .status(MemberStatus.인증대기)
                .uuid("uuid")
                .place("서울시 강서구")
                .build();

        this.member = memberRepository.save(member);

        this.board = Board.builder()
                .title("게시글 1")
                .place("서울시 강서구")
                .price(0)
                .status(BoardStatus.판매중)
                .category(Category.C0)
                .content("게시글 내용")
                .thumbnail("썸네일 1")
                .interestCount(0)
                .replyCount(0)
                .member(this.member)
                .images(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("게시글 등록 테스트")
    void addBoardTest() {
        Board saveBoard = boardRepository.save(board);

        assertThat(board.getContent()).isEqualTo(saveBoard.getContent());
    }

    @Test
    @DisplayName("게시글 전체 조회 테스트")
    void findAllBoardTest() {
        boardRepository.save(board);

        List<Board> boardList = boardRepository.findAll();

        assertThat(boardList.size()).isNotZero();
    }

    @Test
    @DisplayName("게시글 ID 수정 테스트")
    void updateBoardById() {
        Board saveBoard = boardRepository.save(board);
        Board board = boardRepository.findById(saveBoard.getId()).get();
        board.setTitle("수정된 게시글 제목");
        boardRepository.save(board);

        Board changeBoard = boardRepository.findById(saveBoard.getId()).get();
        assertThat(changeBoard.getTitle()).isEqualTo(board.getTitle());
    }

    @Test
    @DisplayName("게시글 작성자 ID로 조회 테스트")
    void findAllBoardByWriter() {
        Board saveBoard = boardRepository.save(board);

        List<Board> boardList = boardRepository.findAllByMemberId(saveBoard.getMember().getId());
        assertThat(boardList.size()).isNotZero();
    }

    @Nested
    @DisplayName("게시글 ID 조회 테스트")
    class FindBoardByIdTest {
        @Test
        @DisplayName("성공 테스트")
        void findBoardByIdSuccessTest() {
            Board saveBoard = boardRepository.save(board);
            Board findByIdBoard = boardRepository.findById(saveBoard.getId()).get();

            assertThat(saveBoard.getId()).isEqualTo(findByIdBoard.getId());
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void findBoardByIdFailTest() {
            Assertions.assertThrows(RuntimeException.class, () -> {
                boardRepository.findById(-115L).orElseThrow(RuntimeException::new);
            });
        }
    }

    @Nested
    @DisplayName("게시글 ID 조회 테스트")
    class DeleteBoardByIdTest {
        @Test
        @DisplayName("성공 테스트")
        void deleteBoardByIdSuccessTest () {
            Board saveBoard = boardRepository.save(board);
            int beforeSize = boardRepository.findAll().size();
            boardRepository.deleteById(saveBoard.getId());
            int afterSize = boardRepository.findAll().size();

            assertThat(afterSize).isSameAs(beforeSize - 1);
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void deleteBoardByIdFailTest () {
            Assertions.assertThrows(RuntimeException.class, () -> {
                boardRepository.deleteById(-115L);
            });
        }
    }


}
