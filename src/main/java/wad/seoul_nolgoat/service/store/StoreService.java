package wad.seoul_nolgoat.service.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.store.StoreRepository;
import wad.seoul_nolgoat.exception.notfound.StoreNotFoundException;
import wad.seoul_nolgoat.service.store.dto.StoreUpdateDto;
import wad.seoul_nolgoat.util.mapper.StoreMapper;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsDto;

@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreDetailsDto findByStoreId(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);

        return StoreMapper.toStoreDetailsDto(store);
    }

    @Transactional
    public void update(StoreUpdateDto storeUpdateDto) {
        Store store = storeRepository.findById(storeUpdateDto.getId())
                .orElseThrow(StoreNotFoundException::new);
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
                .orElseThrow(StoreNotFoundException::new);
        store.delete();
    }

    // Review 추가시 Accommodation averageGrade 업데이트
    @Transactional
    public void updateAverageGradeOnReviewAdd(Long storeId, double addedGrade) {
        Store store = storeRepository.findById(storeId).get();
        double previousAverageGrade = store.getNolgoatAverageGrade();
        int reviewCount = store.getReviews().size();

        double updatedAverageGrade = (previousAverageGrade * reviewCount + addedGrade) / (reviewCount + 1);
        storeRepository.updateNolgoatAverageGrade(storeId, updatedAverageGrade);
    }

    // Review 업데이트시 Accommodation averageGrade 업데이트
    @Transactional
    public void updateAverageGradeOnReviewUpdate(Long storeId, double gradeDifference) {
        Store store = storeRepository.findById(storeId).get();
        double previousAverageGrade = store.getNolgoatAverageGrade();
        int reviewCount = store.getReviews().size();

        double updatedAverageGrade = (previousAverageGrade * reviewCount + gradeDifference) / reviewCount;
        storeRepository.updateNolgoatAverageGrade(storeId, updatedAverageGrade);
    }

    // Review 삭제시 Accommodation averageGrade 업데이트
    @Transactional
    public void updateAverageGradeOnReviewDelete(Long storeId, double deletedGrade) {
        Store store = storeRepository.findById(storeId).get();
        double previousAverageGrade = store.getNolgoatAverageGrade();
        int reviewCount = store.getReviews().size();

        if (reviewCount == 1) {
            storeRepository.updateNolgoatAverageGrade(storeId, 0);
            return;
        }

        double updatedAverageGrade = (previousAverageGrade * reviewCount - deletedGrade) / (reviewCount - 1);
        storeRepository.updateNolgoatAverageGrade(storeId, updatedAverageGrade);
    }
}
