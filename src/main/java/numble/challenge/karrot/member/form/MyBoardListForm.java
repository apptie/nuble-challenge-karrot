package numble.challenge.karrot.member.form;

import lombok.*;
import numble.challenge.karrot.board.utils.BoardStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MyBoardListForm {
    private String image;
    private String title;
    private String place;
    private String writer;
    private int price;
    private int replyCount;
    private int interestCount;
    private BoardStatus status;
    private long id;
}
