package wad.seoul_nolgoat.domain.store;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.domain.BaseTimeEntity;
import wad.seoul_nolgoat.domain.review.Review;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String name;
    private String category;
    private String management_number;
    private String phoneNumber;
    private String lot_address;
    private String road_address;
    private double latitude;
    private double longitude;
    private double kakaoAverageGrade;
    private double noloatAverageGrade;

    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
    private List<Review> reviews = new ArrayList<>();
}
