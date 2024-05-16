package models;

import javax.xml.crypto.Data;
import java.io.*;
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

    public void loadFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Database file does not exist: " + filePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String tableFilePath = parts[0];
                    File tableFile = new File(tableFilePath);
                    if (!tableFile.exists()) {
                        System.out.println("Table file does not exist: " + tableFilePath);
                        continue;
                    }

                    String tableName = tableFilePath.substring(tableFilePath.lastIndexOf("/") + 1, tableFilePath.lastIndexOf("."));
                    Table table = new Table(tableName);
                    table.loadFromFile(tableFilePath);
                    this.addTable(table);
                }
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
