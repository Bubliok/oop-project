package handlers;

import commands.Command;
import commands.OpenCommand;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private Map<String, Command> commands;

    public CommandHandler() {
        commands = new HashMap<>();
        commands.put("open", new OpenCommand());
    }

    public void handleCommand(String command) throws FileNotFoundException {
        //if(currentFile == null && !command.startsWith("open") && !command.startsWith("help")) {
//        if(!command.startsWith("open") && !command.startsWith("help")) {
//            System.out.println("No file is currently open.");
//            return;
//        }
        String[] parts = command.split(" ");
        if (commands.containsKey(parts[0])) {
            commands.get(parts[0]).execute(parts);
        } else {
            System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }

}
