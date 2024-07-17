package wad.seoul_nolgoat.domain.store;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.domain.BaseTimeEntity;
import wad.seoul_nolgoat.domain.review.Review;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    private String name;
    private String category;
    private String managementNumber;
    private String phoneNumber;
    private String lotAddress;
    private String roadAddress;
    private double latitude;
    private double longitude;
    private double kakaoAverageGrade;
    private double nolgoatAverageGrade;
    private String placeUrl;
    private Boolean isDeleted;

    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
    private List<Review> reviews = new ArrayList<>();

    public Store(
            StoreType storeType,
            String name,
            String managementNumber,
            String lotAddress,
            String roadAddress) {
        this.storeType = storeType;
        this.name = name;
        this.managementNumber = managementNumber;
        this.lotAddress = lotAddress;
        this.roadAddress = roadAddress;
        this.isDeleted = false;
    }

    public void update(
            String name,
            String category,
            String managementNumber,
            String phoneNumber,
            String lotAddress,
            String roadAddress,
            double latitude,
            double longitude,
            double kakaoAverageGrade,
            String placeUrl) {
        this.name = name;
        this.category = category;
        this.managementNumber = managementNumber;
        this.phoneNumber = phoneNumber;
        this.lotAddress = lotAddress;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.kakaoAverageGrade = kakaoAverageGrade;
        this.placeUrl = placeUrl;
    }

    public void updateCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updateAdditionalInfo(
            String category,
            String phoneNumber,
            String placeUrl) {
        this.category = category;
        this.phoneNumber = phoneNumber;
        this.placeUrl = placeUrl;
    }

    public void updateKakaoAverageGrade(double kakaoAverageGrade) {
        this.kakaoAverageGrade = kakaoAverageGrade;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
