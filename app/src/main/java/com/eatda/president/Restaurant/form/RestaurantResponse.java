package com.eatda.president.Restaurant.form;

public class RestaurantResponse {
    private Long id;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantNumber;
    private String restaurantBody;

    public Long getId() {
        return this.id;
    }

    public String getRestaurantName() {
        return this.restaurantName;
    }

    public String getRestaurantAddress() {
        return this.restaurantAddress;
    }

    public String getRestaurantNumber() {
        return this.restaurantNumber;
    }

    public String getRestaurantBody() {
        return this.restaurantBody;
    }
}
