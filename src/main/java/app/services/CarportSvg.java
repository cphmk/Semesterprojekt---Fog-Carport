package app.services;

import app.entities.CarportDesign;

public class CarportSvg
{
    private Svg carportSvg;

    private CarportDesign carportDesign;
    public CarportSvg(CarportDesign carportDesign)
    {
        this.carportDesign = carportDesign;
        carportSvg = new Svg(0, 0, "0 0 855 690", "100%" );
        carportSvg.addRectangle(0,0,carportDesign.getCarport_width(), carportDesign.getCarport_length(), "stroke-width:1px; stroke:#000000; fill: #ffffff");
        addBeams();
        addRafters();
        addShed();
        addPost();
    }

    private void addBeams(){
        carportSvg.addRectangle(0,35,4.5, carportDesign.getCarport_length(), "stroke-width:1px; stroke:#000000; fill: #ffffff");
        carportSvg.addRectangle(0,carportDesign.getCarport_width()-35,4.5, carportDesign.getCarport_length(), "stroke-width:1px; stroke:#000000; fill: #ffffff");
    }

    private void addRafters(){
        for (double i = 0; i < carportDesign.getCarport_length(); i += carportDesign.getCarport_width()/14)
        {
            carportSvg.addRectangle(i, 0.0, carportDesign.getCarport_width(), 4.5,"stroke:#000000; fill: #ffffff" );
        }
    }

    private void addShed() {
        //Hvis der er et redskabsrum
        if (carportDesign.getRedskabsrum_width() != 0) {
            //Top and bottom
            carportSvg.addRectangle(30, 35, 4.5, carportDesign.getRedskabsrum_length(), "stroke-width:3px; stroke:#000000; fill: #ffffff");
            carportSvg.addRectangle(30,carportDesign.getCarport_width()-35, 4.5, carportDesign.getRedskabsrum_length(), "stroke-width:3px; stroke:#000000; fill: #ffffff");
            //Left and right - Sides
            carportSvg.addRectangle(30,35, carportDesign.getCarport_width()-70, 4.5, "stroke-width:3px; stroke:#000000; fill: #ffffff");
            carportSvg.addRectangle(carportDesign.getRedskabsrum_length()+30,35, carportDesign.getCarport_width()-70, 4.5, "stroke-width:3px; stroke:#000000; fill: #ffffff");
        }
    }

    private void addPost() {
        //Hvis der er et redskabsrum
        if (carportDesign.getRedskabsrum_width() != 0) {
            //Top left
            carportSvg.addRectangle(30, 35, 10, 10, "stroke-width:3px; stroke:#000000; fill: red");
            //Top right
            carportSvg.addRectangle(30,carportDesign.getCarport_width()-35, 10, 10, "stroke-width:3px; stroke:#000000; fill: red");
            //Middle left
            carportSvg.addRectangle(30, (carportDesign.getRedskabsrum_width()+35)*0.5, 10, 10, "stroke-width:3px; stroke:#000000; fill: red");
            //Middle right
            carportSvg.addRectangle(carportDesign.getRedskabsrum_length()+30, (carportDesign.getRedskabsrum_width()+35)*0.5, 10, 10, "stroke-width:3px; stroke:#000000; fill: red");
            //Bottom left
            carportSvg.addRectangle(carportDesign.getRedskabsrum_length()+30,35, 10, 10, "stroke-width:3px; stroke:#000000; fill: red");
            //Bottom right
            carportSvg.addRectangle(carportDesign.getRedskabsrum_length()+30,carportDesign.getCarport_width()-35, 10, 10, "stroke-width:3px; stroke:#000000; fill: red");
        }

        //Hvis der ikke er et redskabsrum
        else if (carportDesign.getRedskabsrum_width() == 0) {
            for (double i = 100; i < carportDesign.getCarport_length()-30; i += 200) {
                carportSvg.addRectangle(i,35, 7, 7, "stroke-width:3px; stroke:#000000; fill: green");
                carportSvg.addRectangle(i,carportDesign.getCarport_width()-35, 7,7, "stroke-width:3px; stroke:#000000; fill: green");
            }
        }
    }

    @Override
    public String toString()
    {
        return carportSvg.toString();
    }
}