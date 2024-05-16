package commands;
import handlers.CommandHandler;
import models.Table;

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


//    @Override
//    public void execute(String[] args) {
//        if (args.length < 2) {
//            System.out.println("Invalid command. Please provide a file path.");
//            return;
//        }
//
//        String filePath = args[1];
//        File file = new File(filePath);
//        if (!file.exists()) {
//            System.out.println("File does not exist: " + filePath);
//            return;
//        }
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length == 2) {
//                    String tableFilePath = parts[0];
//                    File tableFile = new File(tableFilePath);
//                    if (!tableFile.exists()) {
//                        System.out.println("Table file does not exist: " + tableFilePath);
//                        continue;
//                    }
//                    commandHandler.setCurrentFile(file);
//                    System.out.println("Successfully opened " + tableFilePath);
//
//                    String tableName = tableFilePath.substring(tableFilePath.lastIndexOf("/") + 1, tableFilePath.lastIndexOf("."));
//                    Table table = new Table(tableName);
//                    table.loadFromFile(tableFilePath);
//                    commandHandler.getDatabase().addTable(table);
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Error when opening table: "+ e.getMessage());
//            return;
//        }
//    }
//}