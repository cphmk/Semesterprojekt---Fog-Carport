package app.persistence;

import app.entities.Material;
import app.entities.Order;
import app.entities.Variant;
import app.exceptions.DatabaseException;

import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MaterialMapper {
    public static List<Variant> getVariantsByProductIdAndMinLength(int minLength, int material_id, ConnectionPool connectionPool) throws SQLException, DatabaseException {
        List<Variant> productVariants = new ArrayList<>();
        String sql = "SELECT * FROM variant INNER JOIN material USING(material_id) WHERE material_id = ? AND length >= ?";

        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, material_id);
            ps.setInt(2, minLength);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {

                int variantId = resultSet.getInt("variant_id");
                int materialId = resultSet.getInt("material_id");
                int length = resultSet.getInt("length");
                String name = resultSet.getString("name");
                String unit = resultSet.getString("unit");
                int price = resultSet.getInt("price");
                Material material = new Material(materialId, name, unit, price);
                Variant variant = new Variant(variantId, length, material);
                productVariants.add(variant);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Could not get variants from the database", e.getMessage());
        }
        return productVariants;
    }
}
