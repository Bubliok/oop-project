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
    public Table getTable(String tableName) {
        for (Table table : tables) {
            if (table.getTableName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Database: ");
        for(Table table: tables){
            sb.append("Table: ").append(table.getTableName());
        }
        return sb.toString();
    }
}
