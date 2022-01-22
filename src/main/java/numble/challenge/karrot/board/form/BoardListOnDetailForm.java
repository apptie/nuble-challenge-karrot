package numble.challenge.karrot.board.form;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BoardListOnDetailForm {
    private String image;
    private String title;
    private int price;
    private long id;
}
