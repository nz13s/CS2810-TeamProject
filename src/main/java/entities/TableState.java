package entities;

import databaseInit.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class that stores every table's state.
 *
 * @author Tony Delchev, Jatin Khatra
 */

public class TableState {

  private static ArrayList<Table> tables = new ArrayList<>();
  private static ArrayList<Table> tableNeedWaiter = new ArrayList<>();
  private static ArrayList<Table> tableOccupied = new ArrayList<>();

  /**
   * Returns a list of tables.
   *
   * @return list of tables.
   */
  public static ArrayList<Table> getTableList() throws SQLException {
    if (tables.isEmpty()) {
      tables = Database.TABLES.fetchTables();
    }
    return tables;
  }

  /**
   * Sets the list of tables in Restaurant.
   *
   * @param tables the List of tables from Database.
   */
  public static void setTables(ArrayList<Table> tables) {
    TableState.tables = tables;
  }

  /**
   * Gets the list of all tables that are unoccupied.
   * Using the tables and the occupied tables,
   * creates a set of the difference.
   *
   * @return occupied the List of tables unoccupied.
   */
  public static ArrayList<Table> getTableUnOccupied() throws SQLException {
    HashSet<Table> similar = new HashSet<>(getTableList());
    HashSet<Table> different = new HashSet<>();
    different.addAll(getTableList());
    different.addAll(getTableOccupied());

    similar.retainAll(getTableOccupied());
    different.removeAll(similar);

    return new ArrayList<>(different);
  }

  /**
   * Gets list of occupied tables.
   * IF the server has been restarted it looks through,
   * all the tables and re-populates the occupied list.E
   * Every occupied table is added to need waiter list.
   *
   * @return occupied the List of tables occupied.
   */
  public static ArrayList<Table> getTableOccupied() throws SQLException {
    if (tableOccupied.isEmpty()) {
      for (Table t : getTableList()) {
        boolean hasT = ActiveStaff.hasWaiter(t);
        if (t.isOccupied() && hasT) {
          addOccupied(t);
        } else if (t.isOccupied() && !hasT) {
          addOccupied(t);
          addNeedWaiter(t);
        }
      }
    }
    return tableOccupied;
  }

  /**
   * Adds a table to the NeedWaiter list.
   *
   * @param t The table
   */
  public static void addOccupied(Table t) {
    if (!tableOccupied.contains(t)) {
      tableOccupied.add(t);
    }
  }

  /**
   * Removes a table from the Occupied list.
   *
   * @param t The table
   */
  public static void removeOccupied(Table t) {
    tableOccupied.remove(t);
  }

  /**
   * Gets the tableNeedWaiter list.
   *
   * @return tableNeedWaiter the List of tables waiting to be assigned.
   */
  public static ArrayList<Table> getNeedWaiter() {
    return tableNeedWaiter;
  }

  /**
   * Adds a table to the NeedWaiter list.
   *
   * @param t The table
   */
  public static void addNeedWaiter(Table t) {
    if (!tableNeedWaiter.contains(t)) {
      tableNeedWaiter.add(t);
    }
  }

  /**
   * Removes a table from the NeedWaiter list.
   *
   * @param t The table
   */
  //TODO Tony Find better way for this:
  public static void removeNeedWaiter(Table t) {
    tableNeedWaiter.remove(t);
  }

  /**
   * Adds a table to the tables list.
   *
   * @param table The table
   */
  public static void addTable(Table table) {
    tables.add(table);
  }

  /**
   * removes all tables.
   */
  public static void removeAllTables() {
    tables.removeAll(tables);
  }

  /**
   * Gets table by ID from list of tables.
   *
   * @param id The table ID
   */
  public static Table getTableByID(int id) throws SQLException {
    for (Table table : getTableList()) {
      if (table.getTableNum() == id) {
        return table;
      }
    }
    return null;
  }

  /**
   * Fetches all tables with orders from Database if Tables list for this class is empty.
   *
   * @throws SQLException if unable to retrieve data.
   */
  public static void updateTables() throws SQLException {
    if (tables.isEmpty()) {
      Database.TABLES.fetchTables();
    }
  }
}