package numble.challenge.karrot.reply.repository;

import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.repository.BoardRepository;
import numble.challenge.karrot.board.utils.BoardStatus;
import numble.challenge.karrot.board.utils.Category;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.repository.MemberRepository;
import numble.challenge.karrot.member.utils.MemberStatus;
import numble.challenge.karrot.reply.entity.Reply;
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
@DisplayName("ReplyRepository 테스트")
public class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    private Member member;
    private Board board;
    private Reply reply;
    private Reply saveReply;
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

        this.reply = Reply.builder()
                .content("댓글 내용")
                .board(board)
                .build();

        this.saveReply = replyRepository.save(reply);
    }

    @Test
    @DisplayName("댓글 저장 테스트")
    void addReplyTest() {
        Reply saveReply = replyRepository.save(reply);

        assertThat(saveReply.getContent()).isEqualTo(reply.getContent());
    }

    @Nested
    @DisplayName("댓글 ID 조회 테스트")
    class findByIdTest {
        @Test
        @DisplayName("성공 테스트")
        void findByIdSuccessTest() {

            Reply saveReply = replyRepository.save(reply);

            Reply findByIdReply = replyRepository.findById(saveReply.getId()).get();

            assertThat(saveReply.getId()).isSameAs(findByIdReply.getId());
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 댓글 ID")
        void findByIdFailTest() {
            Assertions.assertThrows(RuntimeException.class, () -> replyRepository.findById(-115L).orElseThrow(RuntimeException::new));
        }
    }

    @Nested
    @DisplayName("댓글 게시글 ID 조회 테스트")
    class findByBoardIdTest {
        @Test
        @DisplayName("성공 테스트")
        void findByBoardIdSuccessTest() {
            Board board = boardRepository.findAllByMemberId(member.getId()).get(0);

            List<Reply> replyList = replyRepository.findAllByBoardId(board.getId());
            assertThat(replyList.size()).isSameAs(1);
            Reply reply = replyList.get(0);
            assertThat(reply.getContent()).isEqualTo("댓글 내용");
        }

        @Test
        @DisplayName("실패 테스트 - 존재하지 않는 게시글 ID")
        void findByBoardIdFailTest() {
            List<Reply> replyList = replyRepository.findAllByBoardId(-115L);

            assertThat(replyList.size()).isSameAs(0);
        }
    }
}
