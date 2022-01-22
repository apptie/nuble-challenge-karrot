package numble.challenge.karrot.reply.entity;

import lombok.*;
import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.common.entity.BaseEntity;
import numble.challenge.karrot.member.entity.Member;

import javax.persistence.*;

@Entity
@Table(name = "REPLY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPLY_ID")
    private long id;

    @Column(name = "CONTENT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
