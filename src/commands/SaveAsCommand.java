package commands;
import handlers.CommandHandler;
import models.Table;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveAsCommand implements Command {
    private CommandHandler commandHandler;

    public SaveAsCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        try {
            String filePath = args[1];
            File file = new File(filePath);
            File directory = file.getParentFile();

            if (args.length < 2) {
                System.out.println("Invalid file path.");
                return;
            }

            if (file.isDirectory()) {
                System.out.println("No file name provided.");
                return;
            }

            if (file.exists()) {
                System.out.println("File already exists.");
                return;
            }

            if (directory == null) {
                System.out.println("Invalid file path.");
                return;
            }

//            if (!directory.exists()) {
//                directory.mkdirs();
//            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Table table : commandHandler.getDatabase().getTables()) {
                    writer.write(table.getFilePath() + "," + table.getTableName());
                    writer.newLine();
                }
                System.out.println("Successfully saved as " + filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid file path.");
        }
    }
}