package numble.challenge.karrot.board.form;

import lombok.*;
import numble.challenge.karrot.board.utils.BoardStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DetailBoardForm {
    private long id;
    private String profile;
    private String nickname;
    private List<String> imgList;
    private BoardStatus status;
    private String title;
    private int price;
    private String content;
    private String time;
    private String isInterest;
    private long memberId;
}
