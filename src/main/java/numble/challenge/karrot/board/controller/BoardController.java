package numble.challenge.karrot.board.controller;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.board.form.*;
import numble.challenge.karrot.board.service.BoardService;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.common.session.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/new")
    public String boardForm(Model model) {
        model.addAttribute("addBoardForm", AddBoardForm.builder().build());
        return "board/add-board-form";
    }

    @PostMapping("/new")
    public String addBoard(@Validated @ModelAttribute AddBoardForm boardForm,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes,
                           @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberForm loginMember) {
        if (bindingResult.hasErrors()) {
            return "board/add-board-form";
        }
        long boardId = boardService.addBoard(boardForm, loginMember).getId();
        redirectAttributes.addAttribute("boardId", boardId);
        redirectAttributes.addFlashAttribute("type", "new");
        return "redirect:/boards/{boardId}/my-detail";
    }

    @GetMapping("/{id}")
    public String boardDetail(@PathVariable long id, Model model, HttpServletRequest request,
                              @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberForm loginMember) {
        DetailBoardForm form = boardService.getDetailBoard(id, loginMember);
        model.addAttribute("form", form);

        List<BoardListOnDetailForm> listForm = boardService.getDetailBoardList(form.getMemberId(), id);
        model.addAttribute("list", listForm);

        if (request.getHeader("Referer") != null && request.getHeader("Referer").contains("boards")) {
            long boardId;
            try {
                boardId = Long.parseLong(request.getHeader("Referer").split("/")[4]);

                if (boardId == id) {
                    model.addAttribute("type", "list");
                }
                else {
                    model.addAttribute("type", "other");
                    model.addAttribute("boardId", request.getHeader("Referer").split("/")[4]);
                }

            } catch (NumberFormatException e) {
                model.addAttribute("type", "list");
            }
        } else {
            model.addAttribute("type", "list");
        }
        return "board/detail-board";
    }
    
    @GetMapping("/{id}/other")
    public String writerOtherBoardList(@PathVariable long id, Model model) {
        List<OtherBoardListForm> list = boardService.getOtherBoardList(id);

        model.addAttribute("memberId", boardService.getMemberId(id));
        model.addAttribute("boardId", id);
        model.addAttribute("list", list);
        return "board/other-board-list";
    }

    @GetMapping("/{id}/my-detail")
    public String myDetailBoardForm(@PathVariable long id, Model model, RedirectAttributes redirectAttributes,
                                  @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberForm loginMember) {
        DetailBoardForm form = boardService.getDetailBoard(id, loginMember);
        model.addAttribute("form", form);

        if (redirectAttributes.getFlashAttributes().get("type") == null) {
            model.addAttribute("type", "my-page");
        } else {
            model.addAttribute("type", "new");
        }

        return "board/update-board-detail";
    }

    @GetMapping("/{id}/my-update")
    public String myUpdateBoardForm(@PathVariable long id, Model model) {
        UpdateBoardForm form = boardService.getUpdateBoardForm(id);
        model.addAttribute("updateBoardForm", form);
        model.addAttribute("category", form.getCategory().getValue());
        return "board/board-update";
    }

    @PostMapping("/{id}/my-update")
    public String myUpdateBoard(@PathVariable long id, @Validated @ModelAttribute UpdateBoardForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "board/board-update";
        }

        boardService.updateBoard(form, id);
        return "redirect:/boards/{id}/my-detail";
    }

    @GetMapping("/interest")
    public String interestBoard(Model model,
                                @SessionAttribute(name = SessionConst.LOGIN_MEMBER) LoginMemberForm loginMember) {
        List<BoardListForm> list = boardService.getInterestBoardList(loginMember.getId());
        model.addAttribute("list", list);
        return "board/interest-board-list";
    }
}
