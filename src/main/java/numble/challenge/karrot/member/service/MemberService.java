package numble.challenge.karrot.member.service;

import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.form.JoinMemberForm;
import numble.challenge.karrot.member.form.MemberForm;
import numble.challenge.karrot.member.form.MyBoardListForm;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberService {
    Member join(JoinMemberForm memberForm);

    Member changeProfile(long memberId, MemberForm form);

    List<MyBoardListForm> getBoardList(long memberId);

    Member getMemberByEmail(String email);

    Member getMemberByNickname(String nickname);

    String getUUID(String email);

    boolean checkEmailUUID(String email, String uuid);

    boolean deleteNotVerifyMember(String email);

    Member checkPlace(long memberId, boolean check, LocalDateTime now);
}
