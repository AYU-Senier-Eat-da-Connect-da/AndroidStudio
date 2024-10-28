package com.eatda.ui.childManagement;

import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.eatda.R;
import com.eatda.data.api.childManagement.SponsorChildManagementApiService;
import com.eatda.data.api.childManagement.SponsorChildManagementRetrofitClient;
import com.eatda.data.form.childManagement.ChildResponse;
import com.eatda.data.form.sponsor.SponsorDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildList extends AppCompatActivity {

    private LinearLayout childContainer;  // 아동 정보를 동적으로 추가할 레이아웃 컨테이너
    private Long sponsorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_list);

        childContainer = findViewById(R.id.child_container);  // XML에서 LinearLayout 참조

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sponsorID = getSubFromToken();
        Log.d("sponsorID :", "sponsorID" + sponsorID);

        fetchChildrenList();
    }

    private void fetchChildrenList() {
        SponsorChildManagementApiService service = SponsorChildManagementRetrofitClient.getRetrofitInstance(this).create(SponsorChildManagementApiService.class);

        Call<List<ChildResponse>> call = service.getChildren(sponsorID);
        call.enqueue(new Callback<List<ChildResponse>>() {
            @Override
            public void onResponse(Call<List<ChildResponse>> call, Response<List<ChildResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChildResponse> children = response.body();

                    Toast.makeText(ChildList.this, "children found", Toast.LENGTH_SHORT).show();
                    displayChildren(children);
                } else {
                    Toast.makeText(ChildList.this, "No children found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChildResponse>> call, Throwable t) {
                Log.e("ChildList", "Failed to fetch children: " + t.getMessage());
                Toast.makeText(ChildList.this, "Failed to fetch children", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayChildren(List<ChildResponse> children) {
        for (ChildResponse child : children) {
            View childView = LayoutInflater.from(this).inflate(R.layout.child_item_list, childContainer, false);

            TextView childName = childView.findViewById(R.id.child_name);
            TextView childEmail = childView.findViewById(R.id.child_email);
            TextView childPhone = childView.findViewById(R.id.child_phone);
            TextView childAddress = childView.findViewById(R.id.child_address);
            Button registerButton = childView.findViewById(R.id.register_button);

            childName.setText(child.getName());
            childEmail.setText(child.getEmail());  // Email 설정
            childPhone.setText(child.getChildNumber());  // Phone 설정
            childAddress.setText(child.getChildAddress());  // Address 설정

            //등록하기 버튼 리스터 추가
            registerButton.setOnClickListener(v -> {
                // 등록 API 호출
                SponsorChildManagementApiService service = SponsorChildManagementRetrofitClient.getRetrofitInstance(this).create(SponsorChildManagementApiService.class);
                Call<SponsorDTO> call = service.addChildToSponsor(sponsorID, child.getId());  // API 호출

                call.enqueue(new Callback<SponsorDTO>() {
                    @Override
                    public void onResponse(Call<SponsorDTO> call, Response<SponsorDTO> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(ChildList.this, ChildMgmt.class);
                            Toast.makeText(ChildList.this, child.getName() + "가 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("ChildList", "등록된 아동: " + child.getName());
                            startActivity(intent);
                        } else {
                            Toast.makeText(ChildList.this, "등록 실패: 서버 오류", Toast.LENGTH_SHORT).show();
                            Log.e("ChildList", "등록 실패: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<SponsorDTO> call, Throwable t) {
                        Toast.makeText(ChildList.this, "등록 실패: 네트워크 오류", Toast.LENGTH_SHORT).show();
                        Log.e("ChildList", "네트워크 오류: " + t.getMessage());
                    }
                });
            });

            childContainer.addView(childView);  // 동적으로 childView 추가
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
