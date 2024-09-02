package wad.seoul_nolgoat.service.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.store.StoreRepository;
import wad.seoul_nolgoat.exception.ErrorMessages;
import wad.seoul_nolgoat.exception.StoreNotFoundException;
import wad.seoul_nolgoat.service.store.dto.StoreUpdateDto;
import wad.seoul_nolgoat.util.mapper.StoreMapper;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsDto;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreDetailsDto findByStoreId(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(ErrorMessages.STORE_NOT_FOUND_MESSAGE));

        return StoreMapper.toStoreDetailsDto(store);
    }

    @Transactional
    public void update(StoreUpdateDto storeUpdateDto) {
        Store store = storeRepository.findById(storeUpdateDto.getId())
                .orElseThrow(() -> new StoreNotFoundException(ErrorMessages.STORE_NOT_FOUND_MESSAGE));
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

    @Transactional
    public void deleteById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException(ErrorMessages.STORE_NOT_FOUND_MESSAGE));
        store.delete();
    }
}
