package com.eatda.president;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.eatda.Login;
import com.eatda.R;
import com.eatda.president.Restaurant.PresidentManageRestaurantApiService;
import com.eatda.president.Restaurant.PresidentManageRestaurantRetrofitClient;
import com.eatda.president.Restaurant.form.RestaurantResponse;
import com.eatda.sponsor.SponsorJoin;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsMgmt extends AppCompatActivity {

    private LinearLayout restaurantContainer;
    private Long presidentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurants_mgmt);

        restaurantContainer = findViewById(R.id.restaurant_container);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presidentId = getSubFromToken();
        Log.d("sponsorID :", "사장ID" + presidentId);

        getRestaurant();

    }

    private void getRestaurant(){
        PresidentManageRestaurantApiService service = PresidentManageRestaurantRetrofitClient.getRetrofitInstance(this).create(PresidentManageRestaurantApiService.class);

        Call<List<RestaurantResponse>> call = service.getRestaurantByPresidentId(presidentId);
        call.enqueue(new Callback<List<RestaurantResponse>>() {
            @Override
            public void onResponse(Call<List<RestaurantResponse>> call, Response<List<RestaurantResponse>> response) {
                if(response.isSuccessful() && response.body() != null){
                    if(response.body().isEmpty()){
                        addShowAlertDialog("식당이 없습니다.","식당을 등록하시겠습니까?", true);
                    }else {
                        List<RestaurantResponse> restaurant = response.body();
                        Toast.makeText(RestaurantsMgmt.this, "내 식당을 불러옵니다.", Toast.LENGTH_SHORT).show();
                        displayRestaurant(restaurant);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RestaurantResponse>> call, Throwable t) {
                Toast.makeText(RestaurantsMgmt.this,"식당이 없습니다.2",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayRestaurant(List<RestaurantResponse> restaurant){
        for(RestaurantResponse response : restaurant){
            View restaurantView = LayoutInflater.from(this).inflate(R.layout.restaurants_item_list, restaurantContainer, false);
            Log.d("RestaurantID", "식당 ID: " + response.getId());

            TextView restaurantName = restaurantView.findViewById(R.id.restaurant_name);
            TextView restaurantAddress = restaurantView.findViewById(R.id.restaurant_address);
            TextView restaurantNumber = restaurantView.findViewById(R.id.restaurant_number);
            TextView restaurantBody = restaurantView.findViewById(R.id.restaurant_body);

            restaurantName.setText(response.getRestaurantName());
            restaurantAddress.setText(response.getRestaurantAddress());
            restaurantNumber.setText(response.getRestaurantNumber());
            restaurantBody.setText(response.getRestaurantBody());

            restaurantContainer.addView(restaurantView);
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

    private void addShowAlertDialog(String title, String message, boolean shouldStartIntent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantsMgmt.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> {
            dialog.dismiss();
            // Intent를 실행해야 하는 경우
            if (shouldStartIntent) {
                Intent intent = new Intent(RestaurantsMgmt.this, RestaurantAdd.class);
                startActivity(intent);
            }
        });
        builder.show();
    }
}