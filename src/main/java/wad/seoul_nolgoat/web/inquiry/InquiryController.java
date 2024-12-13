package wad.seoul_nolgoat.web.inquiry;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@Tag(name = "건의 사항")
@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
@RestController
public class InquiryController {

    private final InquiryService inquiryService;

    @Operation(summary = "건의 사항 작성")
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

    @Operation(summary = "건의 사항 단건 조회")
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryDetailsDto> showInquiryByInquiryId(@PathVariable Long inquiryId) {
        return ResponseEntity
                .ok(inquiryService.findByInquiryId(inquiryId));
    }

    @Operation(summary = "건의 사항 목록 조회(페이지네이션)")
    @GetMapping
    public ResponseEntity<Page<InquiryListDto>> showInquiries(Pageable pageable) {
        return ResponseEntity
                .ok(inquiryService.findInquiriesWithPagination(pageable));
    }

    @Operation(summary = "건의 사항 수정")
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

    @Operation(summary = "건의 사항 삭제")
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long inquiryId) {
        inquiryService.deleteById(inquiryId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
