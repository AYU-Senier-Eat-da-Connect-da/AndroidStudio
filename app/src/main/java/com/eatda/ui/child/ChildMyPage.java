package com.eatda.ui.child;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.eatda.R;
import com.eatda.data.api.child.ChildApiService;
import com.eatda.data.api.child.ChildRetrofitClient;
import com.eatda.data.form.childManagement.ChildResponse;
import com.eatda.ui.childManagement.ChildMgmt;
import com.eatda.ui.review.AddReview;
import com.eatda.ui.review.ChildMyReviewList;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kr.co.bootpay.android.Bootpay;
import kr.co.bootpay.android.events.BootpayEventListener;
import kr.co.bootpay.android.models.BootExtra;
import kr.co.bootpay.android.models.BootItem;
import kr.co.bootpay.android.models.BootUser;
import kr.co.bootpay.android.models.Payload;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildMyPage extends AppCompatActivity {

    private Button btn_child_myReviewList;
    private Button btn_child_buyList;
    private Button btn_card_balance;
    private Button btn_child_help;
    private Button btn_child_addPoint;
    private Long childId;
    private String childName;
    private String childEmail;
    private String childNumber;
    private String childAddress;
    private int childAmount;
    private int inputPrice;


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

        childId = getSubFromToken();
        getChild(childId);

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

        btn_child_addPoint = findViewById(R.id.btn_addPoint);
        btn_child_addPoint.setOnClickListener(v -> showCustomDialog());
    }

    private void getChild(Long childId){
        ChildApiService service = ChildRetrofitClient.getRetrofitInstance(this).create(ChildApiService.class);
        Call<ChildResponse> call = service.getChildInfo(childId);

        call.enqueue(new Callback<ChildResponse>() {
            @Override
            public void onResponse(Call<ChildResponse> call, Response<ChildResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    ChildResponse child = response.body();
                    childName = child.getChildName();
                    childEmail = child.getChildEmail();
                    childNumber = child.getChildNumber();
                    childAddress = child.getChildAddress();
                    childAmount = child.getChildAmount();
                    displayInfo();
                }else{
                    Toast.makeText(getApplicationContext(), "정보 조회 불가", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChildResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayInfo(){
        TextView nameView = findViewById(R.id.tv_child_name_value);
        TextView addressView = findViewById(R.id.tv_child_address_value);
        TextView emailView = findViewById(R.id.tv_child_email_value);
        TextView phoneView = findViewById(R.id.tv_child_phone_value);
        TextView donationView = findViewById(R.id.tv_child_points_value);

        nameView.setText(childName != null ? childName : "정보 없음");
        addressView.setText(childAddress != null ? childAddress : "정보 없음");
        emailView.setText(childEmail != null ? childEmail : "정보 없음");
        phoneView.setText(childNumber != null ? childNumber : "정보 없음");

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedPrice = numberFormat.format(childAmount);
        donationView.setText(formattedPrice);
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

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.child_add_point, null);
        builder.setView(dialogView);

        EditText inputField = dialogView.findViewById(R.id.inputField);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        AlertDialog dialog = builder.create();

        btnConfirm.setOnClickListener(v -> {
            String inputText = inputField.getText().toString();
            if (!inputText.isEmpty()) {
                inputPrice = Integer.parseInt(inputText);
                PaymentTest(childName, childEmail, childAddress, childNumber, inputPrice);
                dialog.dismiss();
            } else {
                Toast.makeText(ChildMyPage.this, "값을 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void PaymentTest(String childName, String childEmail, String childAddress, String childNumber, int price) {
        BootUser user = new BootUser()
                .setUsername(childName)
                .setEmail(childEmail)
                .setAddr(childAddress)
                .setPhone(childNumber);

        BootExtra extra = new BootExtra()
                .setCardQuota("0,2,3");

        List<BootItem> items = new ArrayList<>();
        BootItem item1 = new BootItem().setName("충전금액").setId("ITEM_CODE_MOUSE").setQty(1).setPrice((double) price);
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
                        Toast.makeText(ChildMyPage.this, "취소", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String data) {
                        Log.d("bootpay", "error: " + data);
                        Toast.makeText(ChildMyPage.this, "에러", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClose() {
                        Log.d("bootpay", "close ");
                        Toast.makeText(ChildMyPage.this, "닫기", Toast.LENGTH_SHORT).show();
                        Bootpay.removePaymentWindow();
                    }

                    @Override
                    public void onIssued(String data) {
                        Log.d("bootpay", "issued: " + data);
                        Toast.makeText(ChildMyPage.this, "이슈", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public boolean onConfirm(String data) {
                        Log.d("bootpay", "confirm: " + data);
                        Toast.makeText(ChildMyPage.this, "확인", Toast.LENGTH_SHORT).show();
                        addPoint();
                        return true;
                    }

                    @Override
                    public void onDone(String data) {
                        Log.d("done", data);
                        Toast.makeText(ChildMyPage.this, "끝", Toast.LENGTH_SHORT).show();
                    }
                }).requestPayment();
    }

    private void addPoint(){
        ChildApiService service = ChildRetrofitClient.getRetrofitInstance(this).create(ChildApiService.class);
        Call<String> call = service.addPoint(childId, inputPrice);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "충전 완료", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "충전 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }
}