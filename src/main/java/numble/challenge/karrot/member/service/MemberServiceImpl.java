package numble.challenge.karrot.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.utils.BoardStatus;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.form.JoinMemberForm;
import numble.challenge.karrot.member.form.MemberForm;
import numble.challenge.karrot.member.form.MyBoardListForm;
import numble.challenge.karrot.member.repository.MemberRepository;
import numble.challenge.karrot.common.exception.MemberNotExistException;
import numble.challenge.karrot.member.utils.MemberStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Member join(JoinMemberForm memberForm) {
        Member member = Member.builder()
                .email(memberForm.getEmail())
                .password(memberForm.getPassword())
                .name(memberForm.getName())
                .phoneNumber(memberForm.getPhoneNumber())
                .nickname(memberForm.getNickname())
                .status(MemberStatus.인증대기)
                .place(memberForm.getPlace())
                .uuid(UUID.randomUUID().toString())
                .verifyDate(LocalDateTime.now())
                .build();

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member changeProfile(long memberId, MemberForm form) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistException::new);

        member.setProfile(form.getProfile());
        member.setNickname(form.getNickname());
        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public List<MyBoardListForm> getBoardList(long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistException::new);
        List<Board> boardList = member.getBoardList();

        List<MyBoardListForm> list = new ArrayList<>();
        for (Board board : boardList) {
            if (board.getStatus() != BoardStatus.판매중) {
                continue ;
            }

            MyBoardListForm form = MyBoardListForm.builder()
                    .title(board.getTitle())
                    .place(board.getPlace())
                    .price(board.getPrice())
                    .replyCount(board.getReplyCount())
                    .interestCount(board.getInterestCount())
                    .status(board.getStatus())
                    .id(board.getId())
                    .image(board.getThumbnail())
                    .build();

            list.add(form);
        }
        return list;
    }

    @Override
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Member getMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname).orElse(null);
    }

    @Override
    public String getUUID(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotExistException::new);

        return member.getUuid();
    }

    @Override
    @Transactional
    public boolean checkEmailUUID(String email, String uuid) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotExistException::new);

        if (member.getUuid().equals(uuid)) {
            member.setStatus(MemberStatus.인증완료);
            memberRepository.save(member);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteNotVerifyMember(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotExistException::new);

        memberRepository.deleteById(member.getId());
        return true;
    }

    @Override
    public Member checkPlace(long memberId, boolean check, LocalDateTime now) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistException::new);

        if (!check) {
            member.setStatus(MemberStatus.인증오류);
        }
        member.setVerifyDate(now);
        return memberRepository.save(member);
    }

}
