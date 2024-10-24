package com.eatda.president.Restaurant.form;

public class RestaurantRequest {
    private Long id;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantNumber;
    private String restaurantBody;
    private Long presidentId;

    // 기본 생성자
    public RestaurantRequest() {
    }

    // 생성자 추가
    public RestaurantRequest(String restaurantName, String restaurantAddress, String restaurantNumber, String restaurantBody, Long presidentId) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantNumber = restaurantNumber;
        this.restaurantBody = restaurantBody;
        this.presidentId = presidentId;
    }

    // Getter methods
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

    public Long getPresidentId() {
        return this.presidentId;
    }
}
