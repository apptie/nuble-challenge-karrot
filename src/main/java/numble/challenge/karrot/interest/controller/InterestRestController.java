package numble.challenge.karrot.interest.controller;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.common.session.SessionConst;
import numble.challenge.karrot.interest.service.InterestService;
import numble.challenge.karrot.login.form.LoginMemberForm;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interest/{boardId}")
@RequiredArgsConstructor
public class InterestRestController {

    private final InterestService interestService;

    @PostMapping ("/add")
    public String addInterest(@PathVariable long boardId,
                              @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberForm loginMember) {
        interestService.addInterest(loginMember, boardId);
        return "ok";
    }

    @PostMapping("/delete")
    public String deleteInterest(@PathVariable long boardId,
                                 @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberForm loginMember) {
        interestService.deleteInterest(loginMember, boardId);
        return "ok";
    }
}
