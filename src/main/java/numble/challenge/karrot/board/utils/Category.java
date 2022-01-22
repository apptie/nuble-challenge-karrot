package numble.challenge.karrot.board.utils;

import lombok.Getter;

@Getter
public enum Category {
    C0("디지털기기"),
    C1("생활가전"),
    C2("가구/인테리어"),
    C3("유아동"),
    C4("생활/가공식품"),
    C5("유아도서"),
    C6("스포츠/레저"),
    C7("여성잡화"),
    C8("여성의류"),
    C9("남성패션/잡화"),
    C10("게임/취미"),
    C11("뷰티/미용"),
    C12("반려동물용품"),
    C13("도서/티켓/음반"),
    C14("식물"),
    C15("기타 중고물품"),
    C16("중고차");

    private final String value;

    Category(String value) {
        this.value = value;
    }
}
