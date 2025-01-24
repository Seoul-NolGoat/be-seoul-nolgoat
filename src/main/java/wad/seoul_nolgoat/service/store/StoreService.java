package wad.seoul_nolgoat.service.store;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.store.StoreRepository;
import wad.seoul_nolgoat.exception.ApplicationException;
import wad.seoul_nolgoat.service.store.dto.StoreUpdateDto;
import wad.seoul_nolgoat.util.mapper.StoreMapper;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsForListDto;

import static wad.seoul_nolgoat.exception.ErrorCode.STORE_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreDetailsDto findStoreWithReviewsByStoreId(Long storeId) {
        return storeRepository.findStoreWithReviewsByStoreId(storeId)
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
    }

    @Transactional
    public void update(StoreUpdateDto storeUpdateDto) {
        Store store = storeRepository.findById(storeUpdateDto.getId())
                .orElseThrow(() -> new ApplicationException(STORE_NOT_FOUND));
        store.update(
                storeUpdateDto.getName(),
                storeUpdateDto.getCategory(),
                storeUpdateDto.getManagementNumber(),
                storeUpdateDto.getPhoneNumber(),
                storeUpdateDto.getLotAddress(),
                storeUpdateDto.getRoadAddress(),
                storeUpdateDto.getLatitude(),
                storeUpdateDto.getLongitude(),
                storeUpdateDto.getKakaoAverageGrade(),
                storeUpdateDto.getPlaceUrl()
        );
    }

    public Page<StoreDetailsForListDto> searchStores(String searchInput, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String[] keywords = searchInput.split("\\s+"); // 연속된 공백도 처리

        if (keywords.length > 1) {
            return storeRepository
                    .findByNameContainingAndNameContaining(keywords[0], keywords[1], pageable)
                    .map(StoreMapper::toStoreListDto);
        }

        return storeRepository
                .findByNameContaining(keywords[0], pageable)
                .map(StoreMapper::toStoreListDto);
    }
}
