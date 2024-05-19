package commands;
import handlers.CommandHandler;
public class HelpCommand implements Command {

    private CommandHandler commandHandler;

    public HelpCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void execute(String[] args) {

        String helpText = "The following commands are supported:\n" +
                "  open <file>     \n" +
                "      Opens a database file\n" +
                "  close           \n" +
                "      Closes the currently open database\n" +
                "  save            \n" +
                "      Saves the currently open database\n" +
                "  saveas <path>   \n" +
                "      Saves the currently open database to the specified filepath\n" +
                "  help            \n" +
                "      Prints this information\n" +
                "  exit            \n" +
                "      Exits the program\n" +
                "\n" +
                "  import <file>   \n" +
                "      Imports a table into the database from the specified filepath\n" +
                "  showtables      \n" +
                "      Shows all loaded tables in the database\n" +
                "  describe <table>\n" +
                "      Shows the name and types of the columns in the specified table\n" +
                "  print <table>   \n" +
                "      Prints the content of the specified table\n" +
                "  export <table> <path>\n" +
                "      Exports the specified table to the specified filepath\n" +
                "\n" +
                "  select <column-n> <value> <table>\n" +
                "      Prints the rows where the specified column stores the specified value\n" +
                "  addcolumn <table> <column-name> <column-type>\n" +
                "      Adds a new column to the specified table\n" +
                "  update <table> <search column-n> <search value> <target column-n> <target value>\n" +
                "      Updates the target column of every row where the search column stores the search value\n" +
                "  delete <table> <column-n> <value>\n" +
                "      Deletes all rows where the specified column stores the specified value\n" +
                "  insert <table> <values>\n" +
                "      Inserts a new row into the specified table\n" +
                "  aggregate <table> <search column-n> <search value> <target column-n> <operation>\n" +
                "      Performs the specified operation on the target column of the rows where the search column stores the search value\n" +
                "  count <table> <column-n> <value>\n" +
                "      Counts the rows where the specified column stores the specified value\n" +
                "  rename <table> <old column name> <new column name>\n" +
                "      Renames the specified column in the table\n" +
                "  innerjoin <table1> <table2> <column-n1> <column-n2>\n" +
                "      Joins the two tables on the specified columns\n";

        System.out.println(helpText);
    }
}
