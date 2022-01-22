package numble.challenge.karrot.login.controller;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.board.form.BoardListForm;
import numble.challenge.karrot.board.service.BoardService;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.common.session.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BoardService boardService;

    @GetMapping("/")
    public String homeLogin(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) LoginMemberForm loginMember,
            Model model) {
        if (loginMember == null) {
            return "home";
        }

        List<BoardListForm> boardList = boardService.getBoardList(loginMember.getId());
        model.addAttribute("list", boardList);
        return "board/board-list";
    }
}
