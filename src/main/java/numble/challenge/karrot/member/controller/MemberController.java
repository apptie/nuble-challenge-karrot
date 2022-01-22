package numble.challenge.karrot.member.controller;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.common.session.SessionConst;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.member.form.JoinMemberForm;
import numble.challenge.karrot.member.form.MemberForm;
import numble.challenge.karrot.member.form.MyBoardListForm;
import numble.challenge.karrot.member.service.MemberService;
import numble.challenge.karrot.member.utils.ClientIpUtils;
import numble.challenge.karrot.member.validator.ChangeProfileValidator;
import numble.challenge.karrot.member.validator.SignUpValidator;
import numble.challenge.karrot.quartz.service.ScheduleService;
import org.quartz.JobKey;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import java.util.List;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final SignUpValidator signUpValidator;
    private final ClientIpUtils clientIpUtils;
    private final ChangeProfileValidator changeProfileValidator;
    private final ScheduleService scheduleService;

    @GetMapping("/new")
    public String joinForm(Model model) {
        model.addAttribute("joinMemberForm", JoinMemberForm.builder().build());
        return "member/join-form";
    }

    @PostMapping("/new")
    public String join(@Validated @ModelAttribute JoinMemberForm memberForm, BindingResult bindingResult,
                       Model model) throws IOException {
        signUpValidator.validate(memberForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "member/join-form";
        }

        if (!clientIpUtils.checkPlace(memberForm.getIp(), memberForm.getPlace())) {
            bindingResult.addError(new FieldError("memberForm", "place", "현재 위치가 입력한 장소와 6km 이상 떨어져 있습니다."));
            return "member/join-form";
        }

        memberService.join(memberForm);
        model.addAttribute("email", memberForm.getEmail());

        return "member/mail-send";
    }

    @GetMapping
    public String myPage(Model model,
                         @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberForm loginMember) {
        MemberForm form = getMyPageMemberForm(loginMember);
        model.addAttribute("form", form);
        return "member/my-page";
    }

    @GetMapping("/profile")
    public String profileForm(Model model,
                              @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberForm loginMember) {
        MemberForm form = getMyPageMemberForm(loginMember);
        model.addAttribute("memberForm", form);
        return "member/profile-form";
    }

    @PostMapping("/profile")
    public String changeProfile(@Validated @ModelAttribute MemberForm form, BindingResult bindingResult,
                                @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberForm loginMember) {
        changeProfileValidator.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            return "member/profile-form";
        }

        memberService.changeProfile(loginMember.getId(), form);
        loginMember.setNickname(form.getNickname());
        loginMember.setProfile(form.getProfile());
        return "redirect:/members";
    }

    @GetMapping("/boards")
    public String myBoardList(Model model,
                              @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberForm loginMember) {
        List<MyBoardListForm> boardList = memberService.getBoardList(loginMember.getId());
        model.addAttribute("list", boardList);
        model.addAttribute("memberId", loginMember.getId());
        return "member/my-board-list";
    }

    @GetMapping("/check-mail/{email}/{uuid}")
    public String checkEmail(@PathVariable String email, @PathVariable String uuid, Model model) {
        boolean checkEmail = memberService.checkEmailUUID(email, uuid);
        model.addAttribute("flag", checkEmail);

        if (checkEmail) {
            JobKey jobKey = new JobKey("mail delete target : " + email, "DEFAULT");
            if (!scheduleService.isJobRunning(jobKey)) {
                scheduleService.deleteJob(jobKey);
            }
        }
        return "member/mail-check";
    }

    @GetMapping("/not-verify-place")
    public String notVerifyPlace() {
        return "member/not-verify-place";
    }

    private MemberForm getMyPageMemberForm(LoginMemberForm loginMember) {
        return MemberForm.builder()
                .profile(loginMember.getProfile())
                .nickname(loginMember.getNickname())
                .email(loginMember.getEmail())
                .build();
    }
}
