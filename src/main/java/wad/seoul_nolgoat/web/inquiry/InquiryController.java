package wad.seoul_nolgoat.web.inquiry;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.inquiry.InquiryService;
import wad.seoul_nolgoat.util.ValidationUtil;
import wad.seoul_nolgoat.web.inquiry.dto.request.InquirySaveDto;
import wad.seoul_nolgoat.web.inquiry.dto.request.InquiryUpdateDto;
import wad.seoul_nolgoat.web.inquiry.dto.response.InquiryDetailsDto;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
@RestController
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping("/{userId}")
    public ResponseEntity<Void> createInquiry(
            @PathVariable Long userId,
            @RequestBody InquirySaveDto inquirySaveDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long inquiryId = inquiryService.save(userId, inquirySaveDto);
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

    @PutMapping("/{inquiryId}")
    public ResponseEntity<?> update(
            @PathVariable Long inquiryId,
            @RequestBody InquiryUpdateDto inquiryUpdateDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(ValidationUtil.extractErrors(bindingResult));
        }
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
