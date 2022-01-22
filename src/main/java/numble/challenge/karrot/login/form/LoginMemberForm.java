package numble.challenge.karrot.login.form;

import lombok.*;
import numble.challenge.karrot.member.utils.MemberStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginMemberForm {
    private long id;
    private String email;
    private String nickname;
    private String profile;
    private MemberStatus status;
    private LocalDateTime verifyDate;
    private String place;
}
