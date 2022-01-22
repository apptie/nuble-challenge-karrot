package numble.challenge.karrot.board.service;

import lombok.RequiredArgsConstructor;
import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.board.entity.Image;
import numble.challenge.karrot.board.form.*;
import numble.challenge.karrot.board.repository.BoardRepository;
import numble.challenge.karrot.board.utils.BoardSpecification;
import numble.challenge.karrot.board.utils.BoardStatus;
import numble.challenge.karrot.common.exception.BoardNotExistException;
import numble.challenge.karrot.common.exception.MemberNotExistException;
import numble.challenge.karrot.common.utils.ChronoString;
import numble.challenge.karrot.interest.entity.Interest;
import numble.challenge.karrot.interest.repository.InterestRepository;
import numble.challenge.karrot.login.form.LoginMemberForm;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.member.repository.MemberRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final InterestRepository interestRepository;

    @Override
    @Transactional
    public Board addBoard(AddBoardForm boardForm, LoginMemberForm loginMember) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(MemberNotExistException::new);

        String thumbnail = null;

        if (boardForm.getImages().size() > 0) {
            thumbnail = boardForm.getImages().get(0);
        }

        Board board = Board.builder()
                .title(boardForm.getTitle())
                .place(member.getPlace())
                .price(boardForm.getPrice())
                .status(BoardStatus.판매중)
                .category(boardForm.getCategory())
                .member(member)
                .content(boardForm.getContent())
                .thumbnail(thumbnail)
                .images(new ArrayList<>())
                .build();

        List<String> imageNameList = boardForm.getImages();

        for (String name : imageNameList) {
            Image image = Image.builder()
                    .name(name)
                    .build();

            board.addImage(image);
        }

        return boardRepository.save(board);
    }

    @Override
    public List<BoardListForm> getBoardList(long memberId) {
        Specification<Board> spec = Specification.not(BoardSpecification.equalToMemberId(memberId));
        List<Board> boardList = boardRepository.findAll(spec);

        List<BoardListForm> list = new ArrayList<>();

        for (Board board : boardList) {
            BoardListForm form = BoardListForm.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .image(board.getThumbnail())
                    .place(board.getPlace())
                    .price(board.getPrice())
                    .replyCount(board.getReplyCount())
                    .interestCount(board.getInterestCount())
                    .status(board.getStatus())
                    .build();

            list.add(form);
        }

        Collections.sort(list, Comparator.comparingInt(o -> o.getStatus().getValue()));

        return list;
    }

    @Override
    public DetailBoardForm getDetailBoard(long boardId, LoginMemberForm loginMember) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotExistException::new);
        Member member = memberRepository.findById(board.getMemberId()).orElseThrow(MemberNotExistException::new);
        List<Interest> interestList = interestRepository.findAllByMemberId(member.getId());

        return DetailBoardForm.builder()
                .id(boardId)
                .profile(member.getProfile())
                .nickname(member.getNickname())
                .imgList(getImgNameList(board.getImages()))
                .status(board.getStatus())
                .title(board.getTitle())
                .price(board.getPrice())
                .content(board.getContent())
                .time(ChronoString.getChronoString(board.getCreatedDate()))
                .isInterest(isInterest(loginMember.getId(), interestList))
                .memberId(member.getId())
                .build();
    }

    @Override
    public List<BoardListOnDetailForm> getDetailBoardList(long memberId, long boardId) {
        List<BoardListOnDetailForm> list = new ArrayList<>();
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistException::new);
        List<Board> boardList = boardRepository.findAllByMemberId(member.getId());

        for (Board board : boardList) {
            if (board.getId() == boardId) {
                continue ;
            }
            BoardListOnDetailForm form = BoardListOnDetailForm.builder()
                    .image(board.getThumbnail())
                    .title(board.getTitle())
                    .price(board.getPrice())
                    .id(board.getId())
                    .build();

            list.add(form);
        }

        return list;
    }

    @Override
    public List<OtherBoardListForm> getOtherBoardList(long boardId) {
        Board thisBoard = boardRepository.findById(boardId).orElseThrow(BoardNotExistException::new);
        Member member = thisBoard.getMember();
        List<Board> boardList = boardRepository.findAllByMemberId(member.getId());

        List<OtherBoardListForm> list = new ArrayList<>();

        for (Board board : boardList) {
            OtherBoardListForm form = OtherBoardListForm.builder()
                    .image(board.getThumbnail())
                    .title(board.getTitle())
                    .price(board.getPrice())
                    .id(board.getId())
                    .place(board.getPlace())
                    .replyCount(board.getReplyCount())
                    .interestCount(board.getInterestCount())
                    .status(board.getStatus())
                    .writer(member.getNickname())
                    .build();

            list.add(form);
        }
        return list;
    }

    @Override
    public List<OtherBoardListForm> getOtherStatusBoardList(long memberId, BoardStatus status) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistException::new);

        Specification<Board> spec = Specification.where(BoardSpecification.equalToStatus(status));

        if (status == BoardStatus.거래완료) {
            spec = spec.or(BoardSpecification.equalToStatus(BoardStatus.예약중));
        }

        spec = spec.and(BoardSpecification.equalToMemberId(member.getId()));

        List<Board> boardList = boardRepository.findAll(spec);
        List<OtherBoardListForm> list = new ArrayList<>();

        for (Board board : boardList) {
            OtherBoardListForm form = OtherBoardListForm.builder()
                    .image(board.getThumbnail())
                    .title(board.getTitle())
                    .price(board.getPrice())
                    .id(board.getId())
                    .place(board.getPlace())
                    .replyCount(board.getReplyCount())
                    .interestCount(board.getInterestCount())
                    .status(board.getStatus())
                    .writer(member.getNickname())
                    .build();

            list.add(form);
        }

        return list;
    }

    @Override
    @Transactional
    public Board changeBoardStatus(long boardId, BoardStatus status) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotExistException::new);

        board.setStatus(status);
        return boardRepository.save(board);
    }

    @Override
    @Transactional
    public boolean deleteBoard(long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotExistException::new);
        boardRepository.deleteById(board.getId());

        return true;
    }

    @Override
    public UpdateBoardForm getUpdateBoardForm(long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotExistException::new);

        return UpdateBoardForm.builder()
                .category(board.getCategory())
                .content(board.getContent())
                .images(getImgNameList(board.getImages()))
                .price(board.getPrice())
                .title(board.getTitle())
                .build();
    }

    @Override
    @Transactional
    public void updateBoard(UpdateBoardForm form, long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotExistException::new);

        List<String> images = form.getImages();
        board.getImages().clear();
        for (String name : images) {
            Image image = Image.builder()
                    .name(name)
                    .board(board)
                    .build();

            board.addImage(image);
        }

        if (board.getImages().size() == 0) {
            board.setThumbnail(null);
        }
        else {
            board.setThumbnail(board.getImages().get(0).getName());
        }

        board.setTitle(form.getTitle());
        board.setCategory(form.getCategory());
        board.setContent(form.getContent());
        board.setPrice(form.getPrice());


    }

    @Override
    public List<BoardListForm> getInterestBoardList(long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistException::new);
        List<BoardListForm> list = new ArrayList<>();
        List<Interest> interestList = interestRepository.findAllByMemberId(member.getId());

        for (Interest interest : interestList) {
            Board board = interest.getBoard();

            BoardListForm form = BoardListForm.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .image(board.getThumbnail())
                    .place(board.getPlace())
                    .price(board.getPrice())
                    .replyCount(board.getReplyCount())
                    .interestCount(board.getInterestCount())
                    .status(board.getStatus())
                    .build();

            list.add(form);
        }

        return list;
    }
    @Override
    public BoardReplyForm getBoardReply(long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotExistException::new);

        return BoardReplyForm.builder()
                .id(board.getId())
                .title(board.getTitle())
                .image(board.getThumbnail())
                .place(board.getPlace())
                .price(board.getPrice())
                .replyCount(board.getReplyCount())
                .interestCount(board.getInterestCount())
                .status(board.getStatus())
                .build();
    }

    @Override
    public SimpleBoardForm getSimpleBoard(long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotExistException::new);

        return SimpleBoardForm.builder()
                .id(board.getId())
                .build();
    }

    @Override
    public long getMemberId(long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotExistException::new);

        return board.getMember().getId();
    }

    private List<String> getImgNameList(List<Image> images) {
        return images.stream()
                .map(Image::getName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private String isInterest(long memberId, List<Interest> interestList) {
        String checkInterest = "0";

        for (Interest interest : interestList) {
            if (interest.getMember().getId() == memberId) {
                checkInterest = "1";
                break ;
            }
        }
        return checkInterest;
    }

}
