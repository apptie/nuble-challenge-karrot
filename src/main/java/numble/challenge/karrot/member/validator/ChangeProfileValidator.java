package numble.challenge.karrot.member.validator;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.form.MemberForm;
import numble.challenge.karrot.member.service.MemberService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ChangeProfileValidator implements Validator {

    private final MemberService memberService;

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberForm form = (MemberForm) target;

        Member member = memberService.getMemberByNickname(form.getNickname());
        if (member != null && !member.getEmail().equals(form.getEmail()) ) {
            errors.rejectValue("nickname", "", "이미 사용중인 닉네임입니다.");
        }
    }
}
