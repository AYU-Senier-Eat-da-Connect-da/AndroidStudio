package com.eatda.president.Restaurant.form;

public class RestaurantRequest {
    private Long id;
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantNumber;
    private String restaurantBody;
    private String restaurantCategory;
    private Long presidentId;

    public RestaurantRequest() {
    }

    public RestaurantRequest(String restaurantName, String restaurantAddress, String restaurantNumber, String restaurantBody, String restaurantCategory ,Long presidentId) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantNumber = restaurantNumber;
        this.restaurantBody = restaurantBody;
        this.restaurantCategory = restaurantCategory;
        this.presidentId = presidentId;
    }

    public RestaurantRequest modifyRestaurant(String restaurantName, String restaurantAddress, String restaurantNumber, String restaurantBody, String restaurantCategory){
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantNumber = restaurantNumber;
        this.restaurantBody = restaurantBody;
        this.restaurantCategory = restaurantCategory;
        return this;
    }

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

    public String getRestaurantCategory(){
        return this.restaurantCategory;
    }

    public Long getPresidentId() {
        return this.presidentId;
    }
}
