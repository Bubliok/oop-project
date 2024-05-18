package commands.databases;
import commands.Command;
import handlers.CommandHandler;

import java.io.*;

public class OpenCommand implements Command {
    private CommandHandler commandHandler;

    public OpenCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
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
            System.out.println("File does not exist: " + filePath);
            return;
        }

        commandHandler.setCurrentFile(file);
        System.out.println("Successfully opened " + filePath);

        try {
            commandHandler.getDatabase().loadFromFile(filePath);
        } catch (IOException e) {
            System.out.println("Error when loading database: "+ e.getMessage());
            return;
        }
    }
}
