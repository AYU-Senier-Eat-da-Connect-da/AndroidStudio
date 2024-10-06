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

import com.eatda.Join;
import com.eatda.Login;
import com.eatda.R;

public class PresidentJoin extends AppCompatActivity {

    private Button btn_presidentEnter;
    private Button btn_presidentPrevious;

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
    }
}