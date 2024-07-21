package wad.seoul_nolgoat.service.search.sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.domain.store.Store;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CombinationForDistance {

    private final List<Store> stores;
    private double totalDistance;

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }
}
