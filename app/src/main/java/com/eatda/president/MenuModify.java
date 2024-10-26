package com.eatda.president;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eatda.R;
import com.eatda.president.Menu.PresidentManageMenuApiService;
import com.eatda.president.Menu.form.MenuRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuModify extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_modify);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText text_menuName = findViewById(R.id.menu_name);
        EditText text_menuBody = findViewById(R.id.menu_body);
        EditText text_menuPrice = findViewById(R.id.menu_price);
        RadioGroup radioGroup = findViewById(R.id.menu_status_group);
        RadioButton menuStatusAvailable = findViewById(R.id.menu_status_available);
        RadioButton menuStatusSoldOut = findViewById(R.id.menu_status_sold_out);
        Button btn_menuModify = findViewById(R.id.modify_button);

        // Intent로부터 전달된 데이터 가져오기
        Intent intent = getIntent();
        String menuName = intent.getStringExtra("menuName");
        String menuBody = intent.getStringExtra("menuBody");
        boolean menuStatus = intent.getBooleanExtra("menuStatus", true);
        int menuPrice = intent.getIntExtra("menuPrice", 0);

        // UI 요소에 데이터 설정
        text_menuName.setText(menuName);
        text_menuBody.setText(menuBody);
        text_menuPrice.setText(String.valueOf(menuPrice));
        if (menuStatus) {
            menuStatusAvailable.setChecked(true);
        } else {
            menuStatusSoldOut.setChecked(true);
        }

        btn_menuModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String menuName = text_menuName.getText().toString();
                String menuBody = text_menuBody.getText().toString();
                String menuPrice = text_menuPrice.getText().toString();
                int radioId = radioGroup.getCheckedRadioButtonId();
                boolean menuStatus;

                if(radioId == menuStatusAvailable.getId()){
                    menuStatus = true;
                }else{
                    menuStatus = false;
                }

                if(menuName.isEmpty()){
                    showAlertDialog("메뉴 이름 입력", "메뉴 이름을 입력하세요.");
                }else if(menuBody.isEmpty()){
                    showAlertDialog("메뉴 설명 입력", "메뉴 설명을 입력하세요.");
                }else if(menuPrice.isEmpty()){
                    showAlertDialog("메뉴 가격 입력", "메뉴 가격을 입력하세요.");
                }else if(radioId == 0){
                    showAlertDialog("메뉴 상태 선택","메뉴 상태를 확인해주세요.");
                }else{
                    modifyMenu(menuName,menuBody,menuStatus,menuPrice);
                }

            }
        });
    }

    private void modifyMenu(String menuName, String menuBody, boolean menuStatus, String menuPrice) {
        Intent intent = getIntent();
        long menuId = intent.getLongExtra("menuId", -1);

        PresidentManageMenuApiService service = PresidentRetrofitClient.getRetrofitInstance(this).create(PresidentManageMenuApiService.class);
        MenuRequest request = new MenuRequest().modifyMenu(menuName, menuBody, menuStatus, Integer.parseInt(menuPrice));

        Call<MenuRequest> call = service.updateMenu(menuId, request);
        call.enqueue(new Callback<MenuRequest>() {
            @Override
            public void onResponse(Call<MenuRequest> call, Response<MenuRequest> response) {
                if(response.isSuccessful() && response.body() != null){
                    modifyShowAlertDialog("수정 완료", "메뉴 수정이 완료되었습니다.",true);
                }else{
                    modifyShowAlertDialog("수정 실패", "메뉴 수정이 실패했습니다.",true);
                }
            }

            @Override
            public void onFailure(Call<MenuRequest> call, Throwable t) {
                showAlertDialog("수정 실패", "네트워크 오류");
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuModify.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void modifyShowAlertDialog(String title, String message, boolean shouldStartIntent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuModify.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> {
            dialog.dismiss();
            if (shouldStartIntent) {
                Intent intent = new Intent(MenuModify.this, RestaurantsMgmt.class);
                startActivity(intent);
            }
        });
        builder.show();
    }
}