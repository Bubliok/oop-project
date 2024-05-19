package models;

import java.util.*;

public class Database {
    private Map<String, Table> tables = new HashMap<>();
    private String databaseName;
    private String filePath;

    public Database(){};
    public Database(String databaseName) {
        this.databaseName = databaseName;
        this.tables = new HashMap<>();
    }

    public String getDatabaseName() {
        return databaseName;
    }
    public Collection<Table> getTables() {
        return tables.values();
    }
    public void addTable(Table table) {
        tables.put(table.getTableName(), table);
    }
    public Table getTable(String tableName) {
        return tables.get(tableName);
    }
}
