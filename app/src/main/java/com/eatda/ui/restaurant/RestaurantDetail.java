package com.eatda.ui.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.eatda.ReviewContext;
import com.eatda.data.api.restaurant.PresidentManageRestaurantApiService;
import com.eatda.data.form.menu.MenuResponse;
import com.eatda.ui.order.Order;
import com.eatda.ui.president.FragmentAdapter;
import com.eatda.ui.menu.MenuContext;
import com.eatda.data.api.president.PresidentRetrofitClient;
import com.eatda.data.form.restaurant.RestaurantDetailResponse;
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
    private LinearLayout menu_container;
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

        // Intent로부터 전달된 데이터 가져오기
        Intent intent = getIntent();
        long restaurantId = intent.getLongExtra("restaurantId",-1);
        Toast.makeText(RestaurantDetail.this, "식당 아이디 : " + restaurantId, Toast.LENGTH_SHORT).show();

        restaurant_container = findViewById(R.id.restaurant_container);
        //menu_container = findViewById(R.id.menu_container);
        bottom_Navigation = findViewById(R.id.bottom_navigation);
        TextView currentSum = findViewById(R.id.current_sum);

        setRestaurantDetail(restaurantId);

        bottom_Navigation.setOnClickListener(v->{
            Intent orderIntent = new Intent(RestaurantDetail.this, Order.class );

            orderIntent.putExtra("restaurantId", restaurantId);
            orderIntent.putExtra("restaurantName", intent_restaurantName);
            orderIntent.putExtra("restaurantAddress", intent_restaurantAddress);
            orderIntent.putExtra("restaurantNumber", intent_restaurantNumber);
            orderIntent.putExtra("sum", menuFragment.getSum());
            orderIntent.putParcelableArrayListExtra("selectedMenus", new ArrayList<>(menuFragment.getSelectedMenus())); // 선택된 메뉴 데이터 전달
            startActivity(orderIntent);
        });

    }

    private void setRestaurantDetail(long restaurantDetail) {
        PresidentManageRestaurantApiService service = PresidentRetrofitClient.getRetrofitInstance(this).create(PresidentManageRestaurantApiService.class);

        Call<RestaurantDetailResponse> call = service.getRestaurantDetail(restaurantDetail);
        call.enqueue(new Callback<RestaurantDetailResponse>() {
            @Override
            public void onResponse(Call<RestaurantDetailResponse> call, Response<RestaurantDetailResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    displayRestaurantDetail(response);
                }
            }

            @Override
            public void onFailure(Call<RestaurantDetailResponse> call, Throwable t) {
                Toast.makeText(RestaurantDetail.this,"네트워크 오류",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayRestaurantDetail(Response<RestaurantDetailResponse> response) {
        // Restaurant 정보 가져오기
        RestaurantDetailResponse restaurant = response.body();

        if (restaurant == null) return;

        // XML 레이아웃을 불러와서 restaurant_container에 추가
        View restaurantView = getLayoutInflater().inflate(R.layout.restaurant_item_detail_restaurant, restaurant_container, false);
        presidentId = restaurant.getPresidentId();
        TextView restaurantName = restaurantView.findViewById(R.id.restaurant_name);
        TextView restaurantAddress = restaurantView.findViewById(R.id.restaurant_address);
        TextView restaurantNumber = restaurantView.findViewById(R.id.restaurant_number);
        TextView restaurantBody = restaurantView.findViewById(R.id.restaurant_body);
        TextView restaurantCategory = restaurantView.findViewById(R.id.restaurant_category);

        // Restaurant 정보를 설정
        restaurantName.setText(restaurant.getRestaurantName());
        restaurantAddress.setText(restaurant.getRestaurantAddress());
        restaurantNumber.setText(restaurant.getRestaurantNumber());
        restaurantBody.setText(restaurant.getRestaurantBody());
        restaurantCategory.setText(restaurant.getRestaurantCategory());

        //인텐트용 값 가져오기
        intent_restaurantName = restaurantName.getText().toString();
        intent_restaurantAddress = restaurantAddress.getText().toString();
        intent_restaurantNumber = restaurantNumber.getText().toString();

        restaurant_container.addView(restaurantView);
        getPhoto();

        // 탭 레이아웃 세팅

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        menuFragment = new MenuContext();
        Bundle bundle = new Bundle();
        bundle.putSerializable("menus", (Serializable) restaurant.getMenus()); // 메뉴 리스트 전달
        menuFragment.setArguments(bundle);
        adapter.addFragment(menuFragment, "Menu");

        ReviewContext reviewFragment = new ReviewContext();
        adapter.addFragment(reviewFragment, "Reviews");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void getPhoto() {
        // Firebase Storage 참조 가져오기
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // 이미지 경로 설정
        StorageReference imageRef = storageRef.child("restaurant/restaurant_" + presidentId + ".jpg");

        // 이미지 다운로드 URL 가져오기
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            ImageView imageView = findViewById(R.id.imageView3);  // ImageView 설정
            Glide.with(getApplicationContext())
                    .load(uri)  // Glide로 다운로드 URL 로드
                    .into(imageView);
        }).addOnFailureListener(e -> {
            Toast.makeText(RestaurantDetail.this, "이미지 로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Firebase Storage", "이미지 로드 오류: " + e.getMessage());
        });
    }

}