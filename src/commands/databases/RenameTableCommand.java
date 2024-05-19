package commands.databases;

import commands.Command;
import handlers.CommandHandler;
import handlers.TableFileHandlerImpl;
import models.Table;

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
            commandHandler.getDatabase().removeTable(table);
            table.setTableName(newName);
            fileHandler.setFilePath(fileHandler.getFilePath().replace(oldName, newName));
            table.setFileHandler(fileHandler);
            commandHandler.getDatabase().addTable(table);


            System.out.println("Successfully renamed table.");
        }catch (Exception e){
            System.out.println("Error while renaming table: " + e.getMessage());
            return;
        }

    }
}
