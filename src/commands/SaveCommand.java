package commands;
import handlers.CommandHandler;
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
                for (int i = 0; i < commandHandler.getCurrentTable().getColumnName().size(); i++) {
                    writer.write(commandHandler.getCurrentTable().getColumnName().get(i) + " " + commandHandler.getCurrentTable().getDataType().get(i));
                    if (i < commandHandler.getCurrentTable().getColumnName().size() - 1) {
                        writer.write(", ");
                    }
                }
                System.out.println("Successfully saved " + currentFile.getName());
            } catch (IOException e) {
                System.out.println("An error occurred.");
                throw new RuntimeException(e);
            }
        }
    }
}
