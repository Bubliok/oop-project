package commands.tables;

import commands.Command;
import handlers.DatabaseHandler;
import models.Database;
import models.Table;
import utils.TableLoader;

public class ImportCommand implements Command {
    private DatabaseHandler databaseHandler;

    public ImportCommand(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid arguments. Please provide a valid table file path.");
            return;
        }
        String filePath = args[1];
        Database database = databaseHandler.getDatabase();
        String tableName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));

        Table existingTable = database.getTable(tableName);
        if (existingTable != null && existingTable.getTableName().equals(tableName)) {
            System.out.println("Table with the name " + tableName + " already exists.");
            return;
        }

        Table table = new Table(tableName, filePath);
        TableLoader tableLoader = new TableLoader(database);

        if (tableLoader.loadTable(table)){
               System.out.println("Successfully imported table " + tableName + ".");
        }
    }
}
