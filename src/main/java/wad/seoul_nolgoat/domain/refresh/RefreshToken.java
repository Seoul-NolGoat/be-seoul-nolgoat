package wad.seoul_nolgoat.domain.refresh;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshToken;
    private String loginId;
    private String expirationDate;

    public RefreshToken(
            String refreshToken,
            String loginId,
            String expirationDate
    ) {
        this.refreshToken = refreshToken;
        this.loginId = loginId;
        this.expirationDate = expirationDate;
    }
}
