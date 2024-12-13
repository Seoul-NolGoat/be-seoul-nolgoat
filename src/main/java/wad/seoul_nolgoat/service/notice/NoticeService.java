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
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.util.mapper.NoticeMapper;
import wad.seoul_nolgoat.web.notice.dto.request.NoticeSaveDto;
import wad.seoul_nolgoat.web.notice.dto.request.NoticeUpdateDto;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeDetailsDto;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeListDto;

import static wad.seoul_nolgoat.exception.ErrorCode.NOTICE_NOT_FOUND;
import static wad.seoul_nolgoat.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(String loginId, NoticeSaveDto noticeSaveDto) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        return noticeRepository.save(NoticeMapper.toEntity(user, noticeSaveDto)).getId();
    }

    public NoticeDetailsDto findByNoticeId(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApiException(NOTICE_NOT_FOUND));

        return NoticeMapper.toNoticeDetailsDto(notice);
    }

    public Page<NoticeListDto> findNoticesWithPagination(Pageable pageable) {
        return noticeRepository.findAllWithPagination(pageable);
    }

    @Transactional
    public void update(Long noticeId, NoticeUpdateDto noticeUpdateDto) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApiException(NOTICE_NOT_FOUND));

        notice.update(
                noticeUpdateDto.getTitle(),
                noticeUpdateDto.getContent()
        );
    }

    @Transactional
    public void deleteById(Long noticeId) {
        if (!noticeRepository.existsById(noticeId)) {
            throw new ApiException(NOTICE_NOT_FOUND);
        }

        noticeRepository.deleteById(noticeId);
    }

    @Transactional
    public void increaseViews(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ApiException(NOTICE_NOT_FOUND));
        notice.increaseViews();
    }
}
