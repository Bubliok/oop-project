package commands.databases;

import commands.Command;
import handlers.CommandHandler;

import java.io.File;

public class CloseCommand implements Command {
    private CommandHandler commandHandler;

    public CloseCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        File currentFile = commandHandler.getCurrentFile();

        if(currentFile != null){
            commandHandler.setCurrentFile(null);
            System.out.println("Successfully closed: " + currentFile.getName());
            System.gc();
        } else {
            System.out.println("No file is currently open.");
        }
    }
}
