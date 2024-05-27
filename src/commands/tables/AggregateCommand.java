package commands.tables;

import commands.Command;
import handlers.DatabaseHandler;
import models.Column;
import models.Database;
import models.Row;
import models.Table;

import java.util.ArrayList;
import java.util.List;

public class AggregateCommand implements Command {
    private DatabaseHandler databaseHandler;

    public AggregateCommand(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 5){
            System.out.println("Invalid arguments. See 'help' for more information.");
            return;
        }

        String tableName = args[1];
        int searchColumn = Integer.parseInt(args[2]) -1;
        String searchValue = args[3];
        int targetColumn = Integer.parseInt(args[4]) -1;
        String operation = args[5];
        Database database = databaseHandler.getDatabase();
        Table table = database.getTable(tableName);

        if (table == null){
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }

        List<Row> rows = table.getRows();
        List<Column> searchColumns = table.getColumns();
        List<Column> targetColumns = table.getColumns();
        if (searchColumn < 0 || searchColumn >= searchColumns.size() || targetColumn < 0 || targetColumn >= targetColumns.size()) {
            System.out.println("Invalid column index.");
            return;
        }

        List<Double> results = new ArrayList<>();
        for (Row row : rows){
            if (row.getValues().get(searchColumn).equals(searchValue)){
                try {
                double value = Double.parseDouble(row.getValues().get(targetColumn).toString());
                results.add(value);
                } catch (NumberFormatException e){
                    System.out.println("Invalid data type. Values must be numerical.");
                    return;
                }
            }
        }
        double result = 0;
        switch (operation) {
            case "sum":
                for (double value : results){
                    result += value;
                }
                break;
            case "product":
                result = 1;
                for (double value : results){
                    result *= value;
                }
                break;
            case "maximum":
                for (double value : results){
                    result = Math.max(result,value);
                }
                break;
            case "minimum":
                result = Double.POSITIVE_INFINITY;//nai-golqmoto vuzmojno chislo
                for (double value : results){
                    result = Math.min(result,value);//min mjd dvete
                }
                break;
            default:
                System.out.println("Invalid operation. See 'help' for more information.");
                return;
        }
        System.out.println("Result of the operation " + operation + " is " + result);
    }
}
