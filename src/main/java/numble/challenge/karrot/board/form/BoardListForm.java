package numble.challenge.karrot.board.form;

import lombok.*;
import numble.challenge.karrot.board.utils.BoardStatus;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BoardListForm {
    private long id;
    private String image;
    private String title;
    private String place;
    private int price;
    private int replyCount;
    private int interestCount;
    private BoardStatus status;
}
