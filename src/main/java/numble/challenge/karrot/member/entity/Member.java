package numble.challenge.karrot.member.entity;

import lombok.*;
import numble.challenge.karrot.board.entity.Board;
import numble.challenge.karrot.interest.entity.Interest;
import numble.challenge.karrot.member.utils.MemberStatus;
import numble.challenge.karrot.reply.entity.Reply;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private long id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "NICKNAME", unique = true)
    private String nickname;

    @Column(name = "PROFILE")
    private String profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private MemberStatus status;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "PLACE")
    private String place;

    @Column(name = "VERIFY_DATE")
    private LocalDateTime verifyDate;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    private List<Board> boardList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.REMOVE})
    private List<Reply> replyList;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.REMOVE})
    private List<Interest> interestList;
}
