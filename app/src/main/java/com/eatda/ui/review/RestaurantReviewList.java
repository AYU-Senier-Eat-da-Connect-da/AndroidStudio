package com.eatda.ui.review;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.eatda.R;
import com.eatda.data.api.review.ReviewApiService;
import com.eatda.data.api.review.ReviewRetrofitClient;
import com.eatda.data.form.review.ReviewResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantReviewList extends AppCompatActivity {

    private LinearLayout reviewContainer;
    private Long presidentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_review_list);

        // restaurantId를 로그인 정보에서 가져오거나, Intent로부터 받아오는 방식으로 설정해야 함.
        presidentId = getSubFromToken();
        reviewContainer = findViewById(R.id.review_container);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fetchRestaurantReviews(presidentId);
    }

    // 특정 가게의 모든 리뷰 조회
    private void fetchRestaurantReviews(Long presidentId) {
        ReviewApiService service = ReviewRetrofitClient.getRetrofitInstance(this).create(ReviewApiService.class);
        Call<List<ReviewResponseDTO>> call = service.getReviewsByPresidentId(presidentId);

        call.enqueue(new Callback<List<ReviewResponseDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewResponseDTO>> call, Response<List<ReviewResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReviewResponseDTO> reviews = response.body();
                    if (reviews.isEmpty()) {
                        Toast.makeText(RestaurantReviewList.this, "작성된 리뷰가 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        displayReviews(reviews);
                    }
                } else {
                    Toast.makeText(RestaurantReviewList.this, "리뷰 목록을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReviewResponseDTO>> call, Throwable t) {
                Log.e("RestaurantReviewList", "Failed to fetch reviews: " + t.getMessage());
                Toast.makeText(RestaurantReviewList.this, "리뷰 목록을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 리뷰 표시 메서드 (리뷰 목록을 동적으로 생성)
    private void displayReviews(List<ReviewResponseDTO> reviews) {
        // 기존 리뷰 표시 로직
        for (ReviewResponseDTO review : reviews) {
            View reviewView = LayoutInflater.from(this).inflate(R.layout.review_item_list, reviewContainer, false);

            // 별점, 리뷰 내용 및 생성일시 설정
            setStarRating(reviewView, review.getReview_star());
            TextView reviewBody = reviewView.findViewById(R.id.review_body);
            TextView createdAt = reviewView.findViewById(R.id.created_at);

            reviewBody.setText(review.getReview_body());
            createdAt.setText(review.getCreatedAt().toString());

            reviewContainer.addView(reviewView);
        }
    }

    // 별점 설정 메서드
    private void setStarRating(View reviewView, int rating) {
        int[] starIds = {R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5};

        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                reviewView.findViewById(starIds[i]).setBackgroundResource(R.drawable.star_filled);
            } else {
                reviewView.findViewById(starIds[i]).setBackgroundResource(R.drawable.star_empty);
            }
        }
    }

    private Long getSubFromToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("jwt_token", null);

        if (token != null) {
            JWT jwt = new JWT(token);
            Claim subClaim = jwt.getClaim("sub");
            return subClaim.asLong();  // sub 값을 Long으로 변환하여 반환
        }

        return null;
    }

//     //Todo: 필요성을 못 느껴서 일단 임시로 두었음
//    // 특정 리뷰 조회 메서드 (리뷰 id로 가져오기)
//    private void fetchReviewById(Long reviewId) {
//        ReviewApiService service = ReviewRetrofitClient.getRetrofitInstance().create(ReviewApiService.class);
//        Call<ReviewResponseDTO> call = service.getReviewById(reviewId);
//
//        call.enqueue(new Callback<ReviewResponseDTO>() {
//            @Override
//            public void onResponse(Call<ReviewResponseDTO> call, Response<ReviewResponseDTO> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    ReviewResponseDTO review = response.body();
//                    // 리뷰 세부 정보를 표시하는 로직 추가
//                } else {
//                    Toast.makeText(RestaurantReviewList.this, "리뷰 세부 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ReviewResponseDTO> call, Throwable t) {
//                Log.e("RestaurantReviewList", "Failed to fetch review: " + t.getMessage());
//                Toast.makeText(RestaurantReviewList.this, "리뷰 세부 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}