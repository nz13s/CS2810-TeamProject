package sql;

import databaseInit.Database;
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
import javax.annotation.Nullable;

/**
 * Class that stores SQL queries related to the restaurant_table in the database.
 *
 * @author Jatin Khatra, Bhavik Narang
 */

public class Tables {
  private PreparedStatement fetchTables;
  private PreparedStatement tableById;
  private PreparedStatement updateTableOccupied;

  /**
   * Constructor that holds the SQL queries that are going to be used.
   *
   * @param connection connection to the database.
   * @throws SQLException thrown if sql logic is wrong.
   */
  public Tables(Connection connection) throws SQLException {

    fetchTables = connection.prepareStatement(
            "SELECT restaurant_table.table_num, order_id, seats_available, occupied, "
                    + "time_ordered, order_confirmed, order_preparing, order_ready, order_served\n"
                    + "FROM restaurant_table LEFT JOIN orders "
                    + "ON restaurant_table.table_num = orders.table_num\n"
                    + "ORDER BY table_num");

    tableById = connection.prepareStatement(
            "SELECT table_num, seats_available, occupied\n"
                    + "FROM restaurant_table\n"
                    + "WHERE table_num = ?");
    updateTableOccupied = connection.prepareStatement(
            "UPDATE restaurant_table "
                    + "SET occupied = ? "
                    + "WHERE table_num = ?");
  }

  /**
   * Method that gets all tables that have order(s).
   *
   * @return ArrayList of Tables.
   * @throws SQLException thrown if sql logic is incorrect.
   */

  public ArrayList<Table> fetchTables() throws SQLException {
    // TableState tableState = new TableState();
    ResultSet resultSet = fetchTables.executeQuery();
    Set<Table> tables = new HashSet<>();
    TableState.removeAllTables();

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
              .orElse(new Table(resultSet.getInt("table_num"),
                      resultSet.getInt("seats_available"), resultSet.getBoolean("occupied"), null));

      // if no orders exist for a given table
      if (order.getOrderID() != 0) {
        t.addOrder(order);
      }
      tables.add(t);
    }
    return new ArrayList<>(tables);
    //tables.forEach(TableState::addTable);

  }

  /**
   * Selects the table record with all of its orders matching the table_num.
   *
   * @param table_num table number
   * @return Table object
   * @throws SQLException if an error occurred
   */
  @Nullable
  public Table getTableByID(int table_num) throws SQLException {
    ArrayList<Order> allOrders = Database.ORDERS.getOrders(0L, 0L);
    tableById.setInt(1, table_num);
    ResultSet resultSet = tableById.executeQuery();
    if (resultSet.next()) {
      Table t = new Table(
              resultSet.getInt("table_num"),
              resultSet.getInt("seats_available"),
              resultSet.getBoolean("occupied"),
              new ArrayList<>()
      );
      for (Order order : allOrders) {
        if (order.getTableNum() == t.getTableNum()) {
          t.addOrder(order);
        }
      }
      return t;
    }
    return null;
  }

  /**
   * Gets a table, optionally populating it with the orders.
   *
   * @param tableNum num of table to fetch
   * @param populate if true -> calls the other overload with additional data filling
   * @return an {@link entities.IFakeable} Table.
   * @throws SQLException upon a connection error.
   */
  @Nullable
  public Table getTableByID(int tableNum, boolean populate) throws SQLException {
    if (populate) {
      return getTableByID(tableNum);
    }
    tableById.setInt(1, tableNum);
    ResultSet resultSet = tableById.executeQuery();
    if (resultSet.next()) {
      return new Table(
              resultSet.getInt("table_num"),
              resultSet.getInt("seats_available"),
              resultSet.getBoolean("occupied"),
              null
      );
    }
    return null;
  }

  /**
   * Update a table as occupied an execute query.
   *
   * @param occupied boolean t/f
   * @param tableNum of the table
   * @return boolean of the updated state.
   * @throws SQLException
   */
  public boolean updateTableOccupied(Boolean occupied, int tableNum) throws SQLException {
    updateTableOccupied.setBoolean(1, occupied);
    updateTableOccupied.setInt(2, tableNum);
    updateTableOccupied.execute();
    ResultSet resultSet = updateTableOccupied.getGeneratedKeys();
    return resultSet.next();
  }


}