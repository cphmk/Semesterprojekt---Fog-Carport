package app.entities;

import java.util.ArrayList;
import java.util.Date;

public class Order_item {
    private int order_item_id;
    private int quantity;
    private String description;
    private int material_id;
    private int order_id;
    private double price;


    public Order_item(int order_item_id, int quantity, String description, int material_id, int order_id, double price) {
        this.order_item_id = order_item_id;
        this.quantity = quantity;
        this.description = description;
        this.material_id = material_id;
        this.order_id = order_id;
        this.price = price;
    }

    public int getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(int material_id) {
        this.material_id = material_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice()
    {
      return price;
    }

    public void setPrice(double price)
    {
       this.price = price;
    }
}