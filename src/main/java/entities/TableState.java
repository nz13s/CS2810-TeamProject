package entities;

import java.util.ArrayList;

public class TableState {

    private ArrayList<Table> tables;

    public TableState(ArrayList<Table> tables) {
       this.tables = tables;
    }

    public int size() {
        return tables.size();
    }

    public ArrayList<Table> getTableList() {
        return tables;
    }

}
