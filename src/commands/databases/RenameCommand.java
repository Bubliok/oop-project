package commands.databases;

import commands.Command;
import handlers.CommandHandler;
import handlers.DatabaseHandler;
import models.Database;
import models.Table;

import java.io.File;

public class RenameCommand implements Command {
    private DatabaseHandler databaseHandler;
    private SaveCommand saveCommand;

    public RenameCommand(CommandHandler commandHandler, DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
        this.saveCommand = new SaveCommand(commandHandler,databaseHandler);
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            System.out.println("Invalid number of arguments.");
            return;
        }

        String tableName = args[1];
        String newName = args[2];
        Database database = databaseHandler.getDatabase();
        Table table = database.getTable(tableName);

        if (table == null){
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }
        if (database.getTable(newName) != null ){
            System.out.println("Table " + newName + " already exists.");
            return;
        }
        try {
            File oldFile = new File(table.getTablePath());
            String newFilePath = table.getTablePath().replace(tableName, newName);
            File newFile = new File(newFilePath);

            if (oldFile.renameTo(newFile)) {
                table.setTableName(newName);
                table.setTablePath(newFilePath);
                System.out.println("Successfully renamed table.");
                saveCommand.execute(new String[]{});
            } else {
                System.out.println("Error: Unable to rename table.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }


    }
}
