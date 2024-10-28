package com.eatda.data.form.review;


public class ReviewRequestDTO {

    private String reviewBody; // 리뷰 내용
    private int reviewStar;    // 별점

    // 생성자
    public ReviewRequestDTO(String reviewBody, int reviewStar) {
        this.reviewBody = reviewBody;
        this.reviewStar = reviewStar;
    }

    // Getter와 Setter
    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public int getReviewStar() {
        return reviewStar;
    }

    public void setReviewStar(int reviewStar) {
        this.reviewStar = reviewStar;
    }
}
