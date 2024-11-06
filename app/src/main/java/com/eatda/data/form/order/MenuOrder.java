package com.eatda.data.form.order;

public class MenuOrder {
    private Long menuId;
    private int menuCount;

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
}
