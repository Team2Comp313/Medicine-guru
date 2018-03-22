package medicineguru.dto;

import java.util.List;

/**
 * Created by Brinder Saini on 16/03/2018.
 */

public class Order {
    private String orderId;
    private List<OrderItem> orders;
    private String userId;

    public Order() {
    }

    public Order(String orderId, List<OrderItem> orders, String userId) {
        this.orderId = orderId;
        this.orders = orders;
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderItem> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItem> orders) {
        this.orders = orders;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
