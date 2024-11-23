package com.eatda.ui.sponsor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.eatda.MainActivity;
import com.eatda.R;
import com.eatda.data.api.childManagement.SponsorChildManagementApiService;
import com.eatda.data.api.childManagement.SponsorChildManagementRetrofitClient;
import com.eatda.data.api.login.LoginApiService;
import com.eatda.data.api.login.LogoutRetrofitClient;
import com.eatda.data.form.sponsor.SponsorDTO;
import com.eatda.ui.childManagement.ChildList;
import com.eatda.ui.childManagement.ChildMgmt;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SponsorMyPage extends AppCompatActivity {

    private Button btn_child_mgmt;
    private Button btn_child_list;
    private Button btn_support;
    private Button btn_sponsor_help;
    private Button btn_logout;
    private Long sponsorID;
    private String sponsorName;
    private String sponsorAddress;
    private String sponsorEmail;
    private String sponsorNumber;
    private int sponsorAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sponsor_my_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sponsorID = getSubFromToken();

        getSponsor(sponsorID);

        /*
        후원한 아동 관리
         */
        btn_child_mgmt = findViewById(R.id.btn_child_mgmt);
        btn_child_mgmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SponsorMyPage.this, ChildMgmt.class);
                startActivity(intent);
            }
        });

        /*
        후원할 아동 조회
         */
        btn_child_list = findViewById(R.id.btn_child_list);
        btn_child_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SponsorMyPage.this, ChildList.class);
                startActivity(intent);
            }
        });

        /*
        후원하기
         */
        btn_support = findViewById(R.id.btn_support);
        btn_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SponsorMyPage.this, Support.class);
                startActivity(intent);
            }
        });

        /*
        후원자 도움말
         */
        btn_sponsor_help = findViewById(R.id.btn_sponsor_help);
        btn_sponsor_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SponsorMyPage.this, SponsorHelp.class);
                startActivity(intent);
            }
        });

        /*
        로그아웃
         */
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String jwtToken = sharedPreferences.getString("jwt_token",null);

                if(jwtToken != null){
                    logout(jwtToken);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("jwt_token");
                    editor.apply();
                }
            }
        });
    }

    private void getSponsor(Long sponsorID) {
        SponsorChildManagementApiService service = SponsorChildManagementRetrofitClient.getRetrofitInstance(this).create(SponsorChildManagementApiService.class);
        Call<SponsorDTO> call = service.getSponsorInfo(sponsorID);

        call.enqueue(new Callback<SponsorDTO>() {
            @Override
            public void onResponse(Call<SponsorDTO> call, Response<SponsorDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SponsorDTO sponsorDTO = response.body();
                    sponsorName = sponsorDTO.getSponsorName();
                    sponsorAddress = sponsorDTO.getSponsorAddress();
                    sponsorNumber = sponsorDTO.getSponsorNumber();
                    sponsorEmail = sponsorDTO.getSponsorEmail();
                    sponsorAmount = sponsorDTO.getSponsorAmount();
                    displayInfo();
                } else {
                    Toast.makeText(getApplicationContext(), "정보 조회 불가", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SponsorDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayInfo() {
        TextView nameView = findViewById(R.id.tv_sponsor_name_value);
        TextView addressView = findViewById(R.id.tv_sponsor_address_value);
        TextView emailView = findViewById(R.id.tv_sponsor_email_value);
        TextView phoneView = findViewById(R.id.tv_sponsor_phone_value);
        TextView donationView = findViewById(R.id.tv_sponsor_total_donation_value);

        nameView.setText(sponsorName != null ? sponsorName : "정보 없음");
        addressView.setText(sponsorAddress != null ? sponsorAddress : "정보 없음");
        emailView.setText(sponsorEmail != null ? sponsorEmail : "정보 없음");
        phoneView.setText(sponsorNumber != null ? sponsorNumber : "정보 없음");

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedPrice = numberFormat.format(sponsorAmount);
        donationView.setText(formattedPrice);
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

    private void logout(String jwtToken){
        LoginApiService service = LogoutRetrofitClient.getRetrofitInstance().create(LoginApiService.class);

        String authorizationHeader = "Bearer " + jwtToken;

        Call<Void> call = service.logout(authorizationHeader);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(SponsorMyPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Log.d("Logout", "로그아웃 성공");
                }else{
                    Log.d("Logout", "로그아웃 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Logout", "네트워크 오류 실패: ");
            }
        });
    }
}