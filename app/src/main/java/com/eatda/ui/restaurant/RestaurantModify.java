package com.eatda.ui.restaurant;

import android.content.Intent;
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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.eatda.R;
import com.eatda.data.api.restaurant.PresidentManageRestaurantApiService;
import com.eatda.data.api.president.PresidentRetrofitClient;
import com.eatda.data.form.restaurant.RestaurantRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantModify extends AppCompatActivity {

    private Long presidentId;
    private Uri photoUri;
    private static final int GALLERY_REQUEST_CODE = 1;
    private ImageView photoPreview;


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

        photoPreview = findViewById(R.id.photo_preview);

        Button btn_select_photo = findViewById(R.id.select_photo_button);
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
        presidentId = intent.getLongExtra("presidentId", -1);

        // EditText에 값 설정
        restaurantNameEditText.setText(restaurantName);
        restaurantAddressEditText.setText(restaurantAddress);
        restaurantNumberEditText.setText(restaurantNumber);
        restaurantBodyEditText.setText(restaurantBody);
        getPhoto();

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

        btn_select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
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
                    uploadImageToFirebase(photoUri);
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

    private void getPhoto() {
        // Firebase Storage 참조 가져오기
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // 이미지 경로 설정
        StorageReference imageRef = storageRef.child("restaurant/restaurant_" + presidentId + ".jpg");

        // 이미지 다운로드 URL 가져오기
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            ImageView imageView = findViewById(R.id.photo_preview);  // ImageView 설정
            Glide.with(getApplicationContext())
                    .load(uri)  // Glide로 다운로드 URL 로드
                    .into(imageView);
        }).addOnFailureListener(e -> {
            Toast.makeText(RestaurantModify.this, "이미지 로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Firebase Storage", "이미지 로드 오류: " + e.getMessage());
        });
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