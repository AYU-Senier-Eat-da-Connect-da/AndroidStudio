package com.eatda.data.form.order;

import java.util.List;

public class OrderRequest {

    private Long childId;
    private Long restaurantId;
    private List<MenuOrder> menuOrders;
    private int totalsum;

    public OrderRequest(Long childId, Long restaurantId, List<MenuOrder> menuOrders, int totalsum) {
        this.childId = childId;
        this.restaurantId = restaurantId;
        this.menuOrders = menuOrders;
        this.totalsum = totalsum;
    }

    public Long getChildId() {
        return childId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public List<MenuOrder> getMenuOrders() {
        return menuOrders;
    }

    public int getTotalsum() {
        return totalsum;
    }
}
