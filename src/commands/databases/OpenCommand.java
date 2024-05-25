package commands.databases;
import commands.Command;
import handlers.CommandHandler;
import handlers.DatabaseHandler;
import models.Database;
import models.Table;
import utils.DatabaseLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OpenCommand implements Command {
    private DatabaseHandler databaseHandler;
    private CommandHandler commandHandler;

    public OpenCommand(CommandHandler commandHandler, DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        if(args.length < 2) {
            System.out.println("Invalid arguments. Please provide a file path.");
            return;
        }
        String filePath = args[1];
        File file = new File(filePath);
        if(!file.exists()){
            System.out.println("File does not exist.");
            return;
        }
        commandHandler.setCurrentFile(file);
        databaseHandler.loadDatabase(args[1]);
        System.out.println("Successfully opened " + filePath);
    }
}