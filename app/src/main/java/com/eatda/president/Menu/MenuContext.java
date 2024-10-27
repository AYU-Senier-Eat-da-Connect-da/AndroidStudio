package com.eatda.president.Menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.eatda.R;
import com.eatda.president.Menu.form.MenuResponse;

import java.util.List;

public class MenuContext extends Fragment {

    private int sum=0;
    private TextView currentSum;

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
                            checkBox.setChecked(!checkBox.isChecked()); // 현재 상태 반전
                            if (checkBox.isChecked()) {
                                cardView.setCardBackgroundColor(getResources().getColor(R.color.baseColor));
                                sum += menu.getPrice();
                                currentSum.setText(String.format("%,d 원", sum));
                            } else {
                                cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                                sum -= menu.getPrice();
                                currentSum.setText(String.format("%,d 원", sum));
                            }
                        });


                // menu_container에 추가
                menuContainer.addView(menuView);
            }
        }
        return v;
    }
}
