package sql;

import entities.Item;
import entities.Order;
import entities.Table;
import entities.TableState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Class that stores SQL queries related to the restaurant_table in the database.
 *
 * @author Jatin Khatra
 * @author Bhavik Narang
 */

public class Tables {

    private PreparedStatement fetchTables;
    private PreparedStatement getTableByID;

    /**
     * Constructor that holds the SQL queries that are going to be used.
     *
     * @param connection connection to the database.
     * @throws SQLException thrown if sql logic is wrong.
     */

    public Tables(Connection connection) throws SQLException {

        fetchTables = connection.prepareStatement(
                "SELECT restaurant_table.table_num, order_id, seats_available, occupied, time_ordered, order_confirmed, order_preparing, order_ready, order_served\n" +
                        "FROM restaurant_table LEFT JOIN orders ON restaurant_table.table_num = orders.table_num\n" +
                        "ORDER BY table_num");
        getTableByID = connection.prepareStatement("Select * " +
                "FROM resturant_table " +
                "WHERE table_num = ?");
    }

    /**
     * Method that gets all tables that have order(s).
     *
     * @return ArrayList of Tables.
     * @throws SQLException thrown if sql logic is incorrect.
     */

    public TableState fetchTables() throws SQLException {
        TableState tableState = new TableState();
        ResultSet resultSet = fetchTables.executeQuery();
        Set<Table> tables = new HashSet<>();

        while (resultSet.next()) {
            Order order = new Order(
                    resultSet.getInt("order_id"),
                    resultSet.getLong("time_ordered"),
                    resultSet.getLong("order_confirmed"),
                    resultSet.getLong("order_preparing"),
                    resultSet.getLong("order_ready"),
                    resultSet.getLong("order_served"),
                    resultSet.getInt("table_num"),
                    new ArrayList<Item>());
            Table t = tables.stream().filter(tab -> tab.getTableNum() == order.getTableNum()).findFirst()
                    .orElse(new Table(resultSet.getInt("table_num"), resultSet.getInt("seats_available"), resultSet.getBoolean("occupied"),null));

            // if no orders exist for a given table
            if (order.getOrderID() != 0) {
                t.addOrder(order);
            }
            tables.add(t);
        }
        tables.forEach(tableState::addTable);
        return tableState;
    }

    public Table getTableByID(int tableID) throws SQLException {
        getTableByID.setInt(1, tableID);
        ResultSet resultset = getTableByID.executeQuery();
        Table t = new Table(resultset.getInt("table_num"), resultset.getInt("seats_available"),
                resultset.getBoolean("occupied"), null);
        return t;
    }

}