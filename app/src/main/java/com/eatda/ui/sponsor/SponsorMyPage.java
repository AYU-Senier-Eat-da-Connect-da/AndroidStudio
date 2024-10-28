package com.eatda.ui.sponsor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eatda.R;
import com.eatda.ui.childManagement.ChildList;
import com.eatda.ui.childManagement.ChildMgmt;

public class SponsorMyPage extends AppCompatActivity {

    private Button btn_child_mgmt;
    private Button btn_child_list;
    private Button btn_support;
    private Button btn_sponsor_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sponsor_my_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*
        후원한 아동 관리
         */
        btn_child_mgmt = findViewById(R.id.btn_child_mgmt);
        btn_child_mgmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SponsorMyPage.this, ChildMgmt.class);
                startActivity(intent);
            }
        });

        /*
        후원할 아동 조회
         */
        btn_child_list = findViewById(R.id.btn_child_list);
        btn_child_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SponsorMyPage.this, ChildList.class);
                startActivity(intent);
            }
        });

        /*
        후원하기
         */
        btn_support = findViewById(R.id.btn_support);
        btn_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SponsorMyPage.this, Support.class);
                startActivity(intent);
            }
        });

        /*
        후원자 도움말
         */
        btn_sponsor_help = findViewById(R.id.btn_sponsor_help);
        btn_sponsor_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SponsorMyPage.this, SponsorHelp.class);
                startActivity(intent);
            }
        });
    }
}