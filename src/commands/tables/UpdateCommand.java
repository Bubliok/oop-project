package commands.tables;

import commands.Command;
import handlers.DatabaseHandler;
import models.Column;
import models.Database;
import models.Row;
import models.Table;
import utils.TableWriter;
import utils.TypeValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UpdateCommand implements Command {
    private DatabaseHandler databaseHandler;

    public UpdateCommand(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 6){
            System.out.println("Invalid arguments. See 'help' for more information.");
            return;
        }

        String tableName = args[1];
        int searchColumn = Integer.parseInt(args[2]) - 1;
        String searchValue = args[3];
        int targetColumn = Integer.parseInt(args[4]) - 1;
        String targetValue = args[5];
        Database database = databaseHandler.getDatabase();
        TableWriter writer = new TableWriter();

        Table table = database.getTable(tableName);
        if (table == null){
            System.out.println("Table " + tableName + " does not exist.");
            return;
        }
        String filePath = table.getTablePath();
        List<Column> columns = table.getColumns();
        List<Row> rows = table.getRows();
        List<Row> updatedRows = new ArrayList<>();

        if (searchColumn < 0 || searchColumn >= columns.size() || targetColumn < 0 || targetColumn >= columns.size()) {
            System.out.println("Invalid column index.");
            return;
        }

        try {
            TypeValidator.typeValidator(targetValue, columns.get(targetColumn).getColumnType());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid data type.");
            return;
        }

        boolean flag = false;
        for (Row row : rows) {
            Object value = row.getValues().get(searchColumn);
            if (value.toString().equals(searchValue)){
                row.getValues().set(targetColumn, targetValue);
                flag = true;
            }
            updatedRows.add(row);
        }
        table.setRows(updatedRows);

        try {
            writer.writeTable(table, updatedRows, filePath);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        if (!flag){
            System.out.println("Search value not found.");
        } else {
            System.out.println("Rows updated successfully.");
        }
    }
}
