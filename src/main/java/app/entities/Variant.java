package app.entities;

public class Variant {

    private int variant_id;

    private int length;

    private Material material;

    private int order_item_id;

    public Variant(int variant_id, int length, Material material, int order_item_id) {
        this.variant_id = variant_id;
        this.length = length;
        this.material = material;
        this.order_item_id = order_item_id;
    }

    public int getVariant_id() {
        return variant_id;
    }

    public int getLength() {
        return length;
    }

    public Material getMaterial() {
        return material;
    }

    public int getOrder_item_id() {
        return order_item_id;
    }
}
