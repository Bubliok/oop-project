package commands.tables;

import commands.Command;
import handlers.DatabaseHandler;
import models.Column;
import models.Database;
import models.Row;
import models.Table;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportCommand implements Command {
    private DatabaseHandler databaseHandler;

    public ExportCommand(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void execute(String[] args) {
        if(args.length < 3){
            System.out.println("Invalid arguments. Please provide valid name and file path.");
            return;
        }

        String tableName = args[1];
        String filePath = args[2];
        File file = new File(filePath);
        Database database = databaseHandler.getDatabase();
        Table table = database.getTable(tableName);
        File directory = file.getParentFile();

        if (file.exists()){
            System.out.println("File already exists.");
            return;
        }

        if (file.isDirectory()){
            System.out.println("No file name provided.");
            return;
        }

        if (directory == null){
            System.out.println("Invalid file path.");
            return;
        }

        if(table == null){
            System.out.println("Table named: " + tableName + " does not exist.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            int columnCount = table.getColumns().size();
            for (int i = 0; i < columnCount; i++) {
                Column column = table.getColumns().get(i);
                writer.write(column.getColumnName() + ":" + column.getColumnType());
                if (i < columnCount - 1) {
                    writer.write(", ");
                }
            }
            writer.newLine();
            for (Row row : table.getRows()){
                writer.write(row.toString());
                writer.newLine();
            }
            System.out.println("Successfully exported table " + tableName);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
    }
}
