package numble.challenge.karrot.interest.entity;

import lombok.*;
import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.member.entity.Member;

import javax.persistence.*;

@Entity
@Table(name = "INTEREST")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INTEREST_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
