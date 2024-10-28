package com.eatda.data.form.review;

import java.time.LocalDateTime;

public class ReviewResponseDTO {
    private Long id;
    private int review_star;
    private String review_body;
    private LocalDateTime createdAt;
    private Long childId;
    private Long restaurantId;

    // 기본 생성자
    public ReviewResponseDTO() {}

    // 매개변수가 있는 생성자
    public ReviewResponseDTO(Long id, int review_star, String review_body, LocalDateTime createdAt, Long childId, Long restaurantId) {
        this.id = id;
        this.review_star = review_star;
        this.review_body = review_body;
        this.createdAt = createdAt;
        this.childId = childId;
        this.restaurantId = restaurantId;
    }

    // Getter 및 Setter 메서드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getReview_star() {
        return review_star;
    }

    public void setReview_star(int review_star) {
        this.review_star = review_star;
    }

    public String getReview_body() {
        return review_body;
    }

    public void setReview_body(String review_body) {
        this.review_body = review_body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    // toString 메서드
    @Override
    public String toString() {
        return "ReviewResponseDTO{" +
                "id=" + id +
                ", review_star=" + review_star +
                ", review_body='" + review_body + '\'' +
                ", createdAt=" + createdAt +
                ", childId=" + childId +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
