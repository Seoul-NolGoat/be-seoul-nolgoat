package wad.seoul_nolgoat.domain.party;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.domain.user.User;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PartyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    private User participant;

    public PartyUser(Party party, User participant) {
        this.party = party;
        this.participant = participant;
    }
}
