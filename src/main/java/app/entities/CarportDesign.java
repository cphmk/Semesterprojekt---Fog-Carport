package app.entities;

public class CarportDesign {

    private int carport_width;

    private int carport_length;

    private String roof_tiles;

    private String roof_type;

    private int roof_incline;

    private int redskabsrum_width;

    private int redskabsrum_length;

    private String comment;


    //Konstruktor for carport med fladt tag
    public CarportDesign(int carport_width, int carport_length, String roof_tiles, int redskabsrum_width, int redskabsrum_length, String comment) {
        this.carport_width = carport_width;
        this.carport_length = carport_length;
        this.roof_tiles = roof_tiles;
        this.redskabsrum_width = redskabsrum_width;
        this.redskabsrum_length = redskabsrum_length;
        this.comment = comment;
    }

    //Konstruktor for carport med højt tag
    public CarportDesign(int carport_width, int carport_length, String roof_type, int roof_incline, int redskabsrum_width, int redskabsrum_length, String comment) {
        this.carport_width = carport_width;
        this.carport_length = carport_length;
        this.roof_type = roof_type;
        this.roof_incline = roof_incline;
        this.redskabsrum_width = redskabsrum_width;
        this.redskabsrum_length = redskabsrum_length;
        this.comment = comment;
    }

    public int getCarport_width() {
        return carport_width;
    }

    public int getCarport_length() {
        return carport_length;
    }

    public String getRoof_tiles() {
        return roof_tiles;
    }

    public String getRoof_type() {
        return roof_type;
    }

    public int getRoof_incline() {
        return roof_incline;
    }

    public int getRedskabsrum_width() {
        return redskabsrum_width;
    }

    public int getRedskabsrum_length() {
        return redskabsrum_length;
    }

    public String getComment() {
        return comment;
    }

    public void setRedskabsrum_width(int redskabsrumWidth) {
        this.redskabsrum_width = redskabsrumWidth;
    }

    public void setRedskabsrum_length(int redskabsrum_length) {
        this.redskabsrum_length = redskabsrum_length;
    }
}
