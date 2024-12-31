package wad.seoul_nolgoat.domain.party;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.domain.BaseTimeEntity;
import wad.seoul_nolgoat.domain.user.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Party extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String imageUrl;
    private int maxCapacity;
    private LocalDateTime deadline;
    private boolean isClosed;
    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private AdministrativeDistrict administrativeDistrict;

    @ManyToOne(fetch = FetchType.LAZY)
    private User host;

    public Party(
            String title,
            String content,
            String imageUrl,
            int maxCapacity,
            LocalDateTime deadline,
            String district,
            User host
    ) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.maxCapacity = maxCapacity;
        this.deadline = deadline;
        this.isClosed = false;
        this.isDeleted = false;
        this.administrativeDistrict = AdministrativeDistrict.valueOf(district);
        this.host = host;
    }

    public void close() {
        isClosed = true;
    }

    public void delete() {
        isDeleted = true;
    }
}
