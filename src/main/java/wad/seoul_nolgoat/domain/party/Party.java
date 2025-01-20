package wad.seoul_nolgoat.domain.party;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.domain.BaseTimeEntity;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.exception.ApplicationException;
import wad.seoul_nolgoat.web.party.request.PartyUpdateDto;

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
            String district,
            User host
    ) {
        this.title = title;
        this.content = content;
        this.maxCapacity = maxCapacity;
        this.meetingDate = meetingDate;
        this.isClosed = false;
        this.isDeleted = false;
        this.administrativeDistrict = AdministrativeDistrict.valueOf(district);
        this.host = host;
    }

    public void close() {
        isClosed = true;
    }

    public void update(PartyUpdateDto partyUpdateDto) {
        this.title = partyUpdateDto.getTitle();
        this.content = partyUpdateDto.getContent();
        this.maxCapacity = partyUpdateDto.getMaxCapacity();
        this.meetingDate = partyUpdateDto.getMeetingDate();
        this.administrativeDistrict = AdministrativeDistrict.fromString(partyUpdateDto.getAdministrativeDistrict());
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
        currentCount--;
    }
}
