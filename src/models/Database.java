package models;

import java.io.*;
import java.util.*;

public class Database {
    private Map<String, Table> tables = new HashMap<>();
    private String databaseName;
    private String filePath;

    public Database(String databaseName) {
        this.databaseName = databaseName;
        this.tables = new HashMap<>();
    }

    public void loadFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) {
                    throw new IOException("Invalid line in database file: " + line);
                }

                String tableName = parts[0].trim();
                String tableFilePath = parts[1].trim();

                Table table = new Table(tableName);
                table.loadFromFile(tableFilePath);

                tables.put(tableName, table);
            }
        }
    }
    public void saveToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Table> entry : tables.entrySet()) {
                String tableName = entry.getKey();
                String tableFilePath = entry.getValue().getTableFilename();
                writer.write(tableName + "," + tableFilePath);
                writer.newLine();
            }
        }
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
