package commands.tables;

import commands.Command;
import handlers.DatabaseHandler;
import models.Database;
import models.Row;
import models.Table;
import utils.TableWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
        TableWriter writer = new TableWriter();

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

        List<Row> rows = table.getRows();

        try {
            writer.writeTable(table, rows, String.valueOf(file));
            System.out.println("Successfully exported table " + tableName);
        } catch (IOException e){
            System.out.println("Error: " + e.getMessage());
            return;
        }
    }
}
