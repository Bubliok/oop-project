package handlers;

import commands.*;
import models.Database;
import models.Table;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private Table currentTable;
    private Database database;
    private Map<String, Command> commands;
    private File currentFile;
    public CommandHandler(Table currentTable, Database database) {
        commands = new HashMap<>();
        commands.put("open", new OpenCommand(this));
        commands.put("close", new CloseCommand(this));
        commands.put("save", new SaveCommand(this));
        commands.put("saveas", new SaveAsCommand(this));
        commands.put("help", new HelpCommand(this));

        commands.put("import", new ImportCommand(this));
        this.currentTable = currentTable;
        this.database = database;
    }

    public void handleCommand(String command) {
        if(currentFile == null && !command.startsWith("open")){
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

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentTable(Table currentTable) {
        this.currentTable = currentTable;
    }

    public Table getCurrentTable() {
        return currentTable;
    }
    public Database getDatabase() {
        return this.database;
    }
}