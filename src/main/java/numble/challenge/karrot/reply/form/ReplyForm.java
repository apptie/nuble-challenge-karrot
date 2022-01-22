package numble.challenge.karrot.reply.form;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplyForm {
    @NotEmpty(message = "댓글 내용을 입력해주세요.")
    private String content;
}
