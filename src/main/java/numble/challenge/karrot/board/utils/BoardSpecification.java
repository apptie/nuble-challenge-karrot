package numble.challenge.karrot.board.utils;

import numble.challenge.karrot.board.entity.Board;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class BoardSpecification {
    public static Specification<Board> equalToStatus(BoardStatus status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Board> equalToMemberId(long memberId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("memberId"), memberId);
    }
}
