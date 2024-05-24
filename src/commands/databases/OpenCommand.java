package commands;

import models.Row;
import models.Table;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenCommand implements Command {
    private Map<String, Table> tables;

    public OpenCommand() {
        tables = new HashMap<>();
    }

    @Override
    public void execute(String[] args) {
        String databaseFile = args[0];
        try (BufferedReader reader = new BufferedReader(new FileReader(databaseFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String tableName = parts[0];
                String tableFile = parts[1];
                Table table = loadTable(tableName, tableFile);
                tables.put(tableName, table);
                System.out.println("Table " + tableName + " loaded successfully");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Table loadTable(String name, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + filePath + " does not exist");
        }
        Table table = new Table(name, filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Row row = new Row(List.of(values));
                table.addRow(row);
                System.out.println("Row added to table " + name);
            }
        }
        return table;
    }
}