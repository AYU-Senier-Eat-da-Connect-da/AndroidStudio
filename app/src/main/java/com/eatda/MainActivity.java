package com.eatda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eatda.data.api.login.LoginApiService;
import com.eatda.data.api.login.LoginRetrofitClient;
import com.eatda.data.form.login.LoginRequest;
import com.eatda.ui.Join;
import com.eatda.ui.Login;
import com.eatda.ui.home.Home;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn_login = findViewById(R.id.signInButton);
        TextView btn_join = findViewById(R.id.signUpText);
        EditText text_email = findViewById(R.id.email);
        EditText text_password = findViewById(R.id.password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = text_email.getText().toString();
                String userPassword = text_password.getText().toString();

                if(userEmail.isEmpty() && userPassword.isEmpty()){
                    showAlertDialog("정보 입력", "사용자 정보를 입력해주세요.");
                }else if(userEmail.isEmpty()){
                    showAlertDialog("이메일 입력", "이메일을 입력해주세요.");
                }else if(userPassword.isEmpty()){
                    showAlertDialog("비밀번호 입력", "비밀번호를 입력해주세요.");
                }else{
                    login(userEmail, userPassword);
                }
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Join.class);
                startActivity(intent);
            }
        });

    }

    private void login(String userEmail, String userPassword) {
        LoginApiService loginApiService = LoginRetrofitClient.getRetrofitInstance().create(LoginApiService.class);
        LoginRequest requestBody = new LoginRequest(userEmail, userPassword);

        Call<String> call = loginApiService.loginAll(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Login Retrofit Response : ", "Code :" + response.code() + "Body: " + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    // JWT 토큰을 SharedPreferences에 저장
                    saveToken(response.body());

                    Toast.makeText(MainActivity.this, "환영합니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "로그인 실패.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "네트워크 오류.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwt_token", token);
        editor.apply();
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}