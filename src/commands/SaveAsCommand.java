package commands;
import handlers.CommandHandler;

public class SaveAsCommand implements Command{
    private CommandHandler commandHandler;
    public SaveAsCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
    @Override
    public void execute(String[] args) {
        System.out.println("Saving file as: " + args[1]);
    }
}
