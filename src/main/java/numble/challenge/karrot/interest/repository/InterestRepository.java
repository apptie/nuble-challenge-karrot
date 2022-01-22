package numble.challenge.karrot.interest.repository;

import numble.challenge.karrot.interest.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    List<Interest> findAllByMemberId(long memberId);

    void deleteByMemberIdAndBoardId(long memberId, long boardId);
}
