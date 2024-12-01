package wad.seoul_nolgoat.web.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.inquiry.InquiryService;
import wad.seoul_nolgoat.web.inquiry.dto.request.InquirySaveDto;
import wad.seoul_nolgoat.web.inquiry.dto.request.InquiryUpdateDto;
import wad.seoul_nolgoat.web.inquiry.dto.response.InquiryDetailsDto;
import wad.seoul_nolgoat.web.inquiry.dto.response.InquiryListDto;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
@RestController
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    public ResponseEntity<Void> createInquiry(
            @AuthenticationPrincipal OAuth2User loginUser,
            @RequestBody InquirySaveDto inquirySaveDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long inquiryId = inquiryService.save(loginUser.getName(), inquirySaveDto);
        URI location = uriComponentsBuilder.path("/api/inquiries/{inquiryId}")
                .buildAndExpand(inquiryId)
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryDetailsDto> showInquiryByInquiryId(@PathVariable Long inquiryId) {
        return ResponseEntity
                .ok(inquiryService.findByInquiryId(inquiryId));
    }

    @GetMapping
    public ResponseEntity<List<InquiryListDto>> showAllInquiries() {
        return ResponseEntity
                .ok(inquiryService.findAllInquiry());
    }

    @PutMapping("/{inquiryId}")
    public ResponseEntity<Void> update(
            @PathVariable Long inquiryId,
            @RequestBody InquiryUpdateDto inquiryUpdateDto
    ) {
        inquiryService.update(inquiryId, inquiryUpdateDto);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long inquiryId) {
        inquiryService.deleteById(inquiryId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
