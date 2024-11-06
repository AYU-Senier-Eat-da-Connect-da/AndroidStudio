package com.eatda.data.form.order;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private Long restaurantId;
    private Long childId;
    private LocalDateTime orderTime;
    private int price;
    private List<MenuOrder> menuOrders;

    public Long getId() {
        return id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public Long getChildId() {
        return childId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public int getPrice() {
        return price;
    }

    public List<MenuOrder> getMenuOrders() {
        return menuOrders;
    }
}
