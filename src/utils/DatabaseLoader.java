package utils;

import models.Database;
import models.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DatabaseLoader {
    private Database database;

    public DatabaseLoader(Database database) {
        this.database = database;
    }

    public void loadDatabase(String databasePath) {
     try (BufferedReader br = new BufferedReader(new FileReader(databasePath))) {
       String line;
       while ((line = br.readLine()) != null){
           String[] parts = line.split(",");
           String tableName = parts[0].trim();
           String tablePath = parts[1].trim();
           Table table = new Table(tableName, tablePath);
           TableLoader tableLoader = new TableLoader(database);
           tableLoader.loadTable(table);
       }
     } catch (IOException e) {
         System.out.println("Error: " + e.getMessage());
     } catch (ArrayIndexOutOfBoundsException e){

     }
    }
}
