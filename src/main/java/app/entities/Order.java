package app.entities;

import java.util.Date;

public class Order {
    private int order_id;
    private Date date;
    private String status;
    private int user_id;
    private int order_item_id;

    public Order(int order_id, Date date, String status, int user_id, int order_item_id) {
        this.order_id = order_id;
        this.date = date;
        this.status = status;
        this.user_id = user_id;
        this.order_item_id = order_item_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }
}
