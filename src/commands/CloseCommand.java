package commands;
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
        if (currentFile != null) {
            System.out.println("Closing file: " + currentFile.getName());
            commandHandler.setCurrentFile(null);
            System.gc();
        } else {
            System.out.println("No file is currently open.");
        }
    }
}
