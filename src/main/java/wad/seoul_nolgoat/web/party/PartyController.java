package wad.seoul_nolgoat.web.party;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.party.PartyService;
import wad.seoul_nolgoat.web.party.request.PartySaveDto;
import wad.seoul_nolgoat.web.party.request.PartySearchConditionDto;
import wad.seoul_nolgoat.web.party.response.HostedPartyListDto;
import wad.seoul_nolgoat.web.party.response.PartyDetailsDto;
import wad.seoul_nolgoat.web.party.response.PartyListDto;

import java.net.URI;
import java.time.LocalDateTime;

@Hidden
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
        partyService.joinParty(
                loginUser.getName(),
                partyId,
                LocalDateTime.now()
        );

        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/{partyId}")
    public ResponseEntity<Void> closeById(@AuthenticationPrincipal OAuth2User loginUser, @PathVariable Long partyId) {
        partyService.closeById(loginUser.getName(), partyId);

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/{partyId}")
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal OAuth2User loginUser, @PathVariable Long partyId) {
        partyService.deleteById(loginUser.getName(), partyId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{partyId}/participants/{userId}")
    public ResponseEntity<Void> banParticipantFromParty(
            @AuthenticationPrincipal OAuth2User loginUser,
            @PathVariable Long partyId,
            @PathVariable Long userId
    ) {
        partyService.banParticipantFromParty(loginUser.getName(), partyId, userId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/{partyId}")
    public ResponseEntity<PartyDetailsDto> showPartyByPartyId(@PathVariable Long partyId) {
        return ResponseEntity
                .ok(partyService.findPartyDetailsById(partyId, LocalDateTime.now()));
    }

    @GetMapping
    public ResponseEntity<Page<PartyListDto>> showPartiesByCondition(PartySearchConditionDto partySearchConditionDto) {
        return ResponseEntity
                .ok(partyService.findPartiesWithConditionAndPagination(partySearchConditionDto));
    }

    @GetMapping("/me/created")
    public ResponseEntity<Page<HostedPartyListDto>> showMyHostedParties(@AuthenticationPrincipal OAuth2User loginUser, Pageable pageable) {
        return ResponseEntity
                .ok(partyService.findHostedPartiesByLoginId(loginUser.getName(), pageable));
    }

    @GetMapping("/me/joined")
    public ResponseEntity<Page<PartyListDto>> showMyJoinedParties(@AuthenticationPrincipal OAuth2User loginUser, Pageable pageable) {
        return ResponseEntity
                .ok(partyService.findJoinedPartiesByLoginId(loginUser.getName(), pageable));
    }
}
