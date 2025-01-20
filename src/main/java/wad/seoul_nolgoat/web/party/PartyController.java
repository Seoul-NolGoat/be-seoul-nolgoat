package wad.seoul_nolgoat.web.party;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.party.PartyService;
import wad.seoul_nolgoat.web.party.request.PartySaveDto;
import wad.seoul_nolgoat.web.party.request.PartySearchConditionDto;
import wad.seoul_nolgoat.web.party.request.PartyUpdateDto;
import wad.seoul_nolgoat.web.party.response.HostedPartyListDto;
import wad.seoul_nolgoat.web.party.response.PartyDetailsDto;
import wad.seoul_nolgoat.web.party.response.PartyListDto;

import java.net.URI;

@Tag(name = "파티")
@RequiredArgsConstructor
@RequestMapping("/api/parties")
@RestController
public class PartyController {

    private final PartyService partyService;

    @Operation(summary = "파티 생성")
    @PostMapping
    public ResponseEntity<Void> createParty(
            @AuthenticationPrincipal OAuth2User loginUser,
            @Valid @RequestBody PartySaveDto partySaveDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long partyId = partyService.createParty(loginUser.getName(), partySaveDto);

        URI location = uriComponentsBuilder.path("/api/parties/{partyId}")
                .buildAndExpand(partyId)
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @Operation(summary = "파티 참여")
    @PostMapping("/{partyId}/join")
    public ResponseEntity<Void> joinParty(@AuthenticationPrincipal OAuth2User loginUser, @PathVariable Long partyId) {
        partyService.joinParty(loginUser.getName(), partyId);

        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(summary = "파티 마감")
    @PostMapping("/{partyId}")
    public ResponseEntity<Void> close(@AuthenticationPrincipal OAuth2User loginUser, @PathVariable Long partyId) {
        partyService.close(loginUser.getName(), partyId);

        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(summary = "파티 수정")
    @PatchMapping("/{partyId}")
    public ResponseEntity<Void> update(
            @AuthenticationPrincipal OAuth2User loginUser,
            @Valid @RequestBody PartyUpdateDto partyUpdateDto,
            @PathVariable Long partyId
    ) {
        partyService.update(
                partyUpdateDto,
                loginUser.getName(),
                partyId
        );

        return ResponseEntity
                .noContent()
                .build();
    }


    @Operation(summary = "파티 삭제")
    @DeleteMapping("/{partyId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal OAuth2User loginUser, @PathVariable Long partyId) {
        partyService.delete(loginUser.getName(), partyId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "참여자 추방")
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

    @Operation(summary = "파티 단건 조회")
    @GetMapping("/{partyId}")
    public ResponseEntity<PartyDetailsDto> showPartyByPartyId(@PathVariable Long partyId) {
        return ResponseEntity
                .ok(partyService.findPartyDetailsById(partyId));
    }

    @Operation(summary = "파티 목록 조회")
    @GetMapping
    public ResponseEntity<Page<PartyListDto>> showPartiesByCondition(PartySearchConditionDto partySearchConditionDto) {
        return ResponseEntity
                .ok(partyService.findPartiesWithConditionAndPagination(partySearchConditionDto));
    }

    @Operation(summary = "내가 생성한 파티 목록 조회")
    @GetMapping("/me/created")
    public ResponseEntity<Page<HostedPartyListDto>> showMyHostedParties(@AuthenticationPrincipal OAuth2User loginUser, Pageable pageable) {
        return ResponseEntity
                .ok(partyService.findHostedPartiesByLoginId(loginUser.getName(), pageable));
    }

    @Operation(summary = "내가 참여한 파티 목록 조회")
    @GetMapping("/me/joined")
    public ResponseEntity<Page<PartyListDto>> showMyJoinedParties(@AuthenticationPrincipal OAuth2User loginUser, Pageable pageable) {
        return ResponseEntity
                .ok(partyService.findJoinedPartiesByLoginId(loginUser.getName(), pageable));
    }
}
