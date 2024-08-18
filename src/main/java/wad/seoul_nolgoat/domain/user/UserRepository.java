package wad.seoul_nolgoat.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginId(String LoginId);

    Optional<User> findByLoginId(String LoginId);
}
