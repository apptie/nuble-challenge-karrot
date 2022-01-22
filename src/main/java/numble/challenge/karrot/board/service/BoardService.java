package numble.challenge.karrot.board.service;

import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.form.*;
import numble.challenge.karrot.board.utils.BoardStatus;
import numble.challenge.karrot.login.form.LoginMemberForm;

import java.util.List;

public interface BoardService {
    Board addBoard(AddBoardForm boardForm, LoginMemberForm loginMember);

    List<BoardListForm> getBoardList(long memberId);

    DetailBoardForm getDetailBoard(long boardId, LoginMemberForm loginMember);

    List<BoardListOnDetailForm> getDetailBoardList(long memberId, long boardId);

    List<OtherBoardListForm> getOtherBoardList(long boardId);

    List<OtherBoardListForm> getOtherStatusBoardList(long memberId, BoardStatus status);

    Board changeBoardStatus(long boardId, BoardStatus status);

    boolean deleteBoard(long boardId);

    UpdateBoardForm getUpdateBoardForm(long boardId);

    void updateBoard(UpdateBoardForm form, long boardId);

    List<BoardListForm> getInterestBoardList(long memberId);

    BoardReplyForm getBoardReply(long boardId);

    SimpleBoardForm getSimpleBoard(long boardId);

    long getMemberId(long boardId);
}
