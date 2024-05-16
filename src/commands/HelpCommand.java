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
                "open <file>      opens a database file\n" +
                "close            closes currently open database\n" +
                "save             saves the currently open database\n" +
                "saveas <path>    saves the currently open database in filepath\n" +
                "help             prints this information\n" +
                "exit             exit the program\n\n" +
                "import <file>    imports table in the database from filepath\n"+
                "showtables       shows all loaded tables in the database\n" +
                "describe <table> shows the name and types of the columns\n" +
                "print <table>    prints the content of the table\n" +
                "export <table> <path> " +
                "\n\t\t\t\t exports the table in the filepath\n" +
                "select <column-n> <value> <table> " +
                "\n\t\t\t\t prints the rows where the columns store the value\n" +
                "addcolumn <table> <column-name> <column-type> \n\t\t\t\t adds a new column to the table\n"

        );
    }
}
