package numble.challenge.karrot.member.form;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinMemberForm {
    @NotBlank(message = "이메일(ID)을 입력해주세요.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    @Size(min = 1, max = 50, message = "50글자 이하의 이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호(PW)를 입력해주세요.")
    @Size(min = 1, max = 50, message = "50글자 이하의 비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 1, max = 50, message = "50글자 이하의 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "핸드폰번호를 입력해주세요.")
    @Pattern(regexp = "[0-9]{10,11}", message = "10 ~ 11자리의 숫자만 입력 가능합니다.")
    private String phoneNumber;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 1, max = 50, message = "50글자 이하의 닉네임을 입력해주세요.")
    private String nickname;

    @NotBlank(message = "장소를 입력해주세요.")
    private String place;

    private String ip;
}

