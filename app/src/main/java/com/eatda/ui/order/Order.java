package com.eatda.ui.order;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.eatda.R;
import com.eatda.data.api.order.OrderApiService;
import com.eatda.data.api.order.OrderRetrofitClient;
import com.eatda.data.form.menu.MenuResponse;
import com.eatda.data.form.order.MenuOrder;
import com.eatda.data.form.order.OrderRequest;
import com.eatda.data.form.order.OrderResponse;
import com.eatda.ui.menu.MenuAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order extends AppCompatActivity {

    private Long restaurantId;
    private Long childId;
    private ArrayList<MenuResponse> selectedMenus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        restaurantId = intent.getLongExtra("restaurantId",-1);
        childId = getSubFromToken();
        String restaurantName = intent.getStringExtra("restaurantName");
        String restaurantAddress = intent.getStringExtra("restaurantAddress");
        String restaurantNumber = intent.getStringExtra("restaurantNumber");
        int sum = intent.getIntExtra("sum", 0);
        selectedMenus = intent.getParcelableArrayListExtra("selectedMenus");
        Button btn_order = findViewById(R.id.order_button);

        TextView nameTextView = findViewById(R.id.restaurant_name);
        TextView addressTextView = findViewById(R.id.restaurant_address);
        TextView numberTextView = findViewById(R.id.restaurant_number);
        TextView totalSumTextView = findViewById(R.id.total_sum_value);

        nameTextView.setText(restaurantName);
        addressTextView.setText(restaurantAddress);
        numberTextView.setText(restaurantNumber);
        totalSumTextView.setText(String.format("%,d 원", sum));


        RecyclerView recyclerView = findViewById(R.id.selected_menu_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MenuAdapter adapter = new MenuAdapter(selectedMenus);
        recyclerView.setAdapter(adapter);

        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order();
            }
        });

    }

    private void order(){

        List<MenuOrder> menuOrders = new ArrayList<>();
        for (MenuResponse menu : selectedMenus) {
            menuOrders.add(new MenuOrder(menu.getMenuId(), 1));
        }

        int totalSum = getIntent().getIntExtra("sum",0);
        OrderRequest orderRequest = new OrderRequest(childId, restaurantId, menuOrders, totalSum);

        OrderApiService service = OrderRetrofitClient.getRetrofitInstance(this).create(OrderApiService.class);
        Call<OrderResponse> call = service.createOrder(orderRequest);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    // 성공 처리 - 확인 메시지 표시
                    Toast.makeText(Order.this, "주문이 성공적으로 완료되었습니다!", Toast.LENGTH_SHORT).show();
                } else {
                    // 실패 처리 - 오류 메시지 표시
                    if (response.errorBody() != null) {
                        Toast.makeText(Order.this, "에러 : " + response.errorBody(), Toast.LENGTH_SHORT).show();

                        try {
                            String errorMessage = response.errorBody().string();
                            if (errorMessage.contains("잔액이 부족합니다.")) {
                                Toast.makeText(Order.this, "잔액이 부족합니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Order.this, "주문에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(Order.this, "에러 메시지를 가져오는 중 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Order.this, "주문에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(Order.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Long getSubFromToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("jwt_token", null);

        if (token != null) {
            JWT jwt = new JWT(token);
            Claim subClaim = jwt.getClaim("sub");
            return subClaim.asLong();  // sub 값을 Long으로 변환하여 반환
        }

        return null;
    }
}
