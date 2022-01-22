package numble.challenge.karrot.member.validator;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.member.form.JoinMemberForm;
import numble.challenge.karrot.member.service.MemberService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpValidator implements Validator {

    private final MemberService memberService;

    @Override
    public boolean supports(Class<?> clazz) {
        return JoinMemberForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        JoinMemberForm form = (JoinMemberForm) target;

        if (memberService.getMemberByEmail(form.getEmail()) != null) {
            errors.rejectValue("email", "", "이미 사용중인 이메일입니다.");
        }

        if (memberService.getMemberByNickname(form.getNickname()) != null) {
            errors.rejectValue("nickname", "", "이미 사용중인 닉네임입니다.");
        }
    }
}
