package wad.seoul_nolgoat.domain.user;

import jakarta.persistence.*;
import wad.seoul_nolgoat.domain.BaseTimeEntity;

import java.time.LocalDate;

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
}