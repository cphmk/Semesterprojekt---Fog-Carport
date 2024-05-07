package app.entities;

import java.util.Date;

public class Order {
    private int order_id;
    private Date date;
    private String status;
    private int user_id;
    private String comment;
    private int carport_id;


    public Order(int order_id, Date date, String status, int user_id, String comment, int carport_id) {
        this.order_id = order_id;
        this.date = date;
        this.status = status;
        this.user_id = user_id;
        this.comment = comment;
        this.carport_id = carport_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCarport_id() {
        return carport_id;
    }

    public void setCarport_id(int carport_id) {
        this.carport_id = carport_id;
    }
}
