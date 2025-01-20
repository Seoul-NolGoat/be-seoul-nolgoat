package wad.seoul_nolgoat.domain.comment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.domain.party.Party;
import wad.seoul_nolgoat.domain.user.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Party party;

    private String content;

    public Comment(User writer, Party party, String content) {
        this.writer = writer;
        this.party = party;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}