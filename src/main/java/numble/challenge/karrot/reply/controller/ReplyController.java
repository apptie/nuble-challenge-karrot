package numble.challenge.karrot.reply.controller;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.board.form.BoardReplyForm;
import numble.challenge.karrot.board.service.BoardService;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.reply.form.ReplyForm;
import numble.challenge.karrot.reply.form.ReplyListForm;
import numble.challenge.karrot.reply.service.ReplyService;
import numble.challenge.karrot.common.session.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/boards/{id}/replies")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;
    private final BoardService boardService;

    @GetMapping
    public String getReplyList(@PathVariable long id, Model model,
                            @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberForm loginMember) {
        BoardReplyForm board = boardService.getBoardReply(id);
        List<ReplyListForm> replyList = replyService.findReplyList(id);
        model.addAttribute("board", board);
        model.addAttribute("list", replyList);
        model.addAttribute("nickname", loginMember.getNickname());
        return "reply/reply-list";
    }

    @GetMapping("/new")
    public String replyForm(@PathVariable long id, Model model) {
        model.addAttribute("board", boardService.getSimpleBoard(id));
        model.addAttribute("replyForm", ReplyForm.builder().build());
        return "reply/reply-form";
    }

    @PostMapping("/new")
    public String addReply(@PathVariable long id,
                           @Validated @ModelAttribute ReplyForm form, BindingResult bindingResult,
                           @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberForm loginMember) {
        if (bindingResult.hasErrors()) {
            return "/reply/reply-update-form";
        }

        replyService.addReply(form, id, loginMember);
        return "redirect:/boards/{id}/replies";
    }

    @GetMapping("/update/{replyId}")
    public String updateReplyForm(@PathVariable long id,
                              @PathVariable long replyId, Model model) {
        ReplyForm replyForm = replyService.getReply(replyId);
        model.addAttribute("board", boardService.getSimpleBoard(id));
        model.addAttribute("replyForm", replyForm);
        return "reply/reply-update-form";
    }

    @PostMapping("/update/{replyId}")
    public String updateReply(@PathVariable long replyId,
                              @Validated @ModelAttribute ReplyForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "reply/reply-update-form";
        }

        replyService.updateReply(form, replyId);
        return "redirect:/boards/{id}/replies";
    }
}
