package com.eatda.ui.president;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eatda.ui.Join;
import com.eatda.ui.Login;
import com.eatda.R;
import com.eatda.data.api.login.LoginApiService;
import com.eatda.data.api.login.LoginRetrofitClient;
import com.eatda.data.form.login.PresidentJoinRequest;
import com.eatda.data.api.presidentBusinessNumberCheck.PresidentBusinessNumberCheckApiService;
import com.eatda.data.form.presidentBusinessNumberCheck.PresidentBusinessRequest;
import com.eatda.data.form.presidentBusinessNumberCheck.PresidentBusinessResponse;
import com.eatda.data.api.presidentBusinessNumberCheck.PresidentBusinessNumberCheckRetrofitClient;


import java.util.Collections;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresidentJoin extends AppCompatActivity {

    private EditText text_presidentBusinessNumber;
    private EditText text_name;
    private EditText text_email;
    private EditText text_password;
    private EditText text_password2;
    private EditText text_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_president_join);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn_presidentEnter = findViewById(R.id.btn_presidentEnter);
        Button btn_presidentPrevious = findViewById(R.id.btn_presidentPrevious);
        Button btn_presidnetBusinessNumberCheck = findViewById(R.id.BusinessNumberCheckBtn);
        text_presidentBusinessNumber = findViewById(R.id.presidentBusinessNumberText);
        text_name = findViewById(R.id.presidentNameText);
        text_email = findViewById(R.id.presidentEmailText);
        text_password = findViewById(R.id.presidentPasswordText);
        text_password2 = findViewById(R.id.presidentPasswordText2);
        text_number = findViewById(R.id.presidentNumberText);

        btn_presidentEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String presidentName = text_name.getText().toString();
                String presidentEmail = text_email.getText().toString();
                String presidentPassword = text_password.getText().toString();
                String presidentPassword2 = text_password2.getText().toString();
                String presidentBusinessNumber = text_presidentBusinessNumber.getText().toString();
                String presidentNumber = text_number.getText().toString();

                if(presidentName.isEmpty()) {
                    showAlertDialog("이름 입력", "이름을 입력하세요");
                }else if(presidentEmail.isEmpty()){
                    showAlertDialog("이메일 입력", "이메일을 입력하세요");
                }else if(presidentPassword.isEmpty()){
                    showAlertDialog("비밀번호 입력", "비밀번호를 입력하세요");
                }else if(presidentPassword2.isEmpty()){
                    showAlertDialog("비밀번호 재확인", "비밀번호를 확인해주세요");
                }else if(presidentNumber.isEmpty()){
                    showAlertDialog("전화번호 입력", "전화번호를 입력해주세요");
                } else if(presidentBusinessNumber.isEmpty() || text_presidentBusinessNumber.isEnabled()){
                    showAlertDialog("사업자 번호 인증", "사업자 번호를 인증해주세요");
                }


                if(!presidentPassword.equals(presidentPassword2)){
                    showAlertDialog("비밀번호 불일치", "비밀번호가 일치하지 않습니다.");
                }

                //사업자 번호 인증 됐을 때
                if(!text_presidentBusinessNumber.isEnabled()){
                    joinPresident(presidentName, presidentEmail, presidentPassword, presidentNumber, presidentBusinessNumber);
                }else{
                    showAlertDialog("사업자 번호 인증", "사업자 번호를 인증해주세요");
                }


            }
        });



        // 이전 버튼
        btn_presidentPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PresidentJoin.this, Join.class);
                startActivity(intent);
            }
        });


        // 버튼 클릭 시 API 호출
        btn_presidnetBusinessNumberCheck.setOnClickListener(v -> {
            String businessNumber = text_presidentBusinessNumber.getText().toString();
            if (!businessNumber.isEmpty()) {
                checkBusinessNumber(businessNumber);
            } else {
                Toast.makeText(PresidentJoin.this, "사업자 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void joinPresident(String presidentName, String presidentEmail, String presidentPassword, String presidentNumber, String presidentBusinessNumber){
        LoginApiService loginApiService = LoginRetrofitClient.getRetrofitInstance().create(LoginApiService.class);
        PresidentJoinRequest requestBody = new PresidentJoinRequest(presidentName, presidentEmail, presidentPassword, presidentNumber ,presidentBusinessNumber);

        Call<String> call = loginApiService.joinPresident(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Retrofit Response", "Code: " + response.code() + ", Body: " + response.body());
                if(response.isSuccessful() && response.body() != null){
                    joinShowAlertDialog("가입 완료", "가입이 완료되었습니다.",true);
                }else{
                    joinShowAlertDialog("가입 실패", "가입에 실패하였습니다.",false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showAlertDialog("가입 실패", "가입에 실패하였습니다.2");
                Log.e("Retrofit Error", "Failure: " + t.getMessage());
            }
        });
    }



    private void checkBusinessNumber(String businessNumber) {
        PresidentBusinessNumberCheckApiService presidentBusinessNumberCheckApiService = PresidentBusinessNumberCheckRetrofitClient.getRetrofitInstance().create(PresidentBusinessNumberCheckApiService.class);
        PresidentBusinessRequest requestBody = new PresidentBusinessRequest(Collections.singletonList(businessNumber));

        Call<PresidentBusinessResponse> call = presidentBusinessNumberCheckApiService.checkBusinessNumber(requestBody);
        call.enqueue(new Callback<PresidentBusinessResponse>() {
            @Override
            public void onResponse(Call<PresidentBusinessResponse> call, Response<PresidentBusinessResponse> response) {

                if(response.isSuccessful() && response.body() != null){
                    PresidentBusinessResponse apiResponse = response.body();
                    if(Objects.equals(apiResponse.getData().get(0).getTaxType(), "국세청에 등록되지 않은 사업자등록번호입니다.")){
                        //Toast.makeText(PresidentJoin.this, "사업자 번호가 존재하지 않음.", Toast.LENGTH_SHORT).show();
                        showAlertDialog("사업자 번호 오류", "사업자 번호가 존재하지 않습니다.");
                        text_presidentBusinessNumber.setText("");
                    }else{
                        //Toast.makeText(PresidentJoin.this, "사업자 번호 인증 완료", Toast.LENGTH_SHORT).show();
                        showAlertDialog("인증 성공", "사업자 번호 인증이 완료되었습니다.");
                        text_presidentBusinessNumber.setEnabled(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<PresidentBusinessResponse> call, Throwable t) {
                Log.e("API Error", "Failure: " + t.getMessage());
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PresidentJoin.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void joinShowAlertDialog(String title, String message, boolean shouldStartIntent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PresidentJoin.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> {
            dialog.dismiss();
            // Intent를 실행해야 하는 경우
            if (shouldStartIntent) {
                Intent intent = new Intent(PresidentJoin.this, Login.class);
                startActivity(intent);
            }
        });
        builder.show();
    }

}
