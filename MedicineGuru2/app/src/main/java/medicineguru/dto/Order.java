package medicineguru.dto;

import java.util.List;

/**
 * Created by Brinder Saini on 16/03/2018.
 */

public class Order {
    private String orderId;
    private List<OrderItem> orders;
    private String userId;
    private boolean recievedPayment;
    private String tolalpayment;
    private String paymentMethod;
    public Order() {
    }

    public Order(List<OrderItem> orders, String userId, boolean recievedPayment, String tolalpayment,String paymentMethod) {
        this.orders = orders;
        this.userId = userId;
        this.recievedPayment = recievedPayment;
        this.tolalpayment = tolalpayment;
        this.paymentMethod=paymentMethod;
    }

    public String getTolalpayment() {
        return tolalpayment;
    }
    public boolean isRecievedPayment() {
        return recievedPayment;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setRecievedPayment(boolean recievedPayment) {
        this.recievedPayment = recievedPayment;
    }
    public void setTolalpayment(String tolalpayment) {
        this.tolalpayment = tolalpayment;
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
