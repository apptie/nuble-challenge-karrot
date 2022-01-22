package numble.challenge.karrot.board.form;

import lombok.*;
import numble.challenge.karrot.board.utils.Category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UpdateBoardForm {
    @Size(max = 4, message="0 ~ 4개의 사진을 등록해주세요.")
    private List<String> images = new ArrayList<>();

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @PositiveOrZero(message = "0 이상의 숫자를 입력해주세요.")
    private int price;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;
}
