package wad.seoul_nolgoat.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.domain.BaseTimeEntity;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String loginId;
    private String password;
    private String nickname;
    private String profileImage;
    private String gender;
    private LocalDate birthDate;
    private boolean isDeleted;

    public User(
            String loginId,
            String password,
            String nickname,
            String profileImage,
            String gender,
            LocalDate birthDate) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public void update(
            String password,
            String nickname,
            String profileImage) {
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
