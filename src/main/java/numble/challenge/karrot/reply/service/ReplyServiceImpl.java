package numble.challenge.karrot.reply.service;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.repository.BoardRepository;
import numble.challenge.karrot.common.exception.BoardNotExistException;
import numble.challenge.karrot.common.exception.MemberNotExistException;
import numble.challenge.karrot.common.exception.ReplyNotExistException;
import numble.challenge.karrot.common.utils.ChronoString;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.repository.MemberRepository;
import numble.challenge.karrot.reply.entity.Reply;
import numble.challenge.karrot.reply.form.ReplyForm;
import numble.challenge.karrot.reply.form.ReplyListForm;
import numble.challenge.karrot.reply.repository.ReplyRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Reply addReply(ReplyForm replyForm, long boardId, LoginMemberForm loginMember) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotExistException::new);
        board.setReplyCount(board.getReplyCount() + 1);
        boardRepository.save(board);

        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(MemberNotExistException::new);

        Reply reply = Reply.builder()
                .board(board)
                .member(member)
                .content(replyForm.getContent())
                .build();

        return replyRepository.save(reply);
    }

    @Override
    public List<ReplyListForm> findReplyList(long boardId) {
        List<Reply> replyList = replyRepository.findAllByBoardId(boardId);
        List<ReplyListForm> list = new ArrayList<>();

        for (Reply reply : replyList) {
            Member member = reply.getMember();
            ReplyListForm form = ReplyListForm.builder()
                    .content(reply.getContent())
                    .profile(member.getProfile())
                    .writer(member.getNickname())
                    .id(reply.getId())
                    .time(ChronoString.getChronoString(reply.getCreatedDate()))
                    .build();

            list.add(form);
        }
        return list;
    }

    @Override
    public Reply updateReply(ReplyForm replyForm, long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(ReplyNotExistException::new);
        reply.setContent(replyForm.getContent());
        return replyRepository.save(reply);
    }

    @Override
    public ReplyForm getReply(long replyId) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(ReplyNotExistException::new);
        return ReplyForm.builder()
                .content(reply.getContent())
                .build();
    }
}
