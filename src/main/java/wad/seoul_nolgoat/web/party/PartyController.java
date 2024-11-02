package wad.seoul_nolgoat.web.party;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/{partyId}/join")
    public ResponseEntity<Void> joinParty(@AuthenticationPrincipal OAuth2User loginUser, @PathVariable Long partyId) {
        partyService.joinParty(loginUser.getName(), partyId);

        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/{partyId}")
    public ResponseEntity<Void> closeById(@PathVariable Long partyId) {
        partyService.closeById(partyId);

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/{partyId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long partyId) {
        partyService.deleteById(partyId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
