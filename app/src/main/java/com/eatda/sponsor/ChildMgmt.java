package com.eatda.sponsor;

import android.content.Context;
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
import com.eatda.sponsor.ChildManagement.SponsorChildManagementApiService;
import com.eatda.sponsor.ChildManagement.SponsorChildManagementRetrofitClient;
import com.eatda.sponsor.form.ChildResponse;
import com.eatda.sponsor.form.SponsorDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildMgmt extends AppCompatActivity {

    private LinearLayout childContainer; // 아동 정보를 표시할 레이아웃
    private Long sponsorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_mgmt);

        // 시스템 바 인셋 적용
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 레이아웃 초기화
        childContainer = findViewById(R.id.childContainer);  // activity_child_mgmt.xml 파일에 정의된 LinearLayout

        sponsorID = getSubFromToken();
        Log.d("sponsorID :", "현재 후원자 아이디는 " + sponsorID);
        // API 호출
        fetchChildrenData(sponsorID);
    }

    private void fetchChildrenData(Long sponsorId) {
        SponsorChildManagementApiService service = SponsorChildManagementRetrofitClient.getRetrofitInstance(this).create(SponsorChildManagementApiService.class);

        Call<List<ChildResponse>> call = service.getChildren(sponsorId);
        call.enqueue(new Callback<List<ChildResponse>>() {
            @Override
            public void onResponse(Call<List<ChildResponse>> call, Response<List<ChildResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChildResponse> children = response.body();
                    displayChildren(children); // 아동 정보를 동적으로 레이아웃에 추가
                } else {
                    Toast.makeText(ChildMgmt.this, "아동 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChildResponse>> call, Throwable t) {
                Toast.makeText(ChildMgmt.this, "API 호출 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }

    private void displayChildren(List<ChildResponse> children) {
        childContainer.removeAllViews(); // 기존 뷰를 모두 제거

        for (ChildResponse child : children) {
            View childView = LayoutInflater.from(this).inflate(R.layout.child_mgmt_list, childContainer, false);

            // 동적으로 뷰에 데이터 추가
            TextView childName = childView.findViewById(R.id.child_name);
            TextView childEmail = childView.findViewById(R.id.child_email);
            TextView childPhone = childView.findViewById(R.id.child_phone);
            TextView childAddress = childView.findViewById(R.id.child_address);
            Button deleteButton = childView.findViewById(R.id.delete_button);

            childName.setText(child.getName());
            childEmail.setText(child.getEmail());
            childPhone.setText(child.getChildNumber());
            childAddress.setText(child.getChildAddress());

            deleteButton.setOnClickListener(v -> {
                SponsorChildManagementApiService service = SponsorChildManagementRetrofitClient.getRetrofitInstance(this).create(SponsorChildManagementApiService.class);
                Call<SponsorDTO> call = service.deleteChildFromSponsor(sponsorID, child.getId());

                call.enqueue(new Callback<SponsorDTO>() {
                    @Override
                    public void onResponse(Call<SponsorDTO> call, Response<SponsorDTO> response) {
                        if (response.isSuccessful()) {
                            // 삭제 성공 시 해당 childView를 UI에서 제거
                            childContainer.removeView(childView);
                            Toast.makeText(getApplicationContext(), "Child deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // 실패 시 처리
                            Toast.makeText(getApplicationContext(), "Failed to delete child", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SponsorDTO> call, Throwable t) {
                        // 네트워크 오류 등 실패 처리
                        Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            });


            // 컨테이너에 뷰 추가
            childContainer.addView(childView);
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
