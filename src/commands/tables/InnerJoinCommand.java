package commands.tables;

import commands.Command;
import handlers.DatabaseHandler;
import models.Column;
import models.Database;
import models.Row;
import models.Table;
import utils.TableWriter;

import java.io.IOException;
import java.util.List;

public class InnerJoinCommand implements Command {
    private DatabaseHandler databaseHandler;

    public InnerJoinCommand(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 5) {
            System.out.println("Invalid arguments. See 'help' for more information.");
            return;
        }

        String tableName1 = args[1];
        int column1 = Integer.parseInt(args[2]) - 1;
        String tableName2 = args[3];
        int column2 = Integer.parseInt(args[4]) - 1;
        Database database = databaseHandler.getDatabase();


        Table table1 = database.getTable(tableName1);
        Table table2 = database.getTable(tableName2);

        if (table1 == null || table2 == null){
            System.out.println("Table does not exist.");
            return;
        }

        if (database.getTable("innerjoin") != null) {
            database.removeTable(database.getTable("innerjoin"));
        }
        String filePath = "innerjoin.csv";
        Table newTable = new Table("innerjoin",filePath);
        List<Column> table1Columns = table1.getColumns();
        List<Column> table2Columns = table2.getColumns();
        TableWriter writer = new TableWriter();

        System.out.println("Selected column 1: " + table1Columns.get(column1).getColumnName());
        System.out.println("Selected column 2: " +table2Columns.get(column2).getColumnName());

        if (column1 < 0 || column1 >= table1Columns.size() || column2 < 0 || column2 >= table2Columns.size()) {
            System.out.println("Invalid column index.");
            return;
        }

        for (Column column : table1.getColumns()) {
            newTable.addColumn(new Column(column.getColumnName(), column.getColumnType()));
        }

        for (int i = 0; i < table2.getColumns().size(); i++) {
            if (i != column2) {
                Column column = table2.getColumns().get(i);
                newTable.addColumn(new Column(column.getColumnName(), column.getColumnType()));
            }
        }

        for (Row row1 : table1.getRows()) {
            for (Row row2 : table2.getRows()) {
                if (row1.getValues().get(column1).equals(row2.getValues().get(column2))) {
                    Row newRow = new Row();
                    newRow.getValues().addAll(row1.getValues());
                    for (int i = 0; i < row2.getValues().size(); i++) {
                        if (i != column2) {
                            newRow.getValues().add(row2.getValues().get(i));
                        }
                    }
                    newTable.getRows().add(newRow);
                }
            }
        }
        database.addTable(newTable);
        try {
            writer.writeTable(newTable, newTable.getRows(), filePath);
        } catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("New table created: " + newTable.getTablePath());
    }
}
