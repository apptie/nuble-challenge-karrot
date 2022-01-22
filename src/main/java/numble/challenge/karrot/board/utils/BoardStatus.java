package numble.challenge.karrot.board.utils;

import lombok.Getter;

@Getter
public enum BoardStatus {
    판매중(1),
    예약중(2),
    거래완료(3);

    private int value;

    private BoardStatus(int value) {
        this.value = value;
    }
}
