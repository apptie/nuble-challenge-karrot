package numble.challenge.karrot.member.form;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberForm {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 1, max = 50, message = "50글자 이하의 닉네임을 입력해주세요.")
    private String nickname;

    private String profile;

    private String email;
}
