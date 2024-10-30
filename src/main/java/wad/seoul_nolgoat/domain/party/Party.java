package wad.seoul_nolgoat.domain.party;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.domain.user.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String imageUrl;
    private int maxCapacity;
    private LocalDateTime deadline;
    private boolean isClosed;

    // 지역 추가 예정
    // private Enum? location;

    // 상점을 연관관계 대신 문자(상점 링크)로 추가

    @ManyToOne(fetch = FetchType.LAZY)
    private User host;

    public Party(
            String title,
            String imageUrl,
            int maxCapacity,
            LocalDateTime deadline,
            User host
    ) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.maxCapacity = maxCapacity;
        this.deadline = deadline;
        this.isClosed = false;
        this.host = host;
    }
}
