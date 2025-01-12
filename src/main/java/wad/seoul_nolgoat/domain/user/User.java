package wad.seoul_nolgoat.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.domain.BaseTimeEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;
    private String password;
    private String nickname;
    private String profileImage;
    private String email;
    private boolean isDeleted;

    public User(
            String loginId,
            String password,
            String nickname,
            String profileImage,
            String email
    ) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.email = email;
        this.isDeleted = false;
    }

    public void update(
            String nickname,
            String profileImage,
            String email
    ) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.email = email;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void reactivate() {
        this.isDeleted = false;
    }
}
