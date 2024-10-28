package com.eatda.data.form.menu;

public class MenuResponse {
    private Long id;
    private String menuName;
    private String menuBody;
    private int price;
    private boolean menuStatus;
    private Long restaurantId;

    public Long getId(){
        return this.id;
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
}
