package commands.databases;

import commands.Command;
import handlers.CommandHandler;

public class SaveAsCommand implements Command {
    CommandHandler commandHandler;

    public SaveAsCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {

    }
}
