package wad.seoul_nolgoat.service.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.notice.Notice;
import wad.seoul_nolgoat.domain.notice.NoticeRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApplicationException;
import wad.seoul_nolgoat.util.mapper.NoticeMapper;
import wad.seoul_nolgoat.web.notice.dto.request.NoticeSaveDto;
import wad.seoul_nolgoat.web.notice.dto.request.NoticeUpdateDto;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeDetailsDto;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeDetailsForListDto;

import static wad.seoul_nolgoat.exception.ErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(String loginId, NoticeSaveDto noticeSaveDto) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        return noticeRepository.save(NoticeMapper.toEntity(user, noticeSaveDto)).getId();
    }

    public NoticeDetailsDto findByNoticeId(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApplicationException(NOTICE_NOT_FOUND));

        return NoticeMapper.toNoticeDetailsDto(notice);
    }

    public Page<NoticeDetailsForListDto> findNoticesWithPagination(Pageable pageable) {
        return noticeRepository.findAllWithPagination(pageable);
    }

    @Transactional
    public void update(
            String loginId,
            Long noticeId,
            NoticeUpdateDto noticeUpdateDto
    ) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApplicationException(NOTICE_NOT_FOUND));

        // 공지 작성자가 맞는지 검증
        if (!loginId.equals(notice.getUser().getLoginId())) {
            throw new ApplicationException(NOTICE_WRITER_MISMATCH);
        }

        notice.update(
                noticeUpdateDto.getTitle(),
                noticeUpdateDto.getContent()
        );
    }

    @Transactional
    public void delete(String loginId, Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApplicationException(NOTICE_NOT_FOUND));

        // 공지 작성자가 맞는지 검증
        if (!loginId.equals(notice.getUser().getLoginId())) {
            throw new ApplicationException(NOTICE_WRITER_MISMATCH);
        }

        noticeRepository.delete(notice);
    }

    @Transactional
    public void increaseViews(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApplicationException(NOTICE_NOT_FOUND));
        notice.increaseViews();
    }
}
