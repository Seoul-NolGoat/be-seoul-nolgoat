package wad.seoul_nolgoat.auth.oauth2.dto;

import java.util.Optional;

public interface OAuth2Response {

    String getProvider();

    String getProviderId();

    String getNickname();

    Optional<String> getProfileImage();

    String getEmail();
}
