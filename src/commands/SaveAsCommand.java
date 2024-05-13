package commands;
import handlers.CommandHandler;
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
        try{
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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < commandHandler.getCurrentTable().getColumnName().size(); i++) {
                writer.write(commandHandler.getCurrentTable().getColumnName().get(i) + " " + commandHandler.getCurrentTable().getDataType().get(i));
                if (i < commandHandler.getCurrentTable().getColumnName().size() - 1) {
                    writer.write(", ");
                }
            }
            System.out.println("Successfully saved as " + filePath);
        } catch (IOException e) {
            System.out.println("Error: Unable to save as " + filePath);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid file path.");
        }
    }
}