package commands;
import handlers.CommandHandler;
import models.Table;

public class ShowTablesCommand implements Command{
    private CommandHandler commandHandler;

    public ShowTablesCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {
        for (Table table : commandHandler.getDatabase().getTables()) {
            System.out.println(table.getTableName());
        }
    }
}

