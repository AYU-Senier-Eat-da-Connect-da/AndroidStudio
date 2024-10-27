package com.eatda.president.Restaurant.form;

import com.eatda.president.Menu.form.MenuResponse;

import java.util.List;

public class RestaurantDetailResponse {
    private Long id;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantNumber;
    private String restaurantBody;
    private String restaurantCategory;
    private Long presidentId;
    private List<MenuResponse> menus;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public String getRestaurantAddress() { return restaurantAddress; }
    public void setRestaurantAddress(String restaurantAddress) { this.restaurantAddress = restaurantAddress; }

    public String getRestaurantNumber() { return restaurantNumber; }
    public void setRestaurantNumber(String restaurantNumber) { this.restaurantNumber = restaurantNumber; }

    public String getRestaurantBody() { return restaurantBody; }
    public void setRestaurantBody(String restaurantBody) { this.restaurantBody = restaurantBody; }

    public String getRestaurantCategory() { return restaurantCategory; }
    public void setRestaurantCategory(String restaurantCategory) { this.restaurantCategory = restaurantCategory; }

    public Long getPresidentId() { return presidentId; }
    public void setPresidentId(Long presidentId) { this.presidentId = presidentId; }

    public List<MenuResponse> getMenus() { return menus; }
    public void setMenus(List<MenuResponse> menus) { this.menus = menus; }
}
