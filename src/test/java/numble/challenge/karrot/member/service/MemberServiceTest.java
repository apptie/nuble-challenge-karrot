package numble.challenge.karrot.member.service;

import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.utils.BoardStatus;
import numble.challenge.karrot.common.exception.MemberNotExistException;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.form.JoinMemberForm;
import numble.challenge.karrot.member.form.MemberForm;
import numble.challenge.karrot.member.form.MyBoardListForm;
import numble.challenge.karrot.member.repository.MemberRepository;
import numble.challenge.karrot.member.utils.MemberStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MemberService 테스트")
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    private MemberServiceImpl memberService;
    private Member member;

    @BeforeEach
    void beforeEach() {
        this.memberService = new MemberServiceImpl(memberRepository);

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
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void joinTest() {
        doReturn(member).when(memberRepository).save(any());

        JoinMemberForm form = JoinMemberForm.builder()
                .email("test@user.com")
                .password("1234")
                .name("a")
                .phoneNumber("01012345678")
                .nickname("a")
                .place("서울시 강서구")
                .build();

        Member saveMember = memberService.join(form);

        assertThat(saveMember.getId()).isSameAs(1L);
        assertThat(saveMember.getEmail()).isEqualTo(form.getEmail());
        assertThat(saveMember.getNickname()).isEqualTo(form.getNickname());

    }

    @Nested
    @DisplayName("회원 프로필 변경 테스트")
    class ProfileChange {
        @Test
        @DisplayName("성공 테스트")
        void changeProfileSuccessTest() {
            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());

            doReturn(member).when(memberRepository).save(any());

            MemberForm form = MemberForm.builder()
                    .nickname("abc")
                    .profile("profile.png")
                    .build();

            Member member = memberService.changeProfile(1L, form);

            assertThat(member.getId()).isSameAs(1L);
            assertThat(member.getNickname()).isEqualTo(form.getNickname());
            assertThat(member.getProfile()).isEqualTo(form.getProfile());
        }

        @Test
        @DisplayName("실패 테스트 - 없는 회원 아이디")
        void changeProfileFailTest() {
            MemberForm form = MemberForm.builder()
                    .nickname("abc")
                    .profile("profile.png")
                    .build();

            Assertions.assertThrows(MemberNotExistException.class, () -> memberService.changeProfile(9999L, form));
        }
    }

    @Nested
    @DisplayName("회원이 작성한 게시글 리스트 조회 테스트")
    class BoardListTest {
        @Test
        @DisplayName("성공 테스트")
        void getBoardListSuccessTest() {
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

            List<MyBoardListForm> boardList = memberService.getBoardList(1L);

            int i = 0;

            for (MyBoardListForm form : boardList) {
                i++;
                assertThat(form.getId()).isSameAs(Long.valueOf(i));
                assertThat(form.getTitle()).isEqualTo("게시글 " + i);
                assertThat(form.getPlace()).isEqualTo("서울시 강서구");
                assertThat(form.getImage()).isEqualTo("썸네일 " + i);
            }
        }

        @Test
        @DisplayName("실패 테스트 - 없는 회원 아이디")
        void getBoardListFailTest() {
            Assertions.assertThrows(MemberNotExistException.class, () -> memberService.getBoardList(9999L));
        }
    }

    @Nested
    @DisplayName("이메일로 회원 조회 테스트")
    class MemberByEmailTest {
        @Test
        @DisplayName("성공 테스트")
        void getMemberByEmailSuccessTest() {
            doReturn(Optional.of(member)).when(memberRepository).findByEmail(anyString());

            Member memberByEmail = memberService.getMemberByEmail("test@user.com");

            assertThat(memberByEmail.getId()).isSameAs(1L);
            assertThat(memberByEmail.getEmail()).isEqualTo("test@user.com");
            assertThat(memberByEmail.getNickname()).isEqualTo("a");
        }

        @Test
        @DisplayName("실패 테스트 - 없는 회원 이메일")
        void getMemberByEmailFailTest() {
            doThrow(MemberNotExistException.class).when(memberRepository).findByEmail(anyString());

            Assertions.assertThrows(MemberNotExistException.class, () -> memberService.getMemberByEmail("no@user.com"));
        }
    }


    @Nested
    @DisplayName("닉네임으로 회원 조회 테스트")
    class MemberByNickName {
        @Test
        @DisplayName("성공 테스트")
        void getMemberByNicknameTest() {
            doReturn(Optional.of(member)).when(memberRepository).findByNickname(anyString());

            Member memberByNickname = memberService.getMemberByNickname("test@user.com");

            assertThat(memberByNickname.getId()).isSameAs(1L);
            assertThat(memberByNickname.getEmail()).isEqualTo("test@user.com");
            assertThat(memberByNickname.getNickname()).isEqualTo("a");
        }

        @Test
        @DisplayName("실패 테스트 - 없는 회원 닉네임")
        void getMemberByEmailFailTest() {
            doThrow(MemberNotExistException.class).when(memberRepository).findByNickname(anyString());

            Assertions.assertThrows(MemberNotExistException.class, () -> memberService.getMemberByNickname("no-nickname"));
        }
    }

    @Nested
    @DisplayName("메일 인증을 위한 UUID 조회 테스트")
    class getUUID {
        @Test
        @DisplayName("성공 테스트")
        void getUUIDSuccessTest() {
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
                                    .status(MemberStatus.인증대기)
                                    .uuid(uuid)
                                    .place("서울시 강서구")
                                    .build()
                    )
            ).when(memberRepository).findByEmail(anyString());

            String memberUuid = memberService.getUUID("test@user.com");

            assertThat(memberUuid).isEqualTo(uuid);
        }

        @Test
        @DisplayName("실패 테스트 - 없는 회원 이메일")
        void getUUIDFailTest() {
            doThrow(MemberNotExistException.class).when(memberRepository).findByEmail(anyString());

            Assertions.assertThrows(MemberNotExistException.class, () -> memberService.getUUID("no@user.com"));
        }
    }

    @Nested
    @DisplayName("회원 메일 인증 UUID 테스트")
    class CheckUUID {
        @Test
        @DisplayName("인증 성공 테스트")
        void checkEmailUUIDSuccessTest() {
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
                                    .status(MemberStatus.인증대기)
                                    .uuid(uuid)
                                    .place("서울시 강서구")
                                    .build()
                    )
            ).when(memberRepository).findByEmail(anyString());

            boolean check = memberService.checkEmailUUID("test@user.com", uuid);
            assertThat(check).isTrue();
        }

        @Test
        @DisplayName("실패 테스트 - 일치하지 않는 uuid")
        void checkEmailUUIDNotCorrectTest() {
            String uuid = UUID.randomUUID().toString();

            doReturn(Optional.of(member)).when(memberRepository).findByEmail(anyString());

            boolean check = memberService.checkEmailUUID("test@user.com", uuid);
            assertThat(check).isFalse();
        }

        @Test
        @DisplayName("실패 테스트 - 없는 회원 이메일")
        void checkEmailUUIDNotExistMemberTest() {
            doThrow(MemberNotExistException.class).when(memberRepository).findByEmail(anyString());

            Assertions.assertThrows(MemberNotExistException.class, () -> memberService.getUUID("no@user.com"));
        }
    }

}

