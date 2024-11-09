package com.eatda.ui.childManagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.eatda.R;
import com.eatda.data.api.childManagement.SponsorChildManagementApiService;
import com.eatda.data.api.childManagement.SponsorChildManagementRetrofitClient;
import com.eatda.data.api.order.OrderApiService;
import com.eatda.data.api.order.OrderRetrofitClient;
import com.eatda.data.form.childManagement.ChildResponse;
import com.eatda.data.form.order.OrderResponse;
import com.eatda.data.form.sponsor.SponsorDTO;
import com.eatda.ui.order.OrderAdapter;
import com.eatda.ui.order.RecentOrderAdapter;

import java.util.ArrayList;
import java.util.List;

import kr.co.bootpay.android.*;
import kr.co.bootpay.android.events.BootpayEventListener;
import kr.co.bootpay.android.models.BootExtra;
import kr.co.bootpay.android.models.BootItem;
import kr.co.bootpay.android.models.BootUser;
import kr.co.bootpay.android.models.Payload;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildMgmt extends AppCompatActivity {

    private LinearLayout childContainer; // 아동 정보를 표시할 레이아웃
    private Long sponsorID;
    private Long childId;
    private String sponsorName;
    private String sponsorAddress;
    private String sponsorEmail;
    private String sponsorNumber;
    private int inputPrice;  // 사용자가 입력한 금액 저장
    private RecyclerView recyclerView;
    private RecentOrderAdapter recentOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_mgmt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        childContainer = findViewById(R.id.childContainer);

        recyclerView = findViewById(R.id.recentOrdersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sponsorID = getSubFromToken();

        fetchChildrenData(sponsorID);
    }

    private void fetchChildrenData(Long sponsorId) {
        SponsorChildManagementApiService service = SponsorChildManagementRetrofitClient.getRetrofitInstance(this).create(SponsorChildManagementApiService.class);

        Call<List<ChildResponse>> call = service.getChildren(sponsorId);
        call.enqueue(new Callback<List<ChildResponse>>() {
            @Override
            public void onResponse(Call<List<ChildResponse>> call, Response<List<ChildResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChildResponse> children = response.body();

                    // 첫 번째 아동의 ID를 설정
                    if (!children.isEmpty()) {
                        childId = children.get(0).getId(); // 첫 번째 아동 ID를 childId로 설정
                    }

                    displayChildren(children);

                    // childId가 설정된 후에 loadOrders 호출
                    if (childId != null) {
                        loadOrders(childId);
                    } else {
                        Toast.makeText(ChildMgmt.this, "아동 ID가 설정되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChildMgmt.this, "아동 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChildResponse>> call, Throwable t) {
                Toast.makeText(ChildMgmt.this, "API 호출 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage(), t);
            }
        });
    }

    private void loadOrders(Long sponsorID) {
        OrderApiService service = OrderRetrofitClient.getRetrofitInstance(this).create(OrderApiService.class);

        Call<List<OrderResponse>> call = service.getOrderByChildId(sponsorID);
        call.enqueue(new Callback<List<OrderResponse>>() {
            @Override
            public void onResponse(Call<List<OrderResponse>> call, Response<List<OrderResponse>> response) {
                if(response.isSuccessful() && response.body() != null){
                    recentOrderAdapter = new RecentOrderAdapter(response.body());
                    recyclerView.setAdapter(recentOrderAdapter);
                }else{
                    Toast.makeText(getApplicationContext(), "주문내역이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<OrderResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayChildren(List<ChildResponse> children) {
        childContainer.removeAllViews();

        for (ChildResponse child : children) {
            View childView = LayoutInflater.from(this).inflate(R.layout.child_mgmt_list, childContainer, false);

            TextView childName = childView.findViewById(R.id.child_name);
            TextView childEmail = childView.findViewById(R.id.child_email);
            TextView childPhone = childView.findViewById(R.id.child_phone);
            TextView childAddress = childView.findViewById(R.id.child_address);
            TextView childPoint = childView.findViewById(R.id.child_points_value);
            Button deleteButton = childView.findViewById(R.id.delete_button);
            Button giveButton = childView.findViewById(R.id.give_button);

            childName.setText(child.getChildName());
            childEmail.setText(child.getChildEmail());
            childPhone.setText(child.getChildNumber());
            childAddress.setText(child.getChildAddress());
            childPoint.setText(String.valueOf(child.getChildAmount()));

            deleteButton.setOnClickListener(v -> {
                SponsorChildManagementApiService service = SponsorChildManagementRetrofitClient.getRetrofitInstance(this).create(SponsorChildManagementApiService.class);
                Call<SponsorDTO> call = service.deleteChildFromSponsor(sponsorID, child.getId());

                call.enqueue(new Callback<SponsorDTO>() {
                    @Override
                    public void onResponse(Call<SponsorDTO> call, Response<SponsorDTO> response) {
                        if (response.isSuccessful()) {
                            childContainer.removeView(childView);
                            Toast.makeText(getApplicationContext(), "Child deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to delete child", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SponsorDTO> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            });

            giveButton.setOnClickListener(v -> showCustomDialog());

            childContainer.addView(childView);
        }
    }

    private Long getSubFromToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("jwt_token", null);

        if (token != null) {
            JWT jwt = new JWT(token);
            Claim subClaim = jwt.getClaim("sub");
            return subClaim.asLong();
        }

        return null;
    }

    private void PaymentTest(String sponsorName, String sponsorEmail, String sponsorAddress, String sponsorNumber, int price) {
        BootUser user = new BootUser()
                .setUsername(sponsorName)
                .setEmail(sponsorEmail)
                .setAddr(sponsorAddress)
                .setPhone(sponsorNumber);

        BootExtra extra = new BootExtra()
                .setCardQuota("0,2,3");

        List<BootItem> items = new ArrayList<>();
        BootItem item1 = new BootItem().setName("후원금").setId("ITEM_CODE_MOUSE").setQty(1).setPrice((double) price);
        items.add(item1);

        Payload payload = new Payload();
        payload.setApplicationId("671016a286fd08d2213fc484")
                .setOrderName("부트페이 결제테스트")
                .setPg("KCP")
                .setMethod("카드")
                .setOrderId("1234")
                .setPrice((double) price)
                .setUser(user)
                .setExtra(extra)
                .setItems(items);

        Bootpay.init(getSupportFragmentManager())
                .setPayload(payload)
                .setEventListener(new BootpayEventListener() {
                    @Override
                    public void onCancel(String data) {
                        Log.d("bootpay", "cancel: " + data);
                        Toast.makeText(ChildMgmt.this, "취소", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String data) {
                        Log.d("bootpay", "error: " + data);
                        Toast.makeText(ChildMgmt.this, "에러", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClose() {
                        Log.d("bootpay", "close ");
                        Toast.makeText(ChildMgmt.this, "닫기", Toast.LENGTH_SHORT).show();
                        Bootpay.removePaymentWindow();
                    }

                    @Override
                    public void onIssued(String data) {
                        Log.d("bootpay", "issued: " + data);
                        Toast.makeText(ChildMgmt.this, "이슈", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public boolean onConfirm(String data) {
                        Log.d("bootpay", "confirm: " + data);
                        Toast.makeText(ChildMgmt.this, "확인", Toast.LENGTH_SHORT).show();
                        sponToChild();
                        return true;
                    }

                    @Override
                    public void onDone(String data) {
                        Log.d("done", data);
                        Toast.makeText(ChildMgmt.this, "끝", Toast.LENGTH_SHORT).show();
                    }
                }).requestPayment();
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sponsor_spon_point, null);
        builder.setView(dialogView);

        EditText inputField = dialogView.findViewById(R.id.inputField);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        AlertDialog dialog = builder.create();

        btnConfirm.setOnClickListener(v -> {
            String inputText = inputField.getText().toString();
            if (!inputText.isEmpty()) {
                inputPrice = Integer.parseInt(inputText);
                getMyInfoAndProceedToPayment();
                dialog.dismiss();
            } else {
                Toast.makeText(ChildMgmt.this, "값을 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void getMyInfoAndProceedToPayment() {
        SponsorChildManagementApiService service = SponsorChildManagementRetrofitClient.getRetrofitInstance(this).create(SponsorChildManagementApiService.class);
        Call<SponsorDTO> call = service.getSponsorInfo(sponsorID);

        call.enqueue(new Callback<SponsorDTO>() {
            @Override
            public void onResponse(Call<SponsorDTO> call, Response<SponsorDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SponsorDTO sponsorDTO = response.body();
                    sponsorName = sponsorDTO.getSponsorName();
                    sponsorAddress = sponsorDTO.getSponsorAddress();
                    sponsorNumber = sponsorDTO.getSponsorNumber();
                    sponsorEmail = sponsorDTO.getSponsorEmail();
                    PaymentTest(sponsorName, sponsorEmail, sponsorAddress, sponsorNumber, inputPrice);
                } else {
                    Toast.makeText(getApplicationContext(), "정보 조회 불가", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SponsorDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sponToChild() {
        SponsorChildManagementApiService service = SponsorChildManagementRetrofitClient.getRetrofitInstance(this).create(SponsorChildManagementApiService.class);
        Call<String> call = service.updateAmount(sponsorID, inputPrice);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "후원 완료", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "후원 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
