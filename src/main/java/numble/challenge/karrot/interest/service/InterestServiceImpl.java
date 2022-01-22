package numble.challenge.karrot.interest.service;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.repository.BoardRepository;
import numble.challenge.karrot.common.exception.BoardNotExistException;
import numble.challenge.karrot.common.exception.MemberNotExistException;
import numble.challenge.karrot.interest.entity.Interest;
import numble.challenge.karrot.interest.repository.InterestRepository;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final MemberRepository memberRepository;
    private final InterestRepository interestRepository;
    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public Interest addInterest(LoginMemberForm loginMember, long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotExistException::new);
        board.setInterestCount(board.getInterestCount() + 1);
        Interest interest = Interest.builder()
                .board(board)
                .member(memberRepository.findById(loginMember.getId()).orElseThrow(MemberNotExistException::new))
                .build();

        return interestRepository.save(interest);
    }

    @Override
    @Transactional
    public boolean deleteInterest(LoginMemberForm loginMember, long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotExistException::new);
        board.setInterestCount(board.getInterestCount() - 1);
        boardRepository.save(board);
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(MemberNotExistException::new);
        interestRepository.deleteByMemberIdAndBoardId(member.getId(), boardId);
        return true;
    }
}
