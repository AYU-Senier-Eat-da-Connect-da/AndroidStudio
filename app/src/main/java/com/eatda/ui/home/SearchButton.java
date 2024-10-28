package com.eatda.ui.home;

import android.content.Intent;
import android.os.Bundle;
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

import com.eatda.R;
import com.eatda.data.api.home.HomeApiService;
import com.eatda.data.api.home.HomeRetrofitClient;
import com.eatda.ui.restaurant.RestaurantDetail;
import com.eatda.data.form.restaurant.RestaurantResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchButton extends AppCompatActivity {

    private LinearLayout searchContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_button);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchContainer = findViewById(R.id.search_container);
        Intent intent = getIntent();
        String buttonText = intent.getStringExtra("buttonText");

        // 받은 buttonText 값을 사용하여 원하는 작업 수행
        if (buttonText != null) {
            TextView textView = findViewById(R.id.resultTextView);
            textView.setText(buttonText);
        }

        getSearchResult(buttonText);
    }

    private void getSearchResult(String searchText) {
        HomeApiService service = HomeRetrofitClient.getRetrofitInstance(this).create(HomeApiService.class);

        Call<List<RestaurantResponse>> call = service.getRestaurantByCategory(searchText);
        call.enqueue(new Callback<List<RestaurantResponse>>() {
            @Override
            public void onResponse(Call<List<RestaurantResponse>> call, Response<List<RestaurantResponse>> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isEmpty()){
                        Toast.makeText(SearchButton.this, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        List<RestaurantResponse> restaurant = response.body();
                        displayRestaurant(restaurant);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RestaurantResponse>> call, Throwable t) {
                Toast.makeText(SearchButton.this, "네트워크 에러.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayRestaurant(List<RestaurantResponse> restaurant) {
        for(RestaurantResponse response : restaurant){
            View restaurantView = LayoutInflater.from(this).inflate(R.layout.search_item_text, searchContainer, false);

            TextView restaurantName = restaurantView.findViewById(R.id.restaurant_name);
            TextView restaurantAddress = restaurantView.findViewById(R.id.restaurant_address);
            TextView restaurantNumber = restaurantView.findViewById(R.id.restaurant_number);

            restaurantName.setText(response.getRestaurantName());
            restaurantAddress.setText(response.getRestaurantAddress());
            restaurantNumber.setText(response.getRestaurantNumber());

            restaurantView.setOnClickListener(v ->{
                Intent intent = new Intent(SearchButton.this, RestaurantDetail.class);
                intent.putExtra("restaurantId",response.getId());
                startActivity(intent);
            });

            searchContainer.addView(restaurantView);
        }
    }
}