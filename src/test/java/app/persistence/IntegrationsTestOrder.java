package app.persistence;

import app.entities.CarportDesign;
import app.entities.Order;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationsTestOrder {

    static ConnectionPool connectionPool = ConnectionPool.getInstance("", "", "", "");

    @BeforeAll
    static void setupBeforeAll() {

        try (Connection connection = connectionPool.getConnection())
        {
            try (Statement stmt = connection.createStatement())
            {
                // The test schema is already created, so we only need to delete/create test tables
                stmt.execute("DROP TABLE IF EXISTS test.users");
                stmt.execute("DROP TABLE IF EXISTS test.orders");
                stmt.execute("DROP TABLE IF EXISTS test.carport_design");

                stmt.execute("DROP SEQUENCE IF EXISTS test.users_user_id_seq CASCADE;");
                stmt.execute("DROP SEQUENCE IF EXISTS test.orders_order_id_seq CASCADE;");
                stmt.execute("DROP SEQUENCE IF EXISTS test.carport_design_carport_id_seq CASCADE;");

                // Create tables as copy of original public schema structure
                stmt.execute("CREATE TABLE test.users AS (SELECT * from public.users) WITH NO DATA");
                stmt.execute("CREATE TABLE test.orders AS (SELECT * from public.orders) WITH NO DATA");
                stmt.execute("CREATE TABLE test.carport_design AS (SELECT * from public.carport_design) WITH NO DATA");
                // Create sequences for auto generating id's for users and orders
                stmt.execute("CREATE SEQUENCE test.users_user_id_seq");
                stmt.execute("ALTER TABLE test.users ALTER COLUMN user_id SET DEFAULT nextval('test.users_user_id_seq')");
                stmt.execute("CREATE SEQUENCE test.orders_order_id_seq");
                stmt.execute("ALTER TABLE test.orders ALTER COLUMN order_id SET DEFAULT nextval('test.orders_order_id_seq')");
                stmt.execute("CREATE SEQUENCE test.carport_design_carport_id_seq");
                stmt.execute("ALTER TABLE test.carport_design ALTER COLUMN carport_id SET DEFAULT nextval('test.carport_design_carport_id_seq')");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            fail("Database connection failed");
        }
    }

    @BeforeEach
    void setupBeforeEach() {
        try (Connection connection = connectionPool.getConnection())
        {
            try (Statement stmt = connection.createStatement())
            {
                // Remove all rows from all tables
                stmt.execute("DELETE FROM test.orders");
                stmt.execute("DELETE FROM test.users");
                stmt.execute("DELETE FROM test.carport_design");

                stmt.execute("INSERT INTO test.users (username, password, admin) " +
                        "VALUES  ('bruger', '1234', false), ('admin', '1234', true)");

                stmt.execute("INSERT INTO test.orders (order_id, status, date, user_id, comment, carport_id, price) " +
                        "VALUES (1, 'PendingFog', '2024-05-10', 1, 'Levering haster, tak', 1, 17944), (2, 'PendingUser', '2024-05-10', 1, 'Gerne et pænk skur, tak', 2, 19999)") ;

                stmt.execute("INSERT INTO test.carport_design (carport_id, carport_width, carport_length, roof_trapeztype, roof_type, roof_incline, shed_width, shed_length) " +
                        "VALUES (1,510, 660, 'Plasttrapezplader', null, null, 0, 0), (2, 600, 780, 'Uden tagplader', null, null, 300, 360)");

                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('test.orders_order_id_seq', COALESCE((SELECT MAX(order_id) + 1 FROM test.orders), 1), false)");
                stmt.execute("SELECT setval('test.users_user_id_seq', COALESCE((SELECT MAX(user_id) + 1 FROM test.users), 1), false)");
                stmt.execute("SELECT setval('test.carport_design_carport_id_seq', COALESCE((SELECT MAX(carport_id) + 1 FROM test.carport_design), 1), false)");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            fail("Database connection failed");
        }
    }

    @Test
    void getOrders() {
        try
        {
            List<Order> orders = OrderMapper.getOrders(connectionPool);
            assertEquals(2, orders.size());
        }
        catch (DatabaseException e)
        {
            fail("Database fejl: " + e.getMessage());
        }
    }

    @Test
    void deleteOrder() {
        try {
            OrderMapper.deleteOrder(2,connectionPool);
        } catch (DatabaseException e) {
            fail("Database fejl: " + e.getMessage());
        }
    }

    @Test
    void addOrder() {
        //Først skal vi tilføje carport designet til databasen.
        CarportDesign carportDesign = new CarportDesign(600,780,"Uden tagplader",0,0,"Gerne et skur, tak");

        int carport_id = 0;

        try {
            carport_id = CarportMapper.createCarportDesign(carportDesign, connectionPool);
            System.out.println("Carport design added");
        } catch (DatabaseException e) {
            fail("Database fejl: " + e.getMessage());
        }

        //Derefter tilføjer vi ordren.
        try {
            OrderMapper.addOrder(carportDesign,1,carport_id,connectionPool);
            System.out.println("Order added");
        } catch (DatabaseException e) {
            fail("Database fejl: " + e.getMessage());
        }
    }

    @Test
    void updateOrderStatus() {
        try {
            OrderMapper.updateOrderStatus(1,"PendingUser",connectionPool);
        } catch (DatabaseException e) {
            fail("Database fejl: " + e.getMessage());
        }
    }
}