package com.eatda.data.api.review;

import com.eatda.data.form.review.ReviewRequestDTO;
import com.eatda.data.form.review.ReviewResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewApiService {
    // 리뷰 작성
    @POST("/api/reviews/{restaurantId}/addReview")
    Call<ReviewResponseDTO> addReview(
            @Path("restaurantId") Long restaurantId,
            @Body ReviewRequestDTO reviewRequestDto,
            @Query("childId") Long childId
    );

    // 아동ID 별로 리뷰 리스트 조회
    @GET("/api/reviews/child/{childId}")
    Call<List<ReviewResponseDTO>> getReviewListByChildId(
            @Path("childId") Long childId
    );

    // 특정 리뷰 조회
    @GET("/api/reviews/{reviewId}")
    Call<ReviewResponseDTO> getReviewById(
            @Path("reviewId") Long reviewId
    );

    // 특정 가게의 모든 리뷰 조회
    @GET("/api/reviews/restaurant/{restaurantId}")
    Call<List<ReviewResponseDTO>> getReviewsByRestaurantId(
            @Path("restaurantId") Long restaurantId
    );
}
