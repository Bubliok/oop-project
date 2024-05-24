package models;

import java.util.*;

public class Database {
    private List<Table> tables = new ArrayList<>();

    public Database(){}

    public void addTable(Table table){
        tables.add(table);
    }

    public void removeTable(Table table){
        tables.remove(table);
    }

    public List<Table> getTables() {
        return tables;
    }
}
