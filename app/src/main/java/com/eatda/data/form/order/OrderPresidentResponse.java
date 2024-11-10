package com.eatda.data.form.order;

import java.util.List;

public class OrderPresidentResponse {
    private Long id;
    private Long restaurantId;
    private String restaurantName;
    private Long childId;
    private Child child;
    private List<MenuOrder> menuOrders;
    private String orderTime; // ISO-8601 형식 문자열
    private int price;

    public Long getId() {
        return id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public Long getChildId() {
        return childId;
    }

    public Child getChild() {
        return child;
    }

    public List<MenuOrder> getMenuOrders() {
        return menuOrders;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public int getPrice() {
        return price;
    }

    public static class MenuOrder {
        private Long menuId;
        private int menuCount;
        private String menuName;
        private String menuBody;

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

    public static class Child {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String address;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }
    }
}
