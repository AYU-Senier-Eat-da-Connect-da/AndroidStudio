package com.eatda.data.form.menu;

public class MenuRequest {
    private Long id;
    private String menuName;
    private String menuBody;
    private int price;
    private boolean menuStatus;
    private Long restaurantId;

    public MenuRequest() {}


    public MenuRequest(String menuName, String menuBody, int price, boolean menuStatus, Long restaurantId){
        this.menuName = menuName;
        this.menuBody = menuBody;
        this.price = price;
        this.menuStatus = menuStatus;
        this.restaurantId = restaurantId;
    }

    public MenuRequest modifyMenu(String menuName, String menuBody, boolean menuStatus, int price){
        this.menuName = menuName;
        this.menuBody = menuBody;
        this.price = price;
        this.menuStatus = menuStatus;
        return this;
    }

    public String getMenuName(){
        return this.menuName;
    }

    public String getMenuBody(){
        return this.menuBody;
    }

    public int getPrice(){
        return this.price;
    }

    public boolean getMenuStatus(){
        return this.menuStatus;
    }

    public Long getRestaurantId(){
        return this.restaurantId;
    }
}
