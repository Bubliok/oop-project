package commands;
import handlers.CommandHandler;
public class HelpCommand implements Command {

    private CommandHandler commandHandler;

    public HelpCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        System.out.println("The following commands are supported:\n" +
                "open <file>   opens a file\n" +
                "close         closes currently opened file\n" +
                "save          saves the currently open file\n" +
                "saveas <path> saves the currently open file in <path>\n" +
                "help          prints this information\n" +
                "exit          exists the program\n");
    }
}
