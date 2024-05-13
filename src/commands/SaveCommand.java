package commands;
import handlers.CommandHandler;
import models.Table;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class SaveCommand implements Command {
    private CommandHandler commandHandler;

    public SaveCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }


    @Override
    public void execute(String[] args) {
        File currentFile = commandHandler.getCurrentFile();
        if (currentFile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
                for (Table table : commandHandler.getDatabase().getTables()) {
                    String filePath = table.getFilePath();
                    writer.write(filePath + "," + table.getTableName());
                    writer.newLine();
                }
                System.out.println("Successfully saved " + currentFile.getName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
//    @Override
//    public void execute(String[] args) {
//        File currentFile = commandHandler.getCurrentFile();
//        if (currentFile != null) {
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))) {
//                for (int i = 0; i < commandHandler.getCurrentTable().getColumnName().size();i++) {
//                    writer.write(commandHandler.getCurrentTable().getColumnName().get(i) + " " + commandHandler.getCurrentTable().getDataType().get(i));
//                    if (i < commandHandler.getCurrentTable().getColumnName().size() - 1) {
//                        writer.write(", ");
//                    }
//                }
//                System.out.println("Successfully saved " + currentFile.getName());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//}
