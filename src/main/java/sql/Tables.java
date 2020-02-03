package sql;

import entities.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class that stores SQL queries related to the restaurant_table in the database.
 *
 * @author Jatin Khatra
 */

public class Tables {

    private PreparedStatement allNonOccupiedTables;

    /**
     * Constructor that holds the SQL queries that are going to be used.
     *
     * @param connection connection to the database.
     * @throws SQLException thrown if sql logic is wrong.
     */

    public Tables(Connection connection) throws SQLException {
        allNonOccupiedTables = connection.prepareStatement(
                "SELECT table_num, seats_available, occupied " +
                        "FROM restaurant_table" +
                        "WHERE occupied = false");
    }

    /**
     * Method that gets all tables that are not occupied.
     *
     * @return ArrayList of all tables not occupied.
     * @throws SQLException thrown if sql logic is incorrect.
     */

    public ArrayList<Table> getAllNonOccupiedTables() throws SQLException {
        ResultSet resultSet = allNonOccupiedTables.executeQuery();
        ArrayList<Table> list = new ArrayList<Table>();
        while (resultSet.next()) {
            list.add(new Table(
                    resultSet.getInt("table_num"),
                    resultSet.getInt("seats_available"),
                    null));
        }
        return list;
    }

}