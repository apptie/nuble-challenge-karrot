package numble.challenge.karrot.board.controller;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.board.form.OtherBoardListForm;
import numble.challenge.karrot.board.service.BoardService;
import numble.challenge.karrot.board.utils.BoardStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService boardService;

    @PostMapping("/sell")
    public List<OtherBoardListForm> sellBoardList(@RequestBody Map<String, Long> memberIdMap) {
        return boardService.getOtherStatusBoardList(memberIdMap.get("memberId"), BoardStatus.판매중);
    }

    @PostMapping("/done")
    public List<OtherBoardListForm> doneBoardList(@RequestBody Map<String, Long> memberIdMap) {
        return boardService.getOtherStatusBoardList(memberIdMap.get("memberId"), BoardStatus.거래완료);
    }

    @PostMapping("/all")
    public List<OtherBoardListForm> allBoardList(@RequestBody Map<String, Long> memberIdMap) {
        return boardService.getOtherBoardList(memberIdMap.get("memberId"));
    }

    @PostMapping("/status")
    public String changeBoardStatus(@RequestBody Map<String, Object> paramMap) {
        BoardStatus status;
        String type = (String) paramMap.get("type");

        if (type.equals("예약중")) {
            status = BoardStatus.예약중;
        }
        else if (type.equals("판매중")) {
            status = BoardStatus.판매중;
        }
        else {
            status = BoardStatus.거래완료;
        }
        boardService.changeBoardStatus(Long.parseLong((String)paramMap.get("boardId")), status);

        return "ok";
    }

    @PostMapping("/{id}/delete")
    public String deleteBoard(@PathVariable long id) {
        boardService.deleteBoard(id);
        return "ok";
    }
}
