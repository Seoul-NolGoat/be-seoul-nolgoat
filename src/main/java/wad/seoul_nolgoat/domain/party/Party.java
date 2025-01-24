package wad.seoul_nolgoat.domain.party;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.domain.BaseTimeEntity;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.exception.ApplicationException;

import java.time.LocalDateTime;

import static wad.seoul_nolgoat.exception.ErrorCode.PARTY_CAPACITY_EXCEEDED;
import static wad.seoul_nolgoat.exception.ErrorCode.PARTY_COUNT_BELOW_MINIMUM;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Party extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private int maxCapacity;
    private int currentCount = 1;
    private LocalDateTime meetingDate;
    private boolean isClosed;
    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private AdministrativeDistrict administrativeDistrict;

    @ManyToOne(fetch = FetchType.LAZY)
    private User host;

    public Party(
            String title,
            String content,
            int maxCapacity,
            LocalDateTime meetingDate,
            AdministrativeDistrict administrativeDistrict,
            User host
    ) {
        this.title = title;
        this.content = content;
        this.maxCapacity = maxCapacity;
        this.meetingDate = meetingDate;
        this.isClosed = false;
        this.isDeleted = false;
        this.administrativeDistrict = administrativeDistrict;
        this.host = host;
    }

    public void close() {
        isClosed = true;
    }

    public void update(
            String title,
            String content,
            int maxCapacity,
            LocalDateTime meetingDate,
            AdministrativeDistrict administrativeDistrict
    ) {
        this.title = title;
        this.content = content;
        this.maxCapacity = maxCapacity;
        this.meetingDate = meetingDate;
        this.administrativeDistrict = administrativeDistrict;
    }

    public void delete() {
        isDeleted = true;
    }

    public void incrementParticipantCount() {
        if (currentCount >= maxCapacity) {
            throw new ApplicationException(PARTY_CAPACITY_EXCEEDED);
        }
        currentCount++;
    }

    public void decrementParticipantCount() {
        if (currentCount <= 1) {
            throw new ApplicationException(PARTY_COUNT_BELOW_MINIMUM);
        }
        currentCount--;
    }
}
