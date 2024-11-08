package com.eatda.data.form.order;

public class MenuOrder {
    private Long menuId;
    private int menuCount;
    private String menuName;
    private String menuBody;

    public MenuOrder(Long menuId, int menuCount) {
        this.menuId = menuId;
        this.menuCount = menuCount;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getMenuCount() {
        return menuCount;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getMenuBody() {
        return menuBody;
    }
}
