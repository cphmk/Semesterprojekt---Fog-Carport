package app.entities;

public class Order_item {
    private int order_item_id;
    private int quantity;
    private String description;
    private int variant_id;
    private int order_id;
    private double price;


    private String name;

    private String unit;

    private int length;


    public Order_item(int order_item_id, int quantity, String description, int variant_id, int order_id, double price) {
        this.order_item_id = order_item_id;
        this.quantity = quantity;
        this.description = description;
        this.variant_id = variant_id;
        this.order_id = order_id;
        this.price = price;
    }

    public Order_item(String name, int length, int quantity, String unit, String description) {
        this.name = name;
        this.length = length;
        this.quantity = quantity;
        this.unit = unit;
        this.description = description;
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

    public int getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(int variant_id) {
        this.variant_id = variant_id;
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

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public int getLength() {
        return length;
    }
}