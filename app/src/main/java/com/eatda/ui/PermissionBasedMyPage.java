//package com.eatda.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.eatda.R;
//import com.eatda.ui.child.ChildMyPage;
//import com.eatda.ui.president.PresidentMyPage;
//import com.eatda.ui.sponsor.SponsorMyPage;
//
//public class PermissionBasedMyPage extends AppCompatActivity {
//
//    private Button btn_child_myPage;
//    private Button btn_president_myPage;
//    private Button btn_sponsor_myPage;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_permission_based_my_page);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        /*
//        아동 마이페이지
//         */
//        btn_child_myPage = findViewById(R.id.btn_child_myPage);
//        btn_child_myPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(PermissionBasedMyPage.this, ChildMyPage.class);
//                startActivity(intent);
//            }
//        });
//
//        /*
//        사장님 마이페이지
//         */
//        btn_president_myPage = findViewById(R.id.btn_president_myPage);
//        btn_president_myPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(PermissionBasedMyPage.this, PresidentMyPage.class);
//                startActivity(intent);
//            }
//        });
//
//        /*
//        후원자 마이페이지
//         */
//        btn_sponsor_myPage = findViewById(R.id.btn_sponsor_myPage);
//        btn_sponsor_myPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(PermissionBasedMyPage.this, SponsorMyPage.class);
//                startActivity(intent);
//            }
//        });
//    }
//}