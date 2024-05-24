package commands.databases;

import commands.Command;
import handlers.CommandHandler;
import models.Database;
import models.Table;

import java.io.*;

public class SaveCommand implements Command {
    CommandHandler commandHandler;
    Database database;

    public SaveCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        File currentFile = commandHandler.getCurrentFile();
        //TODO needs database passed so it can read
        if (currentFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                //for (Table table : )
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
