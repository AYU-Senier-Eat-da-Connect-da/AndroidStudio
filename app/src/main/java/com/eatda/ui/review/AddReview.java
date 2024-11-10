package com.eatda.ui.review;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.eatda.data.form.review.ReviewRequestDTO;
import com.eatda.data.form.review.ReviewResponseDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReview extends AppCompatActivity {

    private EditText reviewContentEditText;
    private Button submitReviewButton;
    private Long restaurantId;
    private Long childId;
    private int selectedStars = 0; // 선택된 별점
    private LinearLayout starRatingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_review);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        reviewContentEditText = findViewById(R.id.review_content);
        submitReviewButton = findViewById(R.id.btn_submit_review);
        starRatingLayout = findViewById(R.id.star_rating);

        // Intent에서 restaurantId와 childId를 받아옴
        restaurantId = getIntent().getLongExtra("restaurantId", -1);
        childId = getSubFromToken();

        // 별점 터치 리스너 설정
        setupStarRatingClickListener();

        // 리뷰 제출 버튼 클릭 리스너 설정
        submitReviewButton.setOnClickListener(v -> {
            String content = reviewContentEditText.getText().toString();

            if (restaurantId != -1 && childId != -1 && selectedStars > 0) {
                // 리뷰 요청 DTO 생성
                ReviewRequestDTO reviewRequestDTO = new ReviewRequestDTO(content, selectedStars);
                // 리뷰 제출 메소드 호출
                submitReview(restaurantId, reviewRequestDTO, childId);
            } else {
                Toast.makeText(AddReview.this, "주문 정보가 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 초기 별점 설정
        resetStarRating(); // 모든 별을 빈 별로 초기화
    }

    // 별점 클릭 리스너 설정
    private void setupStarRatingClickListener() {
        for (int i = 0; i < starRatingLayout.getChildCount(); i++) {
            final int starIndex = i; // 현재 인덱스를 캡처하여 사용
            ImageView starImageView = (ImageView) starRatingLayout.getChildAt(i);
            starImageView.setOnClickListener(v -> selectStars(starIndex + 1));
        }
    }

    // 선택된 별점 설정
    private void selectStars(int stars) {
        selectedStars = stars; // 선택된 별점 저장

        for (int i = 0; i < starRatingLayout.getChildCount(); i++) {
            ImageView starImageView = (ImageView) starRatingLayout.getChildAt(i);
            if (i < selectedStars) {
                starImageView.setImageResource(R.drawable.star_filled); // 채워진 별
            } else {
                starImageView.setImageResource(R.drawable.star_empty); // 빈 별
            }
        }
    }

    // 초기 별점 설정
    private void resetStarRating() {
        selectedStars = 0; // 초기 별점 0
        for (int i = 0; i < starRatingLayout.getChildCount(); i++) {
            ImageView starImageView = (ImageView) starRatingLayout.getChildAt(i);
            starImageView.setImageResource(R.drawable.star_empty); // 모든 별을 빈 별로 초기화
        }
    }

    // 리뷰 제출 메소드
    private void submitReview(Long restaurantId, ReviewRequestDTO reviewRequestDTO, Long childId) {
        ReviewApiService service = ReviewRetrofitClient.getRetrofitInstance(this).create(ReviewApiService.class);

        Call<ReviewResponseDTO> call = service.addReview(restaurantId, reviewRequestDTO, childId);
        Toast.makeText(AddReview.this, "내용 :" + reviewRequestDTO.getReviewBody() + reviewRequestDTO.getReviewStar(), Toast.LENGTH_SHORT).show();
        call.enqueue(new Callback<ReviewResponseDTO>() {
            @Override
            public void onResponse(Call<ReviewResponseDTO> call, Response<ReviewResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AddReview.this, "리뷰가 성공적으로 작성되었습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("AddReviewActivity", "리뷰 작성 완료: " + response.body().toString());
                    finish(); // 리뷰 작성 완료 후 화면 종료
                } else {
                    Toast.makeText(AddReview.this, "리뷰 작성 실패: 서버 오류", Toast.LENGTH_SHORT).show();
                    Log.e("AddReviewActivity", "리뷰 작성 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ReviewResponseDTO> call, Throwable t) {
                Toast.makeText(AddReview.this, "리뷰 작성 실패: 네트워크 오류", Toast.LENGTH_SHORT).show();
                Log.e("AddReviewActivity", "네트워크 오류: " + t.getMessage());
            }
        });
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
