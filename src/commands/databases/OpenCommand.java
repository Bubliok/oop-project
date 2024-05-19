package commands.databases;
import commands.Command;
import handlers.CommandHandler;
import loaders.DatabaseLoader;

import java.io.*;

public class OpenCommand implements Command {
    private CommandHandler commandHandler;
    private DatabaseLoader databaseLoader;

    public OpenCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        this.databaseLoader = new DatabaseLoader();
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid command. Please provide a file path.");
            return;
        }

        String filePath = args[1];
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        commandHandler.setCurrentFile(file);
        System.out.println("Successfully opened " + filePath);

        try {
            databaseLoader.loadFromFile(commandHandler.getDatabase(), filePath);
        } catch (IOException e) {
            System.out.println("Error while loading database: "+ e.getMessage());
            return;
        }
    }
}
