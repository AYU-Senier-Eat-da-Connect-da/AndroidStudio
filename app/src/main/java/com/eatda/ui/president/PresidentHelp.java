package com.eatda.ui.president;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.eatda.R;
import com.eatda.data.api.firebase.FcmApi;
import com.eatda.data.api.firebase.FcmRetrofitClient;
import com.eatda.data.api.order.OrderApiService;
import com.eatda.data.api.order.OrderRetrofitClient;
import com.eatda.data.form.firbase.FCMNotificationRequestDTO;
import com.eatda.data.form.order.OrderPresidentResponse;
import com.eatda.ui.order.Order;
import com.eatda.ui.restaurant.RestaurantsMgmt;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresidentHelp extends AppCompatActivity {

    private LinearLayout menu_container;
    private Long presidentId;
    private static final int REQUEST_ALARM_PERMISSION = 101;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_president_help);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        requestAlarmPermission();
        presidentId = getSubFromToken();
        menu_container = findViewById(R.id.menu_container);

        getOrderList();
    }

    private void getOrderList(){
        OrderApiService service = OrderRetrofitClient.getRetrofitInstance(this).create(OrderApiService.class);

        Call<List<OrderPresidentResponse>> call = service.getOrderByPresidentId(presidentId);
        call.enqueue(new Callback<List<OrderPresidentResponse>>() {
            @Override
            public void onResponse(Call<List<OrderPresidentResponse>> call, Response<List<OrderPresidentResponse>> response) {
                if(response.isSuccessful()){
                    if(response.body() == null) {
                        Toast.makeText(PresidentHelp.this, "주문 목록이 없습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        displayOrderList(response);
                    }
                }else{
                    Toast.makeText(PresidentHelp.this, "주문 목록 가져오기 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderPresidentResponse>> call, Throwable t) {
                Toast.makeText(PresidentHelp.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayOrderList(Response<List<OrderPresidentResponse>> response) {
        // 서버에서 받은 주문 목록을 순회
        for (OrderPresidentResponse order : response.body()) {
            // ConstraintLayout 동적 생성
            CardView orderCardView = (CardView) getLayoutInflater()
                    .inflate(R.layout.president_order_list_item, menu_container, false);

            // UI 요소에 데이터 바인딩
            TextView restaurantName = orderCardView.findViewById(R.id.restaurant_name);
            TextView phoneNumber = orderCardView.findViewById(R.id.phone_number);
            TextView address = orderCardView.findViewById(R.id.address);
            TextView orderTime = orderCardView.findViewById(R.id.order_time);
            TextView price = orderCardView.findViewById(R.id.price);
            LinearLayout menuContainer = orderCardView.findViewById(R.id.menu_container);

            restaurantName.setText(order.getChild().getName()); // 아동 이름
            phoneNumber.setText("전화번호: " + order.getChild().getPhone()); // 전화번호
            address.setText("주소: " + order.getChild().getAddress()); // 주소
            orderTime.setText("주문 시간: " + order.getOrderTime()); // 주문 시간
            price.setText("가격: " + order.getPrice() + "원"); // 가격

            // 메뉴 정보 동적 추가
            for (OrderPresidentResponse.MenuOrder menuOrder : order.getMenuOrders()) {
                TextView menuItem = new TextView(this);
                menuItem.setText(menuOrder.getMenuName() + " - " + menuOrder.getMenuBody() + " (" + menuOrder.getMenuCount() + "개)");
                menuItem.setTextSize(14);
                menuItem.setPadding(0, 4, 0, 4);
                menuContainer.addView(menuItem);
            }

            // 리뷰 작성 버튼 초기화
            Button writeReviewButton = orderCardView.findViewById(R.id.btn_write_review);
            writeReviewButton.setOnClickListener(v -> {
                long childId = order.getChildId();
                String userType = "CHILD";
                FCMNotificationRequestDTO requestDTO = new FCMNotificationRequestDTO("조리 완료", "조리가 완료 되었습니다.");
                fcm(childId, userType, requestDTO);
                showOrderPopup();
                writeReviewButton.setText("수령 대기");
            });

            // orderLayout을 menu_container에 추가
            menu_container.addView(orderCardView);
        }
    }

    private void fcm(Long userId, String userType, FCMNotificationRequestDTO requestDTO){
        FcmApi service = FcmRetrofitClient.getRetrofitInstance().create(FcmApi.class);

        Call<String> call = service.sendNotification(userId, userType, requestDTO);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "알림 전송 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "알림 전송 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "알림 전송 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showOrderPopup() {
        runOnUiThread(() -> {
            new androidx.appcompat.app.AlertDialog.Builder(PresidentHelp.this)
                    .setTitle("조리 완료")
                    .setMessage("조리가 완료 되었습니다.")
                    .setPositiveButton("확인", (dialog, which) -> dialog.dismiss())
                    .show();
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

    private void requestAlarmPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_ALARM_PERMISSION);
            }
        }
    }
}