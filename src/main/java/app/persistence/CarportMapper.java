package app.persistence;

import app.entities.CarportDesign;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CarportMapper {

    public static void createCarportDesign(CarportDesign carportDesign, ConnectionPool connectionPool) throws DatabaseException {

        if (carportDesign.getRoof_tiles() == null) {
            String sql = "INSERT INTO carport_design (carport_width, carport_length, roof_trapeztype, shed_width, shed_length) VALUES (?,?,?,?,?)";

            try (
                    Connection connection = connectionPool.getConnection();
                    PreparedStatement ps = connection.prepareStatement(sql)
            ) {
                ps.setInt(1,carportDesign.getCarport_width());
                ps.setInt(2, carportDesign.getCarport_length());
                ps.setString(3,carportDesign.getRoof_tiles());
                ps.setInt(4,carportDesign.getRedskabsrum_width());
                ps.setInt(5,carportDesign.getRedskabsrum_length());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Fejl ved oprettelse af ny carport design");
                }
            } catch (SQLException e) {
                String msg = "Der er sket en fejl. Prøv igen";
                if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                    msg = "Carport designet eksisterer allerede";
                }
                throw new DatabaseException(msg, e.getMessage());
            }
        }

        else {
            String sql = "INSERT INTO carport_design (carport_width, carport_length, roof_type, roof_incline, shed_width, shed_length) VALUES (?,?,?,?,?,?)";

            try (
                    Connection connection = connectionPool.getConnection();
                    PreparedStatement ps = connection.prepareStatement(sql)
            ) {
                ps.setInt(1,carportDesign.getCarport_width());
                ps.setInt(2, carportDesign.getCarport_length());
                ps.setString(3,carportDesign.getRoof_type());
                ps.setInt(4,carportDesign.getRoof_incline());
                ps.setInt(5,carportDesign.getRedskabsrum_width());
                ps.setInt(6,carportDesign.getRedskabsrum_length());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected != 1) {
                    throw new DatabaseException("Fejl ved oprettelse af ny carport design");
                }
            } catch (SQLException e) {
                String msg = "Der er sket en fejl. Prøv igen";
                if (e.getMessage().startsWith("ERROR: duplicate key value ")) {
                    msg = "Carport designet eksisterer allerede";
                }
                throw new DatabaseException(msg, e.getMessage());
            }
        }
    }

}
