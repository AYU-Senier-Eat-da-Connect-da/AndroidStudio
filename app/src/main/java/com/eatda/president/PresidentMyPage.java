package com.eatda.president;

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
import com.eatda.president.Menu.MenuMgmt;
import com.eatda.president.Restaurant.RestaurantsMgmt;
import com.eatda.review.RestaurantReviewList;

public class PresidentMyPage extends AppCompatActivity {

    private Button btn_restaurants_mgmt;
    private Button btn_menu_mgmt;
    private Button btn_president_help;
    private Button btn_president_add;

    //Todo: 임시위치 (가게 정보에서 Reviews에 위치해야함)
    private Button btn_restaurant_review_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_president_my_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*
        가게 관리
         */
        btn_restaurants_mgmt = findViewById(R.id.btn_restaurants_mgmt);
        btn_restaurants_mgmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PresidentMyPage.this, RestaurantsMgmt.class);
                startActivity(intent);
            }
        });

        /*
        메뉴 관리
         */
        btn_menu_mgmt = findViewById(R.id.btn_menu_mgmt);
        btn_menu_mgmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PresidentMyPage.this, MenuMgmt.class);
                startActivity(intent);
            }
        });

        /*
        도움말
         */
        btn_president_help = findViewById(R.id.btn_president_help);
        btn_president_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PresidentMyPage.this, PresidentHelp.class);
                startActivity(intent);
            }
        });

        /*
         내가게 리뷰 리스트 조회
         */
        btn_restaurant_review_list = findViewById(R.id.btn_restaurant_review_list);
        btn_restaurant_review_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PresidentMyPage.this, RestaurantReviewList.class);
                startActivity(intent);
            }
        });

/*

        btn_president_add = findViewById(R.id.btn_restaurants_add);
        btn_president_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PresidentMyPage.this, Test.class);
                startActivity(intent);
            }
        });

 */


    }
}