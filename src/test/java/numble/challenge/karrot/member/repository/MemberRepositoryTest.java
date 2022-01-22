package numble.challenge.karrot.member.repository;

import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.utils.MemberStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("MemberRepository 테스트")
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

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

        memberRepository.save(member);
    }

    @Nested
    @DisplayName("회원 저장 테스트")
    class MemberSaveTest {
        @Test
        @DisplayName("성공 테스트")
        void memberSaveSuccessTest() {
            Member member = Member.builder()
                    .email("test2@user.com")
                    .password("1234")
                    .name("a")
                    .phoneNumber("01012345678")
                    .nickname("aa")
                    .profile(null)
                    .status(MemberStatus.인증대기)
                    .uuid("uuid")
                    .place("서울시 강서구")
                    .build();

            Member saveMember = memberRepository.save(member);

            assertThat(saveMember.getEmail()).isEqualTo(member.getEmail());
            assertThat(saveMember.getNickname()).isEqualTo(member.getNickname());
        }

        @Test
        @DisplayName("실패 테스트 - 이메일 중복")
        void memberSaveFailEmail() {
            Member member = Member.builder()
                    .email("testuser@user.com")
                    .password("4321")
                    .name("a")
                    .phoneNumber("01012345678")
                    .nickname("b")
                    .profile(null)
                    .status(MemberStatus.인증대기)
                    .uuid("uuid")
                    .place("서울시 강서구")
                    .build();

            Assertions.assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(member));
        }

        @Test
        @DisplayName("실패 테스트 - 닉네임 중복")
        void memberSaveFailNickname() {

            Member member = Member.builder()
                    .email("test2@user.com")
                    .password("4321")
                    .name("a")
                    .phoneNumber("01012345678")
                    .nickname("aaaaa")
                    .profile(null)
                    .status(MemberStatus.인증대기)
                    .uuid("uuid")
                    .place("서울시 강서구")
                    .build();

            Assertions.assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(member));
        }
    }

    @Nested
    @DisplayName("회원 조회 테스트")
    class MemberFindTest {
        @Test
        @DisplayName("회원 이메일 조회 성공 테스트")
        void memberFindByEmailSuccessTest() {
            Member member = memberRepository.findByEmail("testuser@user.com").get();

            assertThat(member.getEmail()).isEqualTo("testuser@user.com");
            assertThat(member.getNickname()).isEqualTo("aaaaa");
        }

        @Test
        @DisplayName("회원 이메일 조회 실패 테스트 - 존재하지 않는 이메일")
        void memberFindByEmailFailTesT() {
            Assertions.assertThrows(RuntimeException.class, () -> memberRepository.findByEmail("noUserEmail").orElseThrow(RuntimeException::new));
        }

        @Test
        @DisplayName("회원 ID 조회 성공 테스트")
        void memberFindByIdSuccessTest() {
            Member member = Member.builder()
                    .email("test23@user.com")
                    .password("4321")
                    .name("a")
                    .phoneNumber("01012345678")
                    .nickname("aaaaaaaaaa")
                    .profile(null)
                    .status(MemberStatus.인증완료)
                    .uuid("uuid")
                    .place("서울시 강서구")
                    .build();

            Member saveMember = memberRepository.save(member);
            Member findMember = memberRepository.findById(saveMember.getId()).get();

            assertThat(findMember.getId()).isEqualTo(saveMember.getId());
            assertThat(findMember.getEmail()).isEqualTo(saveMember.getEmail());
            assertThat(findMember.getNickname()).isEqualTo(saveMember.getNickname());
        }

        @Test
        @DisplayName("회원 ID 조회 실패 테스트 - 존재하지 않는 회원 ID")
        void memberFindByIdFailTest() {
            Assertions.assertThrows(RuntimeException.class, () -> memberRepository.findById(-115L).orElseThrow(RuntimeException::new));
        }

        @Test
        @DisplayName("회원 닉네임 조회 성공 테스트")
        void memberFindByNicknameSuccessTest() {
            Member member = memberRepository.findByNickname("aaaaa").get();

            assertThat(member.getEmail()).isEqualTo("testuser@user.com");
            assertThat(member.getNickname()).isEqualTo("aaaaa");
        }

        @Test
        @DisplayName("회원 닉네임 조회 실패 테스트 - 존재하지 않는 닉네임")
        void memberFindByNicknameFailTest() {
            Assertions.assertThrows(RuntimeException.class, () -> memberRepository.findByNickname("noUserNickname").orElseThrow(RuntimeException::new));
        }

    }
}
