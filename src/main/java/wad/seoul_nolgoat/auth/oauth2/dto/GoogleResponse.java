package wad.seoul_nolgoat.auth.oauth2.dto;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class GoogleResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getNickname() {
        return attributes.get("name").toString();
    }

    @Override
    public Optional<String> getProfileImage() {
        return Optional.ofNullable(attributes.get("picture").toString());
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }
}
