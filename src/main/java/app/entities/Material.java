package app.entities;

public class Material {

    private int material_id;

    private String name;

    private String unit;

    private int width;

    private int length;

    public Material(int material_id, String name, String unit, int width, int length) {
        this.material_id = material_id;
        this.name = name;
        this.unit = unit;
        this.width = width;
        this.length = length;
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

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }
}
