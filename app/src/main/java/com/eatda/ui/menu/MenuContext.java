package com.eatda.ui.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.eatda.R;
import com.eatda.data.form.menu.MenuResponse;
import com.eatda.ui.restaurant.RestaurantsMgmt;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MenuContext extends Fragment {

    private int sum=0;
    private TextView currentSum;
    private Long menuId;
    private List<MenuResponse> selectMenu = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_menu_context, container, false);
        LinearLayout menuContainer = v.findViewById(R.id.menu_container); // layout에 정의된 메뉴 리스트 컨테이너

        currentSum = getActivity().findViewById(R.id.current_sum);

        // 전달받은 메뉴 리스트 가져오기
        List<MenuResponse> menus = (List<MenuResponse>) getArguments().getSerializable("menus");
        if (menus != null) {
            for (MenuResponse menu : menus) {
                View menuView = inflater.inflate(R.layout.restaurant_item_detail_menu, menuContainer, false);

                menuId = menu.getMenuId();
                CardView cardView = menuView.findViewById(R.id.menu_card_view);
                CheckBox checkBox = menuView.findViewById(R.id.menu_check_box);

                TextView menuName = menuView.findViewById(R.id.menu_name);
                TextView menuBody = menuView.findViewById(R.id.menu_body);
                TextView menuPrice = menuView.findViewById(R.id.menu_price);
                TextView menuStatus = menuView.findViewById(R.id.menu_status);

                menuName.setText(menu.getMenuName());
                menuBody.setText(menu.getMenuBody());
                menuPrice.setText(String.format("%,d 원", menu.getPrice()));
                menuStatus.setText(menu.getMenuStatus() ? "주문 가능" : "품절");

                cardView.setOnClickListener(view -> {
                    if (!menu.getMenuStatus()) {
                        Toast.makeText(getContext(), "품절된 메뉴입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    checkBox.setChecked(!checkBox.isChecked()); // 현재 상태 반전
                    if (checkBox.isChecked()) {
                        cardView.setCardBackgroundColor(getResources().getColor(R.color.baseColor));
                        sum += menu.getPrice();
                        currentSum.setText(String.format("%,d 원", sum));
                        selectMenu.add(menu);
                    } else {
                        cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                        sum -= menu.getPrice();
                        currentSum.setText(String.format("%,d 원", sum));
                        selectMenu.remove(menu);
                    }
                });


                getMenuPhoto(menuId, menuView);
                menuContainer.addView(menuView);
            }
        }
        return v;
    }

    public List<MenuResponse> getSelectedMenus() {
        return selectMenu;
    }

    public int getSum(){
        return sum;
    }

    private void getMenuPhoto(Long menuId, View menuView) {
        // Firebase Storage 참조 가져오기
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // 이미지 경로 설정
        StorageReference imageRef = storageRef.child("menu/menu_" + menuId + ".jpg");

        // 메뉴 항목의 ImageView 참조 가져오기
        ImageView imageView = menuView.findViewById(R.id.photo_preview);

        // 이미지 다운로드 URL 가져오기
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this)
                    .load(uri)
                    .into(imageView);
        }).addOnFailureListener(e -> {
            Log.e("Firebase Storage", "음식 이미지 로드 오류: " + e.getMessage());
        });
    }
}
