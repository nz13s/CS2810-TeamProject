package entities;

import databaseInit.Database;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class that stores every table's state.
 *
 * @author Jatin
 */

public class TableState {

    private static ArrayList<Table> tables = new ArrayList<>();
    private static ArrayList<Table> tableNeedWaiter = new ArrayList<>();

    /**
     * Returns the number of tables in the restaurant.
     *
     * @return number of tables in the restaurant.
     */
    public static int size() {
        return tables.size();
    }

    /**
     * Returns a list of tables.
     *
     * @return list of tables.
     */
    public static ArrayList<Table> getTableList() {
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
     * Gets the list of all tables that are occupied.
     *
     * @return occupied the List of tables occupied.
     */
    public static ArrayList<Table> getTableOccupied() {
        ArrayList<Table> occupied = new ArrayList<>();
        for (Table t : tables) {
            if (t.isOccupied()) {
                occupied.add(t);
            }
        }
        return occupied;
    }

    /**
     * Gets the list of all tables that are occupied.
     *
     * @return freeTables the List of tables that are free.
     */
    public static ArrayList<Table> getTableFree() {
        ArrayList<Table> freeTables = new ArrayList<>();
        for (Table t : tables) {
            if (!t.isOccupied()) {
                freeTables.add(t);
            }
        }
        return freeTables;
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
        Table remove = null;
        for (Table table : tableNeedWaiter) {
            if (table.getTableNum() == t.getTableNum()) {
                remove = table;
            }
        }
        tableNeedWaiter.remove(remove);
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
     * @param ID The table ID
     */
    public static Table getTableByID(int ID) throws SQLException {
        updateTables();
        for (Table table : tables) {
            if (table.getTableNum() == ID) {
                return table;
            }
        }
        return null;
    }

    public static void updateTables() throws SQLException {
        if (tables.isEmpty()) {
            Database.TABLES.fetchTables();
        }
    }


}
