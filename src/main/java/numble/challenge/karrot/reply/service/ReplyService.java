package numble.challenge.karrot.reply.service;

import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.reply.entity.Reply;
import numble.challenge.karrot.reply.form.ReplyForm;
import numble.challenge.karrot.reply.form.ReplyListForm;

import java.util.List;

public interface ReplyService {
    Reply addReply(ReplyForm replyForm, long boardId, LoginMemberForm loginMember);

    List<ReplyListForm> findReplyList(long boardId);

    Reply updateReply(ReplyForm replyForm, long replyId);

    ReplyForm getReply(long replyId);
}
