package handlers;

import commands.*;
import models.Table;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private Table currentTable;
    private Map<String, Command> commands;
    private File currentFile;
    public CommandHandler(Table currentTable) {
        commands = new HashMap<>();
        commands.put("open", new OpenCommand(this));
        commands.put("close", new CloseCommand(this));
        commands.put("save", new SaveCommand(this));
        commands.put("saveas", new SaveAsCommand(this));
        this.currentTable = currentTable;
    }

    public void handleCommand(String command) {
        String[] parts = command.split(" ", 2);
        if (commands.containsKey(parts[0])) {
            commands.get(parts[0]).execute(parts);
        } else {
            System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public Table getCurrentTable() {
        return currentTable;
    }
}