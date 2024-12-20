package wad.seoul_nolgoat.domain.store;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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
    private Point location;
    private double kakaoAverageGrade;
    private double nolgoatAverageGrade;
    private String placeUrl;
    private Boolean isDeleted;

    @OneToMany(mappedBy = "store")
    private List<Review> reviews = new ArrayList<>();

    public Store(
            StoreType storeType,
            String name,
            String managementNumber,
            String lotAddress,
            String roadAddress
    ) {
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
        setLocation(longitude, latitude);
        this.kakaoAverageGrade = kakaoAverageGrade;
        this.placeUrl = placeUrl;
    }

    public void updateCoordinates(double longitude, double latitude) {
        setLocation(longitude, latitude);
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

    // 처음 리뷰를 작성할 때, 해당 Nolgoat 평점을 기존 Nolgoat 평균 평점에 반영
    public void addNolgoatGrade(int addedNolgoatGrade) {
        double previousNolgoatAverageGrade = this.nolgoatAverageGrade;
        int reviewCount = reviews.size();

        this.nolgoatAverageGrade = ((previousNolgoatAverageGrade * reviewCount) + addedNolgoatGrade) / (reviewCount + 1);
    }

    // 리뷰를 수정할 때, 기존 Nolgoat 평균 평점을 업데이트
    public void updateNolgoatAverageGradeForEditReview(int nolgoatGradeDifference) {
        double previousNolgoatAverageGrade = this.nolgoatAverageGrade;
        int reviewCount = reviews.size();

        this.nolgoatAverageGrade = ((previousNolgoatAverageGrade * reviewCount) + nolgoatGradeDifference) / reviewCount;
    }

    // 리뷰를 삭제할 때, 기존 Nolgoat 평균 평점을 업데이트
    public void updateNolgoatAverageGradeForDeleteReview(int nolgoatGrade) {
        double previousNolgoatAverageGrade = this.nolgoatAverageGrade;
        int reviewCount = reviews.size();

        this.nolgoatAverageGrade = ((previousNolgoatAverageGrade * reviewCount) - nolgoatGrade) / (reviewCount - 1);
    }

    public void delete() {
        this.isDeleted = true;
    }

    public double getLongitude() {
        return location.getX();
    }

    public double getLatitude() {
        return location.getY();
    }

    private void setLocation(double longitude, double latitude) {
        GeometryFactory geometryFactory = new GeometryFactory();
        this.location = geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }
}
