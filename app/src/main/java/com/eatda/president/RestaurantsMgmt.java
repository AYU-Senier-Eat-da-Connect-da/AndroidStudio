package com.eatda.president;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.eatda.R;
import com.eatda.president.Menu.PresidentManageMenuApiService;
import com.eatda.president.Menu.form.MenuResponse;
import com.eatda.president.Restaurant.PresidentManageRestaurantApiService;
import com.eatda.president.Restaurant.form.RestaurantResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsMgmt extends AppCompatActivity {

    private LinearLayout restaurantContainer;
    private LinearLayout menuContainer;
    private Long presidentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurants_mgmt);

        restaurantContainer = findViewById(R.id.restaurant_container);
        menuContainer = findViewById(R.id.menu_container);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presidentId = getSubFromToken();
        Log.d("sponsorID :", "사장ID" + presidentId);


        // 메뉴 추가
        Button btn_addMenu = findViewById(R.id.btn_addMenu);
        btn_addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantsMgmt.this, MenuAdd.class);
                startActivity(intent);
            }
        });

        getRestaurant();
        getMenu();
    }


    private void getRestaurant(){
        PresidentManageRestaurantApiService service = PresidentRetrofitClient.getRetrofitInstance(this).create(PresidentManageRestaurantApiService.class);

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
                Toast.makeText(RestaurantsMgmt.this,"네트워크 오류",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMenu(){
        PresidentManageMenuApiService service = PresidentRetrofitClient.getRetrofitInstance(this).create(PresidentManageMenuApiService.class);

        Call<List<MenuResponse>> call = service.getMenuByPresidentId(presidentId);
        call.enqueue(new Callback<List<MenuResponse>>() {
            @Override
            public void onResponse(Call<List<MenuResponse>> call, Response<List<MenuResponse>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<MenuResponse> menu = response.body();
                    Toast.makeText(RestaurantsMgmt.this, "내 메뉴를 불러옵니다.", Toast.LENGTH_SHORT).show();
                    displayMenu(menu);
                }else{
                    Toast.makeText(RestaurantsMgmt.this, "내 메뉴가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MenuResponse>> call, Throwable t) {
                Toast.makeText(RestaurantsMgmt.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayRestaurant(List<RestaurantResponse> restaurant){
        for(RestaurantResponse response : restaurant){
            View restaurantView = LayoutInflater.from(this).inflate(R.layout.restaurants_item_list, restaurantContainer, false);
            Log.d("RestaurantID", "식당 ID: " + response.getId());

            Button btn_restaurantDelete = restaurantView.findViewById(R.id.delete_button);
            Button btn_restaurantModify = restaurantView.findViewById(R.id.modify_button);
            TextView restaurantName = restaurantView.findViewById(R.id.restaurant_name);
            TextView restaurantAddress = restaurantView.findViewById(R.id.restaurant_address);
            TextView restaurantNumber = restaurantView.findViewById(R.id.restaurant_number);
            TextView restaurantBody = restaurantView.findViewById(R.id.restaurant_body);
            TextView restaurantCategory = restaurantView.findViewById(R.id.restaurant_category);

            restaurantName.setText(response.getRestaurantName());
            restaurantAddress.setText(response.getRestaurantAddress());
            restaurantNumber.setText(response.getRestaurantNumber());
            restaurantBody.setText(response.getRestaurantBody());
            restaurantCategory.setText(response.getRestaurantCategory());

            btn_restaurantModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RestaurantsMgmt.this, RestaurantModify.class);

                    intent.putExtra("restaurantId", response.getId());
                    intent.putExtra("restaurantName", response.getRestaurantName());
                    intent.putExtra("restaurantAddress", response.getRestaurantAddress());
                    intent.putExtra("restaurantNumber",response.getRestaurantNumber());
                    intent.putExtra("restaurantBody", response.getRestaurantBody());
                    intent.putExtra("restaurantCategory", response.getRestaurantCategory());

                    startActivity(intent);
                }
            });

            btn_restaurantDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("식당 삭제")
                            .setMessage("삭제하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PresidentManageRestaurantApiService service = PresidentRetrofitClient.getRetrofitInstance(v.getContext()).create(PresidentManageRestaurantApiService.class);

                                    Call<String> call = service.deleteRestaurant(response.getId());
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if(response.isSuccessful() && response.body() != null){
                                                menuContainer.removeView(restaurantView);
                                                Toast.makeText(RestaurantsMgmt.this, "식당 삭제 완료", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(RestaurantsMgmt.this, PresidentMyPage.class);
                                                startActivity(intent);
                                            }else{
                                                Toast.makeText(RestaurantsMgmt.this, "식당 삭제 실패", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Toast.makeText(RestaurantsMgmt.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("아니오", null)
                            .show();
                }
            });

            restaurantContainer.addView(restaurantView);
        }

    }

    private void displayMenu(List<MenuResponse> menu){
        for(MenuResponse response : menu){
            View menuView = LayoutInflater.from(this).inflate(R.layout.menu_item_mgmt, menuContainer, false);

            TextView menuName = menuView.findViewById(R.id.menu_name);
            TextView menuBody = menuView.findViewById(R.id.menu_body);
            TextView menuStatus = menuView.findViewById(R.id.menu_status);
            TextView menuPrice = menuView.findViewById(R.id.menu_price);
            Button btn_menuModify = menuView.findViewById(R.id.modify_button);
            Button btn_menuDelete = menuView.findViewById(R.id.delete_button);

            menuName.setText(response.getMenuName());
            menuBody.setText(response.getMenuBody());
            if(response.getMenuStatus()){
                menuStatus.setText("주문 가능");
            }else{
                menuStatus.setText("품절");
            }
            menuPrice.setText(String.valueOf(response.getPrice()));

            Long menuId = response.getId();

            /*
            메뉴 수정
             */
            btn_menuModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RestaurantsMgmt.this, MenuModify.class);

                    intent.putExtra("menuId",menuId);
                    intent.putExtra("menuName", response.getMenuName());
                    intent.putExtra("menuBody", response.getMenuBody());
                    intent.putExtra("menuStatus", response.getMenuStatus());
                    intent.putExtra("menuPrice", response.getPrice());

                    startActivity(intent);
                }
            });

            /*
            메뉴 삭제
             */
            btn_menuDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("메뉴 삭제")
                            .setMessage("삭제하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PresidentManageMenuApiService service = PresidentRetrofitClient.getRetrofitInstance(v.getContext()).create(PresidentManageMenuApiService.class);

                                    Call<String> call = service.deleteMenu(menuId);
                                    Log.d("MenuID", "메뉴 ID: " + menuId);
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if(response.isSuccessful() && response.body() != null){
                                                menuContainer.removeView(menuView);
                                                Toast.makeText(RestaurantsMgmt.this, "메뉴 삭제 완료", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(RestaurantsMgmt.this, "메뉴 삭제 실패", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Toast.makeText(RestaurantsMgmt.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("아니오", null)
                            .show();
                }
            });

            menuContainer.addView(menuView);
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