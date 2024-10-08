package wad.seoul_nolgoat.web.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.notice.NoticeService;
import wad.seoul_nolgoat.web.notice.dto.request.NoticeSaveDto;
import wad.seoul_nolgoat.web.notice.dto.request.NoticeUpdateDto;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeDetailsDto;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeListDto;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/notices")
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/{userId}")
    public ResponseEntity<Void> createNotice(
            @PathVariable Long userId,
            @RequestBody NoticeSaveDto noticeSaveDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long noticeId = noticeService.save(userId, noticeSaveDto);
        URI location = uriComponentsBuilder.path("/api/notices/{noticeId}")
                .buildAndExpand(noticeId)
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailsDto> showNoticeByNoticeId(@PathVariable Long noticeId) {
        return ResponseEntity
                .ok(noticeService.findByNoticeId(noticeId));
    }

    @GetMapping
    public ResponseEntity<List<NoticeListDto>> showAllNotices() {
        return ResponseEntity
                .ok(noticeService.findAllNotice());
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<?> update(
            @PathVariable Long noticeId,
            @RequestBody NoticeUpdateDto noticeUpdateDto
    ) {
        noticeService.update(noticeId, noticeUpdateDto);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long noticeId) {
        noticeService.deleteById(noticeId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{noticeId}/views")
    public ResponseEntity<Void> increaseViews(@PathVariable Long noticeId) {
        noticeService.increaseViews(noticeId);
        return ResponseEntity.noContent().build();
    }
}