package wad.seoul_nolgoat.service.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.notice.Notice;
import wad.seoul_nolgoat.domain.notice.NoticeRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.notfound.NoticeNotFoundException;
import wad.seoul_nolgoat.exception.notfound.UserNotFoundException;
import wad.seoul_nolgoat.util.mapper.NoticeMapper;
import wad.seoul_nolgoat.web.notice.dto.request.NoticeSaveDto;
import wad.seoul_nolgoat.web.notice.dto.request.NoticeUpdateDto;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeDetailsDto;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeListDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    public Long save(Long userId, NoticeSaveDto noticeSaveDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return noticeRepository.save(NoticeMapper.toEntity(user, noticeSaveDto)).getId();
    }

    public NoticeDetailsDto findByNoticeId(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(NoticeNotFoundException::new);

        return NoticeMapper.toNoticeDetailsDto(notice);
    }

    public List<NoticeListDto> findAllNotice() {
        List<Notice> notices = noticeRepository.findAll();

        return notices.stream()
                .map(NoticeMapper::toNoticeListDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(Long noticeId, NoticeUpdateDto noticeUpdateDto) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(NoticeNotFoundException::new);

        notice.update(
                noticeUpdateDto.getTitle(),
                noticeUpdateDto.getContent()
        );
    }

    public void deleteById(Long noticeId) {
        if (!noticeRepository.existsById(noticeId)) {
            throw new NoticeNotFoundException();
        }

        noticeRepository.deleteById(noticeId);
    }

    @Transactional
    public void increaseViews(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(NoticeNotFoundException::new);
        notice.increaseViews();
    }
}
