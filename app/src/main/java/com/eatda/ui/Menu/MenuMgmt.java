package com.eatda.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.eatda.data.api.menu.PresidentManageMenuApiService;
import com.eatda.data.form.menu.MenuResponse;
import com.eatda.data.api.president.PresidentRetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuMgmt extends AppCompatActivity {

    private LinearLayout menuContainer;
    private Long presidentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_mgmt);

        menuContainer = findViewById(R.id.menu_container);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn_addMenu = findViewById(R.id.btn_addMenu);
        btn_addMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuMgmt.this, MenuAdd.class);
                startActivity(intent);
            }
        });


        presidentId = getSubFromToken();
        getMenu();
    }

    private void getMenu(){
        PresidentManageMenuApiService service = PresidentRetrofitClient.getRetrofitInstance(this).create(PresidentManageMenuApiService.class);

        Call<List<MenuResponse>> call = service.getMenuByPresidentId(presidentId);
        call.enqueue(new Callback<List<MenuResponse>>() {
            @Override
            public void onResponse(Call<List<MenuResponse>> call, Response<List<MenuResponse>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<MenuResponse> menu = response.body();
                    Toast.makeText(MenuMgmt.this, "내 메뉴를 불러옵니다.", Toast.LENGTH_SHORT).show();
                    displayMenu(menu);
                }else{
                    Toast.makeText(MenuMgmt.this, "내 메뉴가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MenuResponse>> call, Throwable t) {
                Toast.makeText(MenuMgmt.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayMenu(List<MenuResponse> menu){
        for(MenuResponse response : menu){
            View menuView = LayoutInflater.from(this).inflate(R.layout.menu_item_mgmt, menuContainer, false);

            TextView menuName = menuView.findViewById(R.id.menu_name);
            TextView menuBody = menuView.findViewById(R.id.menu_body);
            TextView menuStatus = menuView.findViewById(R.id.menu_status);
            TextView menuPrice = menuView.findViewById(R.id.menu_price);

            menuName.setText(response.getMenuName());
            menuBody.setText(response.getMenuBody());
            if(response.getMenuStatus()){
                menuStatus.setText("주문 가능");
            }else{
                menuStatus.setText("품절");
            }
            menuPrice.setText(String.valueOf(response.getPrice()));

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
}