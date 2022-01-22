package numble.challenge.karrot.board.entity;

import lombok.*;
import numble.challenge.karrot.board.utils.BoardStatus;
import numble.challenge.karrot.board.utils.Category;
import numble.challenge.karrot.common.entity.BaseEntity;
import numble.challenge.karrot.interest.entity.Interest;
import numble.challenge.karrot.member.entity.Member;
import numble.challenge.karrot.reply.entity.Reply;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "BOARD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "PLACE")
    private String place;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private BoardStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY")
    private Category category;

    @Column(name = "REPLY_COUNT")
    private int replyCount;

    @Column(name = "INTEREST_COUNT")
    private int interestCount;

    @OneToMany(mappedBy="board", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Image> images;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "MEMBER_ID", insertable = false, updatable = false)
    private long memberId;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.REMOVE})
    private List<Reply> replyList;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<Interest> interestList;

    public void addImage(Image image) {
        images.add(image);
        image.setBoard(this);
    }
}
