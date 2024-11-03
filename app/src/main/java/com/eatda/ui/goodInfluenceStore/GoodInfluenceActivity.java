package com.eatda.ui.goodInfluenceStore;

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
import com.eatda.data.api.childManagement.SponsorChildManagementApiService;
import com.eatda.data.api.childManagement.SponsorChildManagementRetrofitClient;
import com.eatda.data.api.goodInfluenceStore.GoodInfluenceStoreApiService;
import com.eatda.data.api.goodInfluenceStore.GoodInfluenceStoreRetrofitClient;
import com.eatda.data.form.childManagement.ChildResponse;
import com.eatda.data.form.goodInfluenceStore.GoodInfluenceStore;
import com.eatda.data.form.sponsor.SponsorDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoodInfluenceActivity extends AppCompatActivity {

    private static final String TAG = "GoodInfluenceActivity"; // 로그 태그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_card_join); // 확인: 올바른 레이아웃 사용
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        fetchGoodInfluenceStores();
    }

    private void fetchGoodInfluenceStores() {
        GoodInfluenceStoreApiService service = GoodInfluenceStoreRetrofitClient.getRetrofitInstance().create(GoodInfluenceStoreApiService.class);
        Call<List<GoodInfluenceStore>> call = service.callStoreApi();

        call.enqueue(new Callback<List<GoodInfluenceStore>>() {
            @Override
            public void onResponse(Call<List<GoodInfluenceStore>> call, Response<List<GoodInfluenceStore>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GoodInfluenceStore> stores = response.body();
                    // 가게 정보 처리
                    for (GoodInfluenceStore store : stores) {
                        Log.d(TAG, "가게 이름: " + store.getCMPNM_NM());
                    }
                } else {
                    Toast.makeText(GoodInfluenceActivity.this, "가게 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<GoodInfluenceStore>> call, Throwable t) {
                Log.e(TAG, "API 호출 실패: " + t.getMessage());
                Toast.makeText(GoodInfluenceActivity.this, "API 호출 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
