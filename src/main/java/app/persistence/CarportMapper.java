package app.persistence;

import app.entities.CarportDesign;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;

public class CarportMapper {

    public static int createCarportDesign(CarportDesign carportDesign, ConnectionPool connectionPool) throws DatabaseException {
        String sql;
        //Højt tag
        if (carportDesign.getRoof_tiles() == null) {
            sql = "INSERT INTO carport_design (carport_width, carport_length, roof_type, roof_incline, shed_width, shed_length) VALUES (?,?,?,?,?,?)";
        }
        //Fladt tag
        else {
            sql = "INSERT INTO carport_design (carport_width, carport_length, roof_trapeztype, shed_width, shed_length) VALUES (?,?,?,?,?)";
        }

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, carportDesign.getCarport_width());
            ps.setInt(2, carportDesign.getCarport_length());
            //Højt tag
            if (carportDesign.getRoof_tiles() == null) {
                ps.setString(3, carportDesign.getRoof_type());
                ps.setInt(4, carportDesign.getRoof_incline());
                ps.setInt(5, carportDesign.getRedskabsrum_width());
                ps.setInt(6, carportDesign.getRedskabsrum_length());
            }
            //Fladt tag
            else {
                ps.setString(3, carportDesign.getRoof_tiles());
                ps.setInt(4, carportDesign.getRedskabsrum_width());
                ps.setInt(5, carportDesign.getRedskabsrum_length());
            }

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1) {
                throw new DatabaseException("Fejl ved oprettelse af ny carport design");
            }

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new DatabaseException("Fejl ved oprettelse af nyt carport design - kunne ikke finde genereret id");
            }
        } catch (SQLException e) {
            String msg = "Der er sket en fejl. Prøv igen";
            if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                msg = "Carport designet eksisterer allerede";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    public static CarportDesign getCarportDesign(int order_id, ConnectionPool connectionPool) throws DatabaseException {
        String sql = "SELECT * FROM carport_design INNER JOIN orders USING(carport_id) WHERE order_id = ?";

        CarportDesign carportDesign = null;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, order_id);

            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int carport_width = rs.getInt("carport_width");
                    int carport_length = rs.getInt("carport_length");
                    String roof_tiles = rs.getString("roof_trapeztype");
                    String roof_type = rs.getString("roof_type");
                    int roof_incline = rs.getInt("roof_incline");
                    int shed_width = rs.getInt("shed_width");
                    int shed_length = rs.getInt("shed_length");
                    String comment = rs.getString("comment");

                    //Fladt tag
                    if (roof_tiles != null) {
                        carportDesign = new CarportDesign(carport_width,carport_length,roof_tiles,shed_width,shed_length,comment);
                    }

                    //Højt tag
                    else {
                        carportDesign = new CarportDesign(carport_width,carport_length,roof_type,roof_incline,shed_width,shed_length,comment);
                    }
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("DB fejl", e.getMessage());
        }
        return carportDesign;
    }

}
