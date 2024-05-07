package app.persistence;

import app.entities.CarportDesign;
import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.*;

public class CarportMapper {

    public static int createCarportDesign(CarportDesign carportDesign, ConnectionPool connectionPool) throws DatabaseException {
        String sql;
        if (carportDesign.getRoof_tiles() == null) {
            sql = "INSERT INTO carport_design (carport_width, carport_length, roof_trapeztype, shed_width, shed_length) VALUES (?,?,?,?,?)";
        } else {
            sql = "INSERT INTO carport_design (carport_width, carport_length, roof_type, roof_incline, shed_width, shed_length) VALUES (?,?,?,?,?,?)";
        }

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, carportDesign.getCarport_width());
            ps.setInt(2, carportDesign.getCarport_length());
            if (carportDesign.getRoof_tiles() == null) {
                ps.setString(3, carportDesign.getRoof_tiles());
                ps.setInt(4, carportDesign.getRedskabsrum_width());
                ps.setInt(5, carportDesign.getRedskabsrum_length());
            } else {
                ps.setString(3, carportDesign.getRoof_type());
                ps.setInt(4, carportDesign.getRoof_incline());
                ps.setInt(5, carportDesign.getRedskabsrum_width());
                ps.setInt(6, carportDesign.getRedskabsrum_length());
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

}
