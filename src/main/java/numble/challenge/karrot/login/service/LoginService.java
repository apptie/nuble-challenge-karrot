package numble.challenge.karrot.login.service;

import numble.challenge.karrot.login.form.LoginMemberForm;

public interface LoginService {
    LoginMemberForm login(String email, String password);
}
