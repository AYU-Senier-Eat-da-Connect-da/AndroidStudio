package com.eatda.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eatda.PermissionBasedMyPage;
import com.eatda.R;
import com.eatda.child.ChildMyPage;

public class Home extends AppCompatActivity {

    private EditText searchEditText;
    private Button findMyLocation;
    private Button btn_permissionBasedMyPage;   //권한 별 마이페이지 이동 (임시버튼)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        findMyLocation = findViewById(R.id.btn_findMyLocation);
        findMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, MyLocationMap.class);
                startActivity(intent);
            }
        });

        /*
        권한 별 마이페이지 이동 (임시버튼)
         */
        btn_permissionBasedMyPage = findViewById(R.id.btn_permissionBasedMyPage);
        btn_permissionBasedMyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, PermissionBasedMyPage.class);
                startActivity(intent);
            }
        });
    }
}