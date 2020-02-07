package entities;

import java.util.ArrayList;

/**
 * Class that stores every table's state.
 *
 * @author Jatin
 */

public class TableState {

    private ArrayList<Table> tables;

    /**
     * Constructor that initialises the list of tables.
     */

    public TableState() {
        tables = new ArrayList<Table>();
    }

    /**
     * Constructor that initialises the list of tables, given by the parameter.
     */

    public TableState(ArrayList<Table> tables) {
       this.tables = tables;
    }

    /**
     * Returns the number of tables in the restaurant.
     *
     * @return number of tables in the restaurant.
     */

    public int size() {
        return tables.size();
    }

    /**
     * Returns a list of tables.
     *
     * @return list of tables.
     */

    public ArrayList<Table> getTableList() {
        return tables;
    }

    public void addTable(Table table) {
        tables.add(table);
    }

}
