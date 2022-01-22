package numble.challenge.karrot.login.controller;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.login.form.LoginForm;
import numble.challenge.karrot.login.service.LoginService;
import numble.challenge.karrot.common.session.SessionConst;
import numble.challenge.karrot.member.utils.MemberStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", LoginForm.builder().build());
        return "login/login-form";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request,
                        Model model) {
        if (bindingResult.hasErrors()) {
            return "login/login-form";
        }

        LoginMemberForm loginMember = loginService.login(loginForm.getEmail(), loginForm.getPassword());

        if (loginMember == null) {
            model.addAttribute("error", "loginError");
            return "login/login-form";
        }
        model.addAttribute("error", null);

        if (loginMember.getStatus() == MemberStatus.인증대기) {
            return "member/mail-not-check";
        }
        else if (loginMember.getStatus() == MemberStatus.인증오류) {
            return "redirect:/members/not-verify-place";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:" + redirectURL;
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}

