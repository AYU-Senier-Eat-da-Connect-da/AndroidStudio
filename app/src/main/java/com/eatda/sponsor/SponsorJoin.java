package com.eatda.sponsor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eatda.Join;
import com.eatda.Login;
import com.eatda.R;
import com.eatda.child.ChildJoin;
import com.eatda.login.LoginApiService;
import com.eatda.login.LoginRetrofitClient;
import com.eatda.login.form.ChildJoinRequest;
import com.eatda.login.form.SponsorJoinRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SponsorJoin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sponsor_join);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn_sponsorEnter = findViewById(R.id.btn_sponsorEnter);
        EditText text_name = findViewById(R.id.sponsorNameText);
        EditText text_email = findViewById(R.id.sponsorEmailText);
        EditText text_password = findViewById(R.id.sponsorPasswordText);
        EditText text_password2 = findViewById(R.id.sponsorPasswordText2);
        EditText text_number = findViewById(R.id.sponsorNumberText);
        EditText text_address = findViewById(R.id.sponsorAddressText);

        btn_sponsorEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sponsorName = text_name.getText().toString();
                String sponsorEmail = text_email.getText().toString();
                String sponsorPassword = text_password.getText().toString();
                String sponsorPassword2 = text_password2.getText().toString();
                String sponsorNumber = text_number.getText().toString();
                String sponsorAddress = text_address.getText().toString();

                if(sponsorName.isEmpty()) {
                    showAlertDialog("이름 입력", "이름을 입력하세요");
                }else if(sponsorEmail.isEmpty()){
                    showAlertDialog("이메일 입력", "이메일을 입력하세요");
                }else if(sponsorPassword.isEmpty()){
                    showAlertDialog("비밀번호 입력", "비밀번호를 입력하세요");
                }else if(sponsorPassword2.isEmpty()){
                    showAlertDialog("비밀번호 재확인", "비밀번호를 확인해주세요");
                }else if(sponsorNumber.isEmpty()){
                    showAlertDialog("전화번호 입력", "전화번호를 입력해주세요");
                }else if(sponsorAddress.isEmpty()){
                    showAlertDialog("주소", "주소를 입력해주세요");
                }else if(!sponsorPassword.equals(sponsorPassword2)){
                    showAlertDialog("비밀번호 불일치", "비밀번호가 일치하지 않습니다.");
                }else{
                    joinSponsor(sponsorName, sponsorEmail, sponsorPassword, sponsorNumber, sponsorAddress);
                }
            }
        });

        Button btn_sponsorPrevious = findViewById(R.id.btn_sponsorPrevious);
        btn_sponsorPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SponsorJoin.this, Join.class);
                startActivity(intent);
            }
        });
    }

    private void joinSponsor(String sponsorName, String sponsorEmail, String sponsorPassword, String sponsorNumber, String sponsorAddress) {
        LoginApiService loginApiService = LoginRetrofitClient.getRetrofitInstance().create(LoginApiService.class);
        SponsorJoinRequest requestBody = new SponsorJoinRequest(sponsorName, sponsorEmail, sponsorPassword, sponsorNumber, sponsorAddress);

        Call<String> call = loginApiService.joinSponsor(requestBody);
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


    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SponsorJoin.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void joinShowAlertDialog(String title, String message, boolean shouldStartIntent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SponsorJoin.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> {
            dialog.dismiss();
            // Intent를 실행해야 하는 경우
            if (shouldStartIntent) {
                Intent intent = new Intent(SponsorJoin.this, Login.class);
                startActivity(intent);
            }
        });
        builder.show();
    }


}

