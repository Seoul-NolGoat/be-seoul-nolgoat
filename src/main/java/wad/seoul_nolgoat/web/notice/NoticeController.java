package wad.seoul_nolgoat.web.notice;

import io.swagger.v3.oas.annotations.Hidden;
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
import wad.seoul_nolgoat.service.notice.NoticeService;
import wad.seoul_nolgoat.web.notice.dto.request.NoticeSaveDto;
import wad.seoul_nolgoat.web.notice.dto.request.NoticeUpdateDto;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeDetailsDto;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeDetailsForListDto;

import java.net.URI;

@Tag(name = "공지 사항")
@RequiredArgsConstructor
@RequestMapping("/api/notices")
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지 사항 작성")
    @PostMapping
    public ResponseEntity<Void> createNotice(
            @AuthenticationPrincipal OAuth2User loginUser,
            @Valid @RequestBody NoticeSaveDto noticeSaveDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long noticeId = noticeService.save(loginUser.getName(), noticeSaveDto);
        URI location = uriComponentsBuilder.path("/api/notices/{noticeId}")
                .buildAndExpand(noticeId)
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @Operation(summary = "공지 사항 단건 조회")
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailsDto> showNoticeByNoticeId(@PathVariable Long noticeId) {
        return ResponseEntity
                .ok(noticeService.findByNoticeId(noticeId));
    }

    @Operation(summary = "공지 사항 목록 조회(페이지네이션)")
    @GetMapping
    public ResponseEntity<Page<NoticeDetailsForListDto>> showNotices(Pageable pageable) {
        return ResponseEntity
                .ok(noticeService.findNoticesWithPagination(pageable));
    }

    @Operation(summary = "공지 사항 수정")
    @PatchMapping("/{noticeId}")
    public ResponseEntity<Void> update(
            @AuthenticationPrincipal OAuth2User loginUser,
            @PathVariable Long noticeId,
            @Valid @RequestBody NoticeUpdateDto noticeUpdateDto
    ) {
        noticeService.update(
                loginUser.getName(),
                noticeId,
                noticeUpdateDto
        );

        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "공지 사항 삭제")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal OAuth2User loginUser,
            @PathVariable Long noticeId
    ) {
        noticeService.delete(loginUser.getName(), noticeId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Hidden
    @PatchMapping("/{noticeId}/views")
    public ResponseEntity<Void> increaseViews(@PathVariable Long noticeId) {
        noticeService.increaseViews(noticeId);
        return ResponseEntity
                .noContent()
                .build();
    }
}