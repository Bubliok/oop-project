package commands;
import handlers.CommandHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OpenCommand implements Command {
    private CommandHandler commandHandler;


    public OpenCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        String filename = args[1];
        new File(filename);
        File currentFile = new File(filename);
        if (!currentFile.exists()) {
            System.out.println("File not found: " + filename);
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        commandHandler.setCurrentFile(currentFile);
        System.out.println("Opening file: " + filename);
    }
}