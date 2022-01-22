package numble.challenge.karrot.interest.service;

import numble.challenge.karrot.interest.entity.Interest;
import numble.challenge.karrot.login.form.LoginMemberForm;

public interface InterestService {
    Interest addInterest(LoginMemberForm loginMember, long boardId);

    boolean deleteInterest(LoginMemberForm loginMember, long boardId);
}
