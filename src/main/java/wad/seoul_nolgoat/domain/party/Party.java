package wad.seoul_nolgoat.domain.party;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.domain.BaseTimeEntity;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.exception.ApiException;

import java.time.LocalDateTime;

import static wad.seoul_nolgoat.exception.ErrorCode.PARTY_CAPACITY_EXCEEDED;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Party extends BaseTimeEntity {

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String imageUrl;
    private int maxCapacity;
    private int currentCount = 1;
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

    public boolean hasImageUrl() {
        return this.imageUrl != null && !this.imageUrl.isEmpty();
    }

    public void addParticipant() {
        if (currentCount >= maxCapacity) {
            throw new ApiException(PARTY_CAPACITY_EXCEEDED);
        }
        currentCount++;
    }

    public void removeParticipant() {
        currentCount--;
    }
}
