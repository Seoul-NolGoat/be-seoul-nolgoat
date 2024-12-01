package wad.seoul_nolgoat.web.notice;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.notice.NoticeService;
import wad.seoul_nolgoat.web.notice.dto.request.NoticeSaveDto;
import wad.seoul_nolgoat.web.notice.dto.request.NoticeUpdateDto;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeDetailsDto;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeListDto;

import java.net.URI;
import java.util.List;

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
            @RequestBody NoticeSaveDto noticeSaveDto,
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

    @Operation(summary = "공지 사항 목록 조회")
    @GetMapping
    public ResponseEntity<List<NoticeListDto>> showAllNotices() {
        return ResponseEntity
                .ok(noticeService.findAllNotice());
    }

    @Operation(summary = "공지 사항 수정")
    @PutMapping("/{noticeId}")
    public ResponseEntity<Void> update(
            @PathVariable Long noticeId,
            @RequestBody NoticeUpdateDto noticeUpdateDto
    ) {
        noticeService.update(noticeId, noticeUpdateDto);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "공지 사항 삭제")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long noticeId) {
        noticeService.deleteById(noticeId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Hidden
    @PutMapping("/{noticeId}/views")
    public ResponseEntity<Void> increaseViews(@PathVariable Long noticeId) {
        noticeService.increaseViews(noticeId);
        return ResponseEntity.noContent().build();
    }
}