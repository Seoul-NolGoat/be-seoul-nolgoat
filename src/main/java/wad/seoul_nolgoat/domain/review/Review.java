package wad.seoul_nolgoat.domain.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.domain.BaseTimeEntity;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.user.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double grade;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public Review(
            double grade,
            String content,
            User user,
            Store store) {
        this.grade = grade;
        this.content = content;
        this.user = user;
        this.store = store;
        store.getReviews().add(this);
    }

    public void update(double grade, String content) {
        this.grade = grade;
        this.content = content;
    }

    public void delete() {
        store.getReviews().remove(this);
    }
}