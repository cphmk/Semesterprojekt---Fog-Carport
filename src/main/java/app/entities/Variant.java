package app.entities;

public class Variant {

    private int variant_id;

    private int length;

    private Material material;



    public Variant(int variant_id, int length, Material material) {
        this.variant_id = variant_id;
        this.length = length;
        this.material = material;
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


}
