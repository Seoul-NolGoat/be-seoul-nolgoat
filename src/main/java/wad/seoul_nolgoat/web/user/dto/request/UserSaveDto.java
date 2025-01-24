package wad.seoul_nolgoat.web.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class UserSaveDto {

    @NotBlank
    @Size(min = 6, max = 16, message = "아이디는 6자 이상, 16자 이하여야 합니다.")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "아이디는 영문 대소문자와 숫자만 사용할 수 있습니다.")
    private final String loginId;

    @NotBlank
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).*$", message = "비밀번호는 특수문자를 하나이상 포함해야 합니다.")
    private final String password;

    @NotBlank
    @Size(min = 6, max = 12, message = "닉네임은 6자 이상, 12자 이하여야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "닉네임은 한글, 영문 대소문자, 숫자만 사용할 수 있습니다.")
    private final String nickname;

    private final String profileImage;
    private final String gender;
    private final LocalDate birthDate;
}
