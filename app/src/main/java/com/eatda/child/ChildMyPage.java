package com.eatda.UI.child;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eatda.R;
import com.eatda.review.AddReview;
import com.eatda.review.ChildMyReviewList;

public class ChildMyPage extends AppCompatActivity {

    private Button btn_child_myReviewList;
    private Button btn_child_buyList;
    private Button btn_card_balance;
    private Button btn_child_help;

    //Todo: 임시 (삭제필요). 주문했던 정보에서 리뷰를 작성 할 수 있게 해야함
    private Button btn_addReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_my_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*
            리뷰 작성 Todo: 임시. 삭제필요
        */

        btn_addReview = findViewById(R.id.btn_addReview);
        btn_addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChildMyPage.this, AddReview.class);
                startActivity(intent);
            }
        });

        /*
        (아동) 내가 쓴 리뷰 리스트
         */
        btn_child_myReviewList = findViewById(R.id.btn_child_myReviewList);
        btn_child_myReviewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChildMyPage.this, ChildMyReviewList.class);
                startActivity(intent);
            }
        });

        /*
        (아동) 주문내역
         */
        btn_child_buyList = findViewById(R.id.btn_child_buyList);
        btn_child_buyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChildMyPage.this, ChildBuyList.class);
                startActivity(intent);
            }
        });

        /*
        급식 카드 잔액 조회
         */
        btn_card_balance = findViewById(R.id.btn_card_balance);
        btn_card_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //급식 카드 잔액 조회 사이트
                String url = "https://www.purmeecard.com/index6.jsp";

                //인텐트로 웹 브라우저 열기
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        /*
        도움말
         */
        btn_child_help = findViewById(R.id.btn_child_help);
        btn_child_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChildMyPage.this, ChildHelp.class);
                startActivity(intent);
            }
        });
    }
}