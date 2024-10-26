package com.eatda.president;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.eatda.president.Menu.form.MenuRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuAdd extends AppCompatActivity {

    private Long presidentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presidentId = getSubFromToken();
        Button btn_addRestaurant = findViewById(R.id.register_button);
        EditText text_menuName = findViewById(R.id.menu_name);
        EditText text_menuBody = findViewById(R.id.menu_body);
        EditText text_price = findViewById(R.id.menu_price);
        RadioGroup radioGroup = findViewById(R.id.menu_status_group);
        RadioButton Rbtn_true = findViewById(R.id.menu_status_available);
        RadioButton Rbtn_false = findViewById(R.id.menu_status_sold_out);

        btn_addRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String menuName = text_menuName.getText().toString();
                String menuBody = text_menuBody.getText().toString();
                String price = text_price.getText().toString();
                int radioId = radioGroup.getCheckedRadioButtonId();
                boolean menuStatus;

                if(radioId == Rbtn_true.getId()){
                    menuStatus = true;
                }else{
                    menuStatus = false;
                }

                if(menuName.isEmpty()){
                    showAlertDialog("메뉴 이름 입력", "메뉴 이름을 입력하세요.");
                }else if(menuBody.isEmpty()){
                    showAlertDialog("메뉴 설명 입력", "메뉴 설명을 입력하세요.");
                }else if(price.isEmpty()){
                    showAlertDialog("메뉴 가격 입력", "메뉴 가격을 입력하세요.");
                }else if(radioId == 0){
                    showAlertDialog("메뉴 상태 선택","메뉴 상태를 확인해주세요.");
                }else{
                    addMenu(menuName,menuBody,menuStatus,price);
                }
            }
        });
    }

    private void addMenu(String menuName, String menuBody, boolean menuStatus, String price) {
        PresidentManageMenuApiService service = PresidentRetrofitClient.getRetrofitInstance(this).create(PresidentManageMenuApiService.class);
        MenuRequest request = new MenuRequest(menuName, menuBody, Integer.parseInt(price), menuStatus, presidentId);

        Call<MenuRequest> call = service.addMenu(request,presidentId);
        call.enqueue(new Callback<MenuRequest>() {
            @Override
            public void onResponse(Call<MenuRequest> call, Response<MenuRequest> response) {
                if(response.isSuccessful() && response.body() != null){
                    addShowAlertDialog("등록 완료", "메뉴 등록 완료되었습니다.",true);
                }else{
                    addShowAlertDialog("등록 실패", "등록에 실패하였습니다.",false);
                }
            }

            @Override
            public void onFailure(Call<MenuRequest> call, Throwable t) {
                showAlertDialog("등록 실패", "네트워크 오류");
            }
        });
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

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuAdd.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void addShowAlertDialog(String title, String message, boolean shouldStartIntent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuAdd.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> {
            dialog.dismiss();
            if (shouldStartIntent) {
                Intent intent = new Intent(MenuAdd.this, RestaurantsMgmt.class);
                startActivity(intent);
            }
        });
        builder.show();
    }
}