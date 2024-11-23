package com.eatda.ui.restaurant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.eatda.R;
import com.eatda.data.api.restaurant.PresidentManageRestaurantApiService;
import com.eatda.data.api.president.PresidentRetrofitClient;
import com.eatda.data.form.restaurant.RestaurantRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantAdd extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;
    private Long presidentId;
    private String restaurantCategory;
    private ImageView photoPreview;
    private Long restaurantId;
    private Uri imageUri;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_add);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        photoPreview = findViewById(R.id.photo_preview);

        Spinner categorySpinner = findViewById(R.id.category_spinner);

        //List<String> categories = Arrays.asList("한식", "중식", "일식", "양식", "돈까스","회","피자","족발","보쌈","치킨","햄버거","떡볶이","카페","디저트");
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Spinner 선택 이벤트
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                restaurantCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 선택하지 않은 경우 기본값 처리 (필요 시)
            }
        });

        presidentId = getSubFromToken();
        Log.d("sponsorID :", "사장ID" + presidentId);

        Button btn_selectImage = findViewById(R.id.select_photo_button);
        Button btn_addRestaurant = findViewById(R.id.register_button);
        EditText text_restaurantName = findViewById(R.id.restaurant_name);
        EditText text_restaurantAddress = findViewById(R.id.restaurant_address);
        EditText text_restaurantNumber = findViewById(R.id.restaurant_number);
        EditText text_restaurantBody = findViewById(R.id.restaurant_body);

        btn_selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });
        
        btn_addRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String restaurantName = text_restaurantName.getText().toString();
                String restaurantAddress = text_restaurantAddress.getText().toString();
                String restaurantNumber = text_restaurantNumber.getText().toString();
                String restaurantBody = text_restaurantBody.getText().toString();
                
                if(restaurantName.isEmpty()){
                    showAlertDialog("식당 이름 입력", "식당 이름을 입력하세요.");
                }else if(restaurantAddress.isEmpty()){
                    showAlertDialog("주소 입력", "주소를 입력하세요.");
                }else if(restaurantNumber.isEmpty()){
                    showAlertDialog("전화번호 입력","전화번호를 입력하세요.");
                }else if(restaurantBody.isEmpty()){
                    showAlertDialog("상세 정보 입력","상세 정보를 입력해주세요.");
                }else{
                    addRestaurant(restaurantName,restaurantAddress,restaurantNumber,restaurantBody, restaurantCategory);
                }
            }
        });

    }

    private void addRestaurant(String restaurantName, String restaurantAddress, String restaurantNumber, String restaurantBody, String restaurantCategory) {
        PresidentManageRestaurantApiService service = PresidentRetrofitClient.getRetrofitInstance(this).create(PresidentManageRestaurantApiService.class);
        RestaurantRequest request = new RestaurantRequest(restaurantName, restaurantAddress, restaurantNumber, restaurantBody, restaurantCategory ,presidentId);

        Call<RestaurantRequest> call = service.addRestaurant(request);
        call.enqueue(new Callback<RestaurantRequest>() {
            @Override
            public void onResponse(Call<RestaurantRequest> call, Response<RestaurantRequest> response) {
                Log.d("Retrofit Response", "Code: " + response.code() + ", Body: " + response.body());
                if(response.isSuccessful() && response.body() != null){
                    restaurantId = response.body().getId();
                    uploadImageToFirebase(photoUri);
                    addShowAlertDialog("등록 완료", "식당이 등록 완료되었습니다.",true);
                }else{
                    addShowAlertDialog("등록 실패", "등록에 실패하였습니다.",false);
                }
            }

            @Override
            public void onFailure(Call<RestaurantRequest> call, Throwable t) {
                showAlertDialog("등록 실패", "등록에 실패하였습니다.2");
                Log.e("Retrofit Error", "Failure: " + t.getMessage());
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

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantAdd.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void addShowAlertDialog(String title, String message, boolean shouldStartIntent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantAdd.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> {
            dialog.dismiss();
            if (shouldStartIntent) {
                Intent intent = new Intent(RestaurantAdd.this, RestaurantsMgmt.class);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photoUri = data.getData();
            photoPreview.setImageURI(photoUri);  // 이미지 미리보기 설정
        }
    }


    private void uploadImageToFirebase(Uri fileUri) {
        if (presidentId == null) {
            showAlertDialog("오류", "사장 ID가 설정되지 않았습니다.");
            return;
        }

        // Firebase Storage 참조 가져오기
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference fileRef = storageRef.child("restaurant/restaurant_" + presidentId + ".jpg");

        // 파일 업로드 시작
        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        Log.d("Firebase Storage", "Image URL: " + downloadUrl);
                        showAlertDialog("업로드 완료", "이미지가 성공적으로 업로드되었습니다.");
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase Storage", "업로드 실패: " + e.getMessage());
                    showAlertDialog("업로드 실패", "이미지 업로드에 실패했습니다.");
                })
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    Log.d("Firebase Storage", "업로드 진행: " + progress + "%");
                });
    }



}