package wad.seoul_nolgoat.util.mapper;

import wad.seoul_nolgoat.domain.inquiry.Inquiry;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.web.inquiry.dto.request.InquirySaveDto;
import wad.seoul_nolgoat.web.inquiry.dto.response.InquiryDetailsDto;
import wad.seoul_nolgoat.web.inquiry.dto.response.InquiryListDto;

import static wad.seoul_nolgoat.util.DateTimeUtil.formatDate;

public class InquiryMapper {

    public static Inquiry toEntity(User user, InquirySaveDto inquirySaveDto) {
        return new Inquiry(
                inquirySaveDto.getTitle(),
                inquirySaveDto.getContent(),
                inquirySaveDto.getIsPublic(),
                user
        );
    }

    public static InquiryDetailsDto toInquiryDetailsDto(Inquiry inquiry) {
        return new InquiryDetailsDto(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getIsPublic(),
                inquiry.getUser().getId(),
                inquiry.getUser().getNickname(),
                inquiry.getUser().getProfileImage(),
                formatDate(inquiry.getCreatedDate())
        );
    }

    public static InquiryListDto toInquiryListDto(Inquiry inquiry) {
        return new InquiryListDto(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getIsPublic(),
                inquiry.getUser().getId(),
                inquiry.getUser().getNickname(),
                inquiry.getUser().getProfileImage(),
                formatDate(inquiry.getCreatedDate())
        );
    }
}
