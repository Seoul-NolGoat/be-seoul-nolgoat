package wad.seoul_nolgoat.web.admin.dto.response;

import lombok.Getter;

@Getter
public class TestTokenDto {

    private String accessToken;
    private String refreshToken;

    public TestTokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
