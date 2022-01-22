package numble.challenge.karrot.reply.form;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplyListForm {
    private String writer;
    private String time;
    private String content;
    private String profile;
    private long id;
}
