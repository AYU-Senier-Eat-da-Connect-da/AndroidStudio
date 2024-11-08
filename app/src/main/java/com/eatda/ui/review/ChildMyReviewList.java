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

public class ChildMyReviewList extends AppCompatActivity {

    private LinearLayout reviewContainer;
    private Long childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_my_review_list);

        childId = getSubFromToken();

        reviewContainer = findViewById(R.id.review_container);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fetchChildReviews(childId);
    }

    /*
        아동ID로 리뷰 리스트 조회
     */
    private void fetchChildReviews(Long childId) {
        ReviewApiService service = ReviewRetrofitClient.getRetrofitInstance(this).create(ReviewApiService.class);
        Call<List<ReviewResponseDTO>> call = service.getReviewListByChildId(childId);

        call.enqueue(new Callback<List<ReviewResponseDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewResponseDTO>> call, Response<List<ReviewResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReviewResponseDTO> reviews = response.body();
                    if (reviews.isEmpty()) {
                        Toast.makeText(ChildMyReviewList.this, "작성한 리뷰가 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        displayReviews(reviews);
                    }
                } else {
                    Toast.makeText(ChildMyReviewList.this, "리뷰 목록을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReviewResponseDTO>> call, Throwable t) {
                Log.e("ChildMyReviewList", "Failed to fetch reviews: " + t.getMessage());
                Toast.makeText(ChildMyReviewList.this, "리뷰 목록을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
        서버에서 받은 리뷰 목록을 화면에 동적으로 표시
     */
    private void displayReviews(List<ReviewResponseDTO> reviews) {
        for (ReviewResponseDTO review : reviews) {
            View reviewView = LayoutInflater.from(this).inflate(R.layout.review_item_list, reviewContainer, false);

            setStarRating(reviewView, review.getReview_star());
            TextView reviewBody = reviewView.findViewById(R.id.review_body);
            TextView createdAt = reviewView.findViewById(R.id.created_at);

            reviewBody.setText(review.getReview_body());
            createdAt.setText(review.getCreatedAt().toString());

            reviewContainer.addView(reviewView);  // 동적으로 reviewView 추가
        }
    }

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
}