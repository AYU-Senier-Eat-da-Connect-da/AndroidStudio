package com.eatda.data.form.review;


public class ReviewRequestDTO {

    private String review_body; // 리뷰 내용
    private int review_star;    // 별점

    // 생성자
    public ReviewRequestDTO(String reviewBody, int reviewStar) {
        this.review_body = reviewBody;
        this.review_star = reviewStar;
    }

    // Getter와 Setter
    public String getReviewBody() {
        return review_body;
    }

    public void setReviewBody(String reviewBody) {
        this.review_body = reviewBody;
    }

    public int getReviewStar() {
        return review_star;
    }

    public void setReviewStar(int reviewStar) {
        this.review_star = reviewStar;
    }
}
