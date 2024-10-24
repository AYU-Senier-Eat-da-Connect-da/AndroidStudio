package com.eatda.president;

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

import com.eatda.Join;
import com.eatda.Login;
import com.eatda.R;
import com.eatda.president.PresidentBusinessNumberCheck.PresidentBusinessNumberCheckApiService;
import com.eatda.president.PresidentBusinessNumberCheck.PresidentBusinessRequest;
import com.eatda.president.PresidentBusinessNumberCheck.PresidentBusinessResponse;
import com.eatda.president.PresidentBusinessNumberCheck.PresidentBusinessNumberCheckRetrofitClient;


import java.util.Collections;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresidentJoin extends AppCompatActivity {

    private Button btn_presidentEnter;
    private Button btn_presidentPrevious;
    private Button btn_presidnetBusinessNumberCheck;
    private EditText text_presidentBusinessNumber;
    private PresidentBusinessNumberCheckApiService presidentBusinessNumberCheckApiService;

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

        btn_presidentEnter = findViewById(R.id.btn_presidentEnter);
        btn_presidentEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PresidentJoin.this, Login.class);
                startActivity(intent);
            }
        });

        btn_presidentPrevious = findViewById(R.id.btn_presidentPrevious);
        btn_presidentPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PresidentJoin.this, Join.class);
                startActivity(intent);
            }
        });

        text_presidentBusinessNumber = findViewById(R.id.presidentBusinessNumberText);
        btn_presidnetBusinessNumberCheck=findViewById(R.id.BusinessNumberCheckBtn);

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


    private void checkBusinessNumber(String businessNumber) {
        presidentBusinessNumberCheckApiService = PresidentBusinessNumberCheckRetrofitClient.getRetrofitInstance().create(PresidentBusinessNumberCheckApiService.class);
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
}
