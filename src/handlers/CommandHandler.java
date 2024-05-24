package handlers;

import commands.Command;
import commands.HelpCommand;
import commands.databases.CloseCommand;
import commands.databases.OpenCommand;
import commands.databases.SaveAsCommand;
import commands.databases.SaveCommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private Map<String, Command> commands;
    File currentFile;

    public CommandHandler() {
        commands = new HashMap<>();
        commands.put("open", new OpenCommand(this));
        commands.put("close", new CloseCommand(this));
        commands.put("save", new SaveCommand(this));
        commands.put("saveas", new SaveAsCommand(this));
        commands.put("help", new HelpCommand());
    }

    public void handleCommand(String command) {
        if(currentFile == null && !command.startsWith("open") && !command.startsWith("help")) {
            System.out.println("No file is currently open.");
            return;
        }

        String[] parts = command.split(" ");
        if (commands.containsKey(parts[0])) {
            commands.get(parts[0]).execute(parts);
        } else {
            System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

}
