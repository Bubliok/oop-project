package utils;

import models.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class DatabaseLoader {
    private Map<String, Table> tables;

    public DatabaseLoader(Map<String, Table> tables) {
        this.tables = tables;
    }

    public void loadDatabase(String databasePath) {
     try (BufferedReader br = new BufferedReader(new FileReader(databasePath))) {
       String line;
       while ((line = br.readLine()) != null){
           String[] parts = line.split(",");
           String tableName = parts[0];
           String tablePath = parts[1];
           Table table = new Table(tableName, tablePath);
           tables.put(tableName, table);
           TableLoader tableLoader = new TableLoader();
           tableLoader.loadTable(table);
       }
     } catch (IOException e) {
         System.out.println("Error: " + e.getMessage());
     }
    }
}
