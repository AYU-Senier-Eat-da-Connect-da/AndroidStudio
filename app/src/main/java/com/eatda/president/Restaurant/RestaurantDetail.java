package com.eatda.president.Restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import com.eatda.R;
import com.eatda.ReviewContext;
import com.eatda.president.FragmentAdapter;
import com.eatda.president.Menu.MenuContext;
import com.eatda.president.PresidentRetrofitClient;
import com.eatda.president.Restaurant.form.RestaurantDetailResponse;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetail extends AppCompatActivity {

    private LinearLayout restaurant_container;
    private LinearLayout menu_container;
    private int sum = 0;

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
        TextView currentSum = findViewById(R.id.current_sum);

        setRestaurantDetail(restaurantId);
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

        // restaurant_container에 식당 정보를 담은 View 추가
        restaurant_container.addView(restaurantView);

        // 메뉴 리스트가 없을 때 "메뉴가 없습니다" 메시지를 추가
        /*
        if (restaurant.getMenus() == null || restaurant.getMenus().isEmpty()) {
            TextView emptyMenuMessage = new TextView(this);
            emptyMenuMessage.setText("메뉴가 없습니다.");
            emptyMenuMessage.setTextSize(16);
            emptyMenuMessage.setTextColor(getResources().getColor(android.R.color.darker_gray));
            emptyMenuMessage.setPadding(16, 16, 16, 16);

            menu_container.addView(emptyMenuMessage);
        } else {
            // 메뉴 리스트 가져와서 menu_container에 추가
            for (MenuResponse menu : restaurant.getMenus()) {
                View menuView = getLayoutInflater().inflate(R.layout.restaurant_item_detail_menu, menu_container, false);

                // menuView에서 CardView와 CheckBox 참조
                CardView cardView = menuView.findViewById(R.id.menu_card_view);
                CheckBox checkBox = menuView.findViewById(R.id.menu_check_box);

                TextView menuName = menuView.findViewById(R.id.menu_name);
                TextView menuBody = menuView.findViewById(R.id.menu_body);
                TextView menuPrice = menuView.findViewById(R.id.menu_price);
                TextView menuStatus = menuView.findViewById(R.id.menu_status);

                // Menu 정보를 설정
                menuName.setText(menu.getMenuName());
                menuBody.setText(menu.getMenuBody());
                menuPrice.setText(String.format("%,d 원", menu.getPrice())); // 가격 형식 맞추기
                menuStatus.setText(menu.getMenuStatus() ? "주문 가능" : "품절");

                cardView.setOnClickListener(v -> {
                    checkBox.setChecked(!checkBox.isChecked()); // 현재 상태 반전
                    if (checkBox.isChecked()) {
                        cardView.setCardBackgroundColor(getResources().getColor(R.color.baseColor)); // 선택된 색상
                        sum += menu.getPrice();
                        currentSum.setText(String.format("%,d 원", sum));
                    } else {
                        cardView.setCardBackgroundColor(getResources().getColor(R.color.white)); // 기본 색상
                        sum -= menu.getPrice();
                        currentSum.setText(String.format("%,d 원", sum));
                    }
                });


                // menu_container에 메뉴 정보를 담은 View 추가
                menu_container.addView(menuView);
            }

         */
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        MenuContext menuFragment = new MenuContext();
        Bundle bundle = new Bundle();
        bundle.putSerializable("menus", (Serializable) restaurant.getMenus()); // 메뉴 리스트 전달
        menuFragment.setArguments(bundle);
        adapter.addFragment(menuFragment, "Menu");

        ReviewContext reviewFragment = new ReviewContext();
        adapter.addFragment(reviewFragment, "Reviews");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

}