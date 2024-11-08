package com.eatda.ui.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.eatda.R;
import com.eatda.data.api.restaurant.PresidentManageRestaurantApiService;
import com.eatda.data.api.review.ReviewApiService;
import com.eatda.data.api.review.ReviewRetrofitClient;
import com.eatda.data.form.review.ReviewResponseDTO;
import com.eatda.ui.order.Order;
import com.eatda.ui.president.FragmentAdapter;
import com.eatda.ui.menu.MenuContext;
import com.eatda.data.api.president.PresidentRetrofitClient;
import com.eatda.data.form.restaurant.RestaurantDetailResponse;
import com.eatda.ui.review.RestaurantReviewList;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetail extends AppCompatActivity {

    private LinearLayout restaurant_container;
    private LinearLayout bottom_Navigation;
    private int sum = 0;
    private Long presidentId;
    private MenuContext menuFragment;
    private String intent_restaurantName;
    private String intent_restaurantAddress;
    private String intent_restaurantNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        long restaurantId = intent.getLongExtra("restaurantId", -1);
        Toast.makeText(RestaurantDetail.this, "식당 아이디 : " + restaurantId, Toast.LENGTH_SHORT).show();

        restaurant_container = findViewById(R.id.restaurant_container);
        bottom_Navigation = findViewById(R.id.bottom_navigation);

        setRestaurantDetail(restaurantId);
        fetchRestaurantReviews(restaurantId);

        bottom_Navigation.setOnClickListener(v -> {
            Intent orderIntent = new Intent(RestaurantDetail.this, Order.class);
            orderIntent.putExtra("restaurantId", restaurantId);
            orderIntent.putExtra("restaurantName", intent_restaurantName);
            orderIntent.putExtra("restaurantAddress", intent_restaurantAddress);
            orderIntent.putExtra("restaurantNumber", intent_restaurantNumber);
            orderIntent.putExtra("sum", menuFragment.getSum());
            orderIntent.putParcelableArrayListExtra("selectedMenus", new ArrayList<>(menuFragment.getSelectedMenus()));
            startActivity(orderIntent);
        });
    }

    private void setRestaurantDetail(long restaurantDetail) {
        PresidentManageRestaurantApiService service = PresidentRetrofitClient.getRetrofitInstance(this).create(PresidentManageRestaurantApiService.class);
        Call<RestaurantDetailResponse> call = service.getRestaurantDetail(restaurantDetail);
        call.enqueue(new Callback<RestaurantDetailResponse>() {
            @Override
            public void onResponse(Call<RestaurantDetailResponse> call, Response<RestaurantDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayRestaurantDetail(response);
                }
            }

            @Override
            public void onFailure(Call<RestaurantDetailResponse> call, Throwable t) {
                Toast.makeText(RestaurantDetail.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRestaurantReviews(long restaurantId) {
        ReviewApiService service = ReviewRetrofitClient.getRetrofitInstance(this).create(ReviewApiService.class);
        Call<List<ReviewResponseDTO>> call = service.getReviewsByRestaurantId(restaurantId);

        call.enqueue(new Callback<List<ReviewResponseDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewResponseDTO>> call, Response<List<ReviewResponseDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReviewResponseDTO> reviews = response.body();
                    if (reviews.isEmpty()) {
                        Toast.makeText(RestaurantDetail.this, "작성된 리뷰가 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        displayReviews(reviews);
                    }
                } else {
                    Toast.makeText(RestaurantDetail.this, "리뷰 목록을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReviewResponseDTO>> call, Throwable t) {
                Toast.makeText(RestaurantDetail.this, "리뷰 목록을 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayRestaurantDetail(Response<RestaurantDetailResponse> response) {

        RestaurantDetailResponse restaurants = response.body();

        if (restaurants == null) return;

        View restaurantView = getLayoutInflater().inflate(R.layout.restaurant_item_detail_restaurant, restaurant_container, false);
        presidentId = restaurants.getPresidentId();
        TextView restaurantName = restaurantView.findViewById(R.id.restaurant_name);
        TextView restaurantAddress = restaurantView.findViewById(R.id.restaurant_address);
        TextView restaurantNumber = restaurantView.findViewById(R.id.restaurant_number);
        TextView restaurantBody = restaurantView.findViewById(R.id.restaurant_body);
        TextView restaurantCategory = restaurantView.findViewById(R.id.restaurant_category);

        restaurantName.setText(restaurants.getRestaurantName());
        restaurantAddress.setText(restaurants.getRestaurantAddress());
        restaurantNumber.setText(restaurants.getRestaurantNumber());
        restaurantBody.setText(restaurants.getRestaurantBody());
        restaurantCategory.setText(restaurants.getRestaurantCategory());

        intent_restaurantName = restaurantName.getText().toString();
        intent_restaurantAddress = restaurantAddress.getText().toString();
        intent_restaurantNumber = restaurantNumber.getText().toString();

        restaurant_container.addView(restaurantView);
        getPhoto();

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        // 메뉴 프래그먼트 추가
        menuFragment = new MenuContext();
        Bundle menuBundle = new Bundle();
        menuBundle.putSerializable("menus", (Serializable) restaurants.getMenus());
        menuFragment.setArguments(menuBundle);
        adapter.addFragment(menuFragment, "Menu");

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // 뷰페이저 어댑터 갱신
        adapter.notifyDataSetChanged();
    }

    private void displayReviews(List<ReviewResponseDTO> reviews) {
        LinearLayout reviewContainer = findViewById(R.id.review_container);

        for (ReviewResponseDTO review : reviews) {
            View reviewView = LayoutInflater.from(this).inflate(R.layout.restaurant_item_detail_review, reviewContainer, false);

            // 별점, 리뷰 내용 및 생성일시 설정
            setStarRating(reviewView, review.getReview_star());
            TextView reviewBody = reviewView.findViewById(R.id.review_body);
            TextView createdAt = reviewView.findViewById(R.id.created_at);

            reviewBody.setText(review.getReview_body());
            createdAt.setText(review.getCreatedAt().toString());

            reviewContainer.addView(reviewView);
        }
    }


    private void getPhoto() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("restaurant/restaurant_" + presidentId + ".jpg");

        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            ImageView imageView = findViewById(R.id.imageView3);
            Glide.with(getApplicationContext())
                    .load(uri)
                    .into(imageView);
        }).addOnFailureListener(e -> {
            Toast.makeText(RestaurantDetail.this, "이미지 로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Firebase Storage", "이미지 로드 오류: " + e.getMessage());
        });
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
}

