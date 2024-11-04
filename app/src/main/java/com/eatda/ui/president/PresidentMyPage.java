package com.eatda.ui.president;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.eatda.data.api.childManagement.SponsorChildManagementApiService;
import com.eatda.data.api.childManagement.SponsorChildManagementRetrofitClient;
import com.eatda.data.api.president.PresidentApiService;
import com.eatda.data.api.president.PresidentRetrofitClient;
import com.eatda.data.form.president.PresidentDTO;
import com.eatda.data.form.sponsor.SponsorDTO;
import com.eatda.ui.menu.MenuMgmt;
import com.eatda.ui.restaurant.RestaurantsMgmt;
import com.eatda.ui.review.RestaurantReviewList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresidentMyPage extends AppCompatActivity {

    private Button btn_restaurants_mgmt;
    private Button btn_menu_mgmt;
    private Button btn_president_help;
    private Long presidentId;
    private String presidentName;
    private String presidentEmail;
    private String presidentNumber;
    private int businessNumber;


    //Todo: 임시위치 (가게 정보에서 Reviews에 위치해야함)
    private Button btn_restaurant_review_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_president_my_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presidentId = getSubFromToken();
        getPresident(presidentId);

        /*
        가게 관리
         */
        btn_restaurants_mgmt = findViewById(R.id.btn_restaurants_mgmt);
        btn_restaurants_mgmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PresidentMyPage.this, RestaurantsMgmt.class);
                startActivity(intent);
            }
        });

        /*
        메뉴 관리
         */
        btn_menu_mgmt = findViewById(R.id.btn_menu_mgmt);
        btn_menu_mgmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PresidentMyPage.this, MenuMgmt.class);
                startActivity(intent);
            }
        });

        /*
        도움말
         */
        btn_president_help = findViewById(R.id.btn_president_help);
        btn_president_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PresidentMyPage.this, PresidentHelp.class);
                startActivity(intent);
            }
        });

        /*
         내가게 리뷰 리스트 조회
         */
        btn_restaurant_review_list = findViewById(R.id.btn_restaurant_review_list);
        btn_restaurant_review_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PresidentMyPage.this, RestaurantReviewList.class);
                startActivity(intent);
            }
        });


    }

    private void getPresident(Long presidentId) {
        PresidentApiService service = PresidentRetrofitClient.getRetrofitInstance(this).create(PresidentApiService.class);
        Call<PresidentDTO> call = service.getPresidentInfo(presidentId);

        call.enqueue(new Callback<PresidentDTO>() {
            @Override
            public void onResponse(Call<PresidentDTO> call, Response<PresidentDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PresidentDTO president = response.body();
                    presidentEmail = president.getPresidentEmail();
                    presidentName = president.getPresidentName();
                    presidentNumber = president.getPresidentNumber();
                    businessNumber = president.getBusinessNumber();
                    displayInfo();
                } else {
                    Toast.makeText(getApplicationContext(), "정보 조회 불가", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PresidentDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayInfo(){
        TextView nameView = findViewById(R.id.tv_president_name_value);
        TextView emailView = findViewById(R.id.tv_president_email_value);
        TextView phoneView = findViewById(R.id.tv_president_phone_value);
        TextView businessView = findViewById(R.id.tv_president_business_number_value);

        nameView.setText(presidentName != null ? presidentName : "정보 없음");
        phoneView.setText(presidentNumber != null ? presidentNumber : "정보 없음");
        emailView.setText(presidentEmail != null ? presidentEmail : "정보 없음");

        if (businessNumber > 0) {  // businessNumber가 유효한 경우에만 포맷 적용
            String businessNumberString = String.format("%010d", businessNumber);
            String formattedBusinessNumber = businessNumberString.replaceFirst("(\\d{3})(\\d{2})(\\d{5})", "$1-$2-$3");
            businessView.setText(formattedBusinessNumber);
        } else {
            businessView.setText("정보 없음");
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