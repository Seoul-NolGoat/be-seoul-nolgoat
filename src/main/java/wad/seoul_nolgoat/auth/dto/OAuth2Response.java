package wad.seoul_nolgoat.auth.dto;

public interface OAuth2Response {

    String getProvider();

    String getProviderId();

    String getNickname();

    String getProfileImage();
}
