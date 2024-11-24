package wad.seoul_nolgoat.auth.oauth2.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UnlinkResponse {

    private final Long id;

    @JsonCreator
    public UnlinkResponse(Long id) {
        this.id = id;
    }
}
