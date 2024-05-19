package commands.databases;
import commands.Command;
import handlers.CommandHandler;
import models.Table;

public class ShowTablesCommand implements Command {
    private CommandHandler commandHandler;

    public ShowTablesCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }


    @Override
    public void execute(String[] args) {
        if (commandHandler.getDatabase().getTables().isEmpty()) {
            System.out.println("No tables to show.");
        } else {
            for (Table table : commandHandler.getDatabase().getTables()) {
                System.out.println(table.getTableName());
            }
        }
    }
}
