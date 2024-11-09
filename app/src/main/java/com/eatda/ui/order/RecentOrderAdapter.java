package com.eatda.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eatda.R;
import com.eatda.data.form.order.MenuOrder;
import com.eatda.data.form.order.OrderResponse;
import com.eatda.ui.review.AddReview;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import android.os.Handler;

public class RecentOrderAdapter extends RecyclerView.Adapter<RecentOrderAdapter.RecentOrderViewHolder> {
    private List<OrderResponse> orderList;

    public RecentOrderAdapter(List<OrderResponse> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public RecentOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsor_child_buy_list_litem, parent, false);
        return new RecentOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentOrderViewHolder holder, int position) {
        OrderResponse order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class RecentOrderViewHolder extends RecyclerView.ViewHolder {
        private TextView restaurantNameTextView;
        private TextView orderTimeTextView;
        private TextView priceTextView;
        private LinearLayout menuContainer;
        private Handler handler;

        public RecentOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantNameTextView = itemView.findViewById(R.id.restaurant_name);
            orderTimeTextView = itemView.findViewById(R.id.order_time);
            priceTextView = itemView.findViewById(R.id.price);
            menuContainer = itemView.findViewById(R.id.menu_container);
            handler = new Handler(Looper.getMainLooper());
        }

        public void bind(OrderResponse order) {
            // DateTimeFormatter 생성
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");

            // orderTime을 지정된 형식으로 포맷
            String formattedOrderTime = order.getOrderTime().format(formatter);

            // TextView에 포맷된 시간 설정
            orderTimeTextView.setText(formattedOrderTime);

            // 나머지 데이터 바인딩
            restaurantNameTextView.setText(order.getRestaurantName());
            priceTextView.setText(String.valueOf(order.getPrice()) + " 원");

            menuContainer.removeAllViews();
            for (MenuOrder menuOrder : order.getMenuOrders()) {
                TextView menuTextView = new TextView(itemView.getContext());
                menuTextView.setText(menuOrder.getMenuName() + "   " + menuOrder.getMenuBody());
                menuContainer.addView(menuTextView);
            }

        }
    }
}
