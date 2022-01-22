package numble.challenge.karrot.login.service;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final MemberRepository memberRepository;

    @Override
    public LoginMemberForm login(String email, String password) {

        return memberRepository.findByEmail(email)
                .filter(m -> m.getPassword().equals(password))
                .map(m -> LoginMemberForm.builder()
                        .id(m.getId())
                        .email(m.getEmail())
                        .nickname(m.getNickname())
                        .profile(m.getProfile())
                        .status(m.getStatus())
                        .verifyDate(m.getVerifyDate())
                        .place(m.getPlace())
                        .build())
                .orElse(null);
    }
}
