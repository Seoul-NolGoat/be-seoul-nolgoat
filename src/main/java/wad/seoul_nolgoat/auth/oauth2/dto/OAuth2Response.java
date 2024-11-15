package wad.seoul_nolgoat.auth.oauth2.dto;

public interface OAuth2Response {

    String getProvider();

    String getProviderId();

    String getNickname();

    String getProfileImage();
}
