package wad.seoul_nolgoat.web.party;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.party.PartyService;
import wad.seoul_nolgoat.web.party.request.PartySaveDto;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/parties")
@RestController
public class PartyController {

    private final PartyService partyService;

    @PostMapping
    public ResponseEntity<Void> createParty(
            @AuthenticationPrincipal OAuth2User loginUser,
            @RequestPart("party") PartySaveDto partySaveDto,
            @RequestPart(value = "file", required = false) MultipartFile image,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long partyId = partyService.createParty(
                loginUser.getName(),
                partySaveDto,
                image
        );

        URI location = uriComponentsBuilder.path("/api/parties/{partyId}")
                .buildAndExpand(partyId)
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }
}
