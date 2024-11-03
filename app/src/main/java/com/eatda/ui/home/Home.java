package com.eatda.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.eatda.kakaomap.MyLocationMap;
import com.eatda.ui.child.ChildMyPage;
import com.eatda.ui.president.PresidentMyPage;
import com.eatda.ui.sponsor.SponsorMyPage;

import java.util.Arrays;
import java.util.List;

public class Home extends AppCompatActivity {

    private EditText searchEditText;
    private Button cultlet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 텍스트로 검색
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String searchText = searchEditText.getText().toString();

                    // 다음 액티비티로 전환하면서 입력 텍스트 전달
                    Intent intent = new Intent(Home.this, SearchText.class);
                    intent.putExtra("searchText", searchText);
                    startActivity(intent);

                    return true;
                }
                return false;
            }
        });

        // 내 위치로 검색
        Button findMyLocation = findViewById(R.id.btn_findMyLocation);
        findMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, MyLocationMap.class);
                startActivity(intent);
            }
        });

        List<Button> foodButtons = Arrays.asList(
                findViewById(R.id.cutlet),
                findViewById(R.id.pizza),
                findViewById(R.id.meat),
                findViewById(R.id.western),
                findViewById(R.id.china),
                findViewById(R.id.chicken),
                findViewById(R.id.korea),
                findViewById(R.id.hamburger),
                findViewById(R.id.ricecake),
                findViewById(R.id.cafe)
        );

        for (Button button : foodButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String buttonText = ((Button) v).getText().toString();
                    goSearchButton(buttonText);
                }
            });
        }



        // 마이 페이지
        ImageButton btn_myPage = findViewById(R.id.btn_myPage);
        btn_myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = getToken();

                if (token != null) {
                    JWT jwt = new JWT(token);
                    Claim authClaim = jwt.getClaim("auth");
                    String authValue = authClaim.asString();

                    Log.d("JWT Claims", "auth: " + authValue);

                    if ("ROLE_CHILD".equals(authValue)) {
                        Intent intent = new Intent(Home.this, ChildMyPage.class);
                        startActivity(intent);
                    } else if ("ROLE_PRESIDENT".equals(authValue)) {
                        Intent intent = new Intent(Home.this, PresidentMyPage.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Home.this, SponsorMyPage.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(Home.this, "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void goSearchButton(String buttonText){
        Intent intent = new Intent(Home.this, SearchButton.class);
        intent.putExtra("buttonText", buttonText); // buttonText 값 전달
        startActivity(intent);
    }

    private String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("jwt_token", null);
    }
}
