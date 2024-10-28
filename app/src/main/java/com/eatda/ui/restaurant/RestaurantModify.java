package com.eatda.ui.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eatda.R;
import com.eatda.data.api.restaurant.PresidentManageRestaurantApiService;
import com.eatda.data.api.president.PresidentRetrofitClient;
import com.eatda.data.form.restaurant.RestaurantRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantModify extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_modify);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText restaurantNameEditText = findViewById(R.id.restaurant_name);
        EditText restaurantAddressEditText = findViewById(R.id.restaurant_address);
        EditText restaurantNumberEditText = findViewById(R.id.restaurant_number);
        EditText restaurantBodyEditText = findViewById(R.id.restaurant_body);
        Spinner categorySpinner = findViewById(R.id.category_spinner);
        Button btn_restaurantModify = findViewById(R.id.modify_button);

        // 전달받은 데이터 가져오기
        Intent intent = getIntent();
        String restaurantName = intent.getStringExtra("restaurantName");
        String restaurantAddress = intent.getStringExtra("restaurantAddress");
        String restaurantNumber = intent.getStringExtra("restaurantNumber");
        String restaurantBody = intent.getStringExtra("restaurantBody");
        String restaurantCategory = intent.getStringExtra("restaurantCategory");

        // EditText에 값 설정
        restaurantNameEditText.setText(restaurantName);
        restaurantAddressEditText.setText(restaurantAddress);
        restaurantNumberEditText.setText(restaurantNumber);
        restaurantBodyEditText.setText(restaurantBody);

        // Spinner에 기본 선택 값 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // restaurantCategory 값을 기본 선택값으로 설정
        if (restaurantCategory != null) {
            int spinnerPosition = adapter.getPosition(restaurantCategory);
            categorySpinner.setSelection(spinnerPosition);
        }

        // Spinner의 선택된 값을 저장할 변수
        final String[] selectedRestaurantCategory = {restaurantCategory};

        // Spinner의 선택 리스너 설정 (한 번만 설정)
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRestaurantCategory[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 선택하지 않은 경우 기본값 처리 (필요 시)
            }
        });

        btn_restaurantModify.setOnClickListener(v -> {
            String restaurantNameInput = restaurantNameEditText.getText().toString();
            String restaurantAddressInput = restaurantAddressEditText.getText().toString();
            String restaurantNumberInput = restaurantNumberEditText.getText().toString();
            String restaurantBodyInput = restaurantBodyEditText.getText().toString();

            if(restaurantNameInput.isEmpty()){
                showAlertDialog("식당 이름 입력", "식당 이름을 입력하세요.");
            } else if(restaurantNumberInput.isEmpty()){
                showAlertDialog("전화번호 입력", "전화번호를 입력하세요.");
            } else if(restaurantAddressInput.isEmpty()){
                showAlertDialog("주소 입력","주소를 입력하세요.");
            } else if(restaurantBodyInput.isEmpty()){
                showAlertDialog("상세 정보 입력", "상세 정보를 입력하세요.");
            } else {
                // 수정 요청 호출
                updateRestaurant(restaurantNameInput, restaurantNumberInput, restaurantAddressInput, restaurantBodyInput, selectedRestaurantCategory[0]);
            }
        });
    }

    private void updateRestaurant(String restaurantName, String restaurantNumber, String restaurantAddress, String restaurantBody, String selectrestaurantCategory) {
        Intent intent = getIntent();
        long restaurantId = intent.getLongExtra("restaurantId", -1);

        PresidentManageRestaurantApiService service = PresidentRetrofitClient.getRetrofitInstance(this).create(PresidentManageRestaurantApiService.class);
        RestaurantRequest request = new RestaurantRequest().modifyRestaurant(restaurantName, restaurantAddress, restaurantNumber, restaurantBody, selectrestaurantCategory);

        Call<RestaurantRequest> call = service.updateRestaurant(restaurantId, request);
        call.enqueue(new Callback<RestaurantRequest>() {
            @Override
            public void onResponse(Call<RestaurantRequest> call, Response<RestaurantRequest> response) {
                if(response.isSuccessful() && response.body() != null){
                    modifyShowAlertDialog("수정 완료", "가게 수정이 완료되었습니다.",true);
                }else{
                    modifyShowAlertDialog("수정 실패", "가게 수정이 실패했습니다.",true);
                }
            }

            @Override
            public void onFailure(Call<RestaurantRequest> call, Throwable t) {
                showAlertDialog("수정 실패", "네트워크 오류");
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantModify.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void modifyShowAlertDialog(String title, String message, boolean shouldStartIntent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantModify.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> {
            dialog.dismiss();
            if (shouldStartIntent) {
                Intent intent = new Intent(RestaurantModify.this, RestaurantsMgmt.class);
                startActivity(intent);
            }
        });
        builder.show();
    }
}