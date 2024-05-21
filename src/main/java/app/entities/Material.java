package app.entities;

public class Material {

    private int material_id;
    private String name;
    private String unit;
    private int price;

    public Material(int material_id, String name, String unit, int price) {
        this.material_id = material_id;
        this.name = name;
        this.unit = unit;
        this.price = price;
    }

    public int getMaterial_id() {
        return material_id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public int getPrice() {
        return price;
    }

    // Tilføj en metode til at hente material_id direkte
    public int getMaterialId() {
        return material_id;
    }
}
