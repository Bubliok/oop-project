package commands.databases;

import commands.Command;
import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Table;

import java.io.File;

public class RenameTableCommand implements Command {
    private CommandHandler commandHandler;

    public RenameTableCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if(args.length < 3){
            System.out.println("Invalid number of arguments. See 'help' for more information.");
            return;
        }
        String oldName = args[1];
        String newName = args[2];

        Table table = commandHandler.getDatabase().getTable(oldName);
        TableFileHandlerImpl fileHandler = new TableFileHandlerImpl(oldName);

        if (table == null) {
            System.out.println("Table " + oldName + " does not exist.");
            return;
        }
        if (commandHandler.getDatabase().getTable(newName) != null) {
            System.out.println("Table " + newName + " already exists.");
            return;
        }
        try {
            File oldFile = new File(fileHandler.getFilePath());
            String newFilePath = fileHandler.getFilePath().replace(oldName, newName);
            File newFile = new File(newFilePath);

            if (oldFile.renameTo(newFile)) {
                commandHandler.getDatabase().removeTable(table);
                table.setTableName(newName);
                fileHandler.setFilePath(newFilePath);
                table.setFileHandler(fileHandler);
                commandHandler.getDatabase().addTable(table);

                System.out.println("Successfully renamed table to: " + newName);
            } else {
                System.out.println("Failed to rename table file.");
            }
        } catch (Exception e) {
            System.out.println("Error while renaming table: " + e.getMessage());
            return;
        }

    }
}
