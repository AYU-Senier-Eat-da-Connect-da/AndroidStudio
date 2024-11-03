package com.eatda.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.eatda.R;
import com.eatda.data.api.menu.PresidentManageMenuApiService;
import com.eatda.data.form.menu.MenuRequest;
import com.eatda.data.api.president.PresidentRetrofitClient;
import com.eatda.ui.restaurant.RestaurantsMgmt;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuAdd extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;
    private Long presidentId;
    private Uri photoUri;
    private ImageView photoPreview;
    private Long menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presidentId = getSubFromToken();
        Button btn_addRestaurant = findViewById(R.id.register_button);
        Button btn_selectImage = findViewById(R.id.select_photo_button);
        EditText text_menuName = findViewById(R.id.menu_name);
        EditText text_menuBody = findViewById(R.id.menu_body);
        EditText text_price = findViewById(R.id.menu_price);
        RadioGroup radioGroup = findViewById(R.id.menu_status_group);
        RadioButton Rbtn_true = findViewById(R.id.menu_status_available);
        RadioButton Rbtn_false = findViewById(R.id.menu_status_sold_out);
        photoPreview = findViewById(R.id.photo_preview);

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
                String menuName = text_menuName.getText().toString();
                String menuBody = text_menuBody.getText().toString();
                String price = text_price.getText().toString();
                int radioId = radioGroup.getCheckedRadioButtonId();
                boolean menuStatus;

                if(radioId == Rbtn_true.getId()){
                    menuStatus = true;
                }else{
                    menuStatus = false;
                }

                if(menuName.isEmpty()){
                    showAlertDialog("메뉴 이름 입력", "메뉴 이름을 입력하세요.");
                }else if(menuBody.isEmpty()){
                    showAlertDialog("메뉴 설명 입력", "메뉴 설명을 입력하세요.");
                }else if(price.isEmpty()){
                    showAlertDialog("메뉴 가격 입력", "메뉴 가격을 입력하세요.");
                }else if(radioId == 0){
                    showAlertDialog("메뉴 상태 선택","메뉴 상태를 확인해주세요.");
                }else{
                    addMenu(menuName,menuBody,menuStatus,price);
                }
            }
        });
    }

    private void addMenu(String menuName, String menuBody, boolean menuStatus, String price) {
        PresidentManageMenuApiService service = PresidentRetrofitClient.getRetrofitInstance(this).create(PresidentManageMenuApiService.class);
        MenuRequest request = new MenuRequest(menuName, menuBody, Integer.parseInt(price), menuStatus, presidentId);

        Call<MenuRequest> call = service.addMenu(request,presidentId);
        call.enqueue(new Callback<MenuRequest>() {
            @Override
            public void onResponse(Call<MenuRequest> call, Response<MenuRequest> response) {
                if(response.isSuccessful() && response.body() != null){
                    addShowAlertDialog("등록 완료", "메뉴 등록 완료되었습니다.",true);
                    menuId = response.body().getMenuId();
                    uploadImageToFirebase(photoUri);
                }else{
                    addShowAlertDialog("등록 실패", "등록에 실패하였습니다.",false);
                }
            }

            @Override
            public void onFailure(Call<MenuRequest> call, Throwable t) {
                showAlertDialog("등록 실패", "네트워크 오류");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuAdd.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void addShowAlertDialog(String title, String message, boolean shouldStartIntent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuAdd.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("확인", (dialog, which) -> {
            dialog.dismiss();
            if (shouldStartIntent) {
                Intent intent = new Intent(MenuAdd.this, RestaurantsMgmt.class);
                startActivity(intent);
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
        if (menuId == null) {
            showAlertDialog("오류", "메뉴 ID가 설정되지 않았습니다.");
            return;
        }

        // Firebase Storage 참조 가져오기
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference fileRef = storageRef.child("menu/menu_" + menuId + ".jpg");

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