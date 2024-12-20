package wad.seoul_nolgoat.service.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.store.StoreRepository;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.store.dto.StoreUpdateDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsDto;

import static wad.seoul_nolgoat.exception.ErrorCode.STORE_NOT_FOUND;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreDetailsDto findStoreWithReviewsByStoreId(Long storeId) {
        return storeRepository.findStoreWithReviewsByStoreId(storeId)
                .orElseThrow(() -> new ApiException(STORE_NOT_FOUND));
    }

    @Transactional
    public void update(StoreUpdateDto storeUpdateDto) {
        Store store = storeRepository.findById(storeUpdateDto.getId())
                .orElseThrow(() -> new ApiException(STORE_NOT_FOUND));
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
}
