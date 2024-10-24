package wad.seoul_nolgoat.domain.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);
}
