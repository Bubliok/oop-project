import java.util.ArrayList;
import java.util.List;

public class Table {

    private String tableName;
    private List<String> columnName;
    private List<String> dataType;
    private List<Row> row;

    public Table(String tableName) {
        this.tableName = tableName;
        this.columnName = new ArrayList<>();
        this.dataType = new ArrayList<>();
        this.row = new ArrayList<>();
    }


    public void addRow(List<Object> values){
        if(values.size() != columnName.size()){
            throw new IllegalArgumentException("Invalid number of values.");
        }
        Row newRow = new Row(values);
        row.add(newRow);
    }
    public void addColumn(String column, String type) {
        if (!type.equals("int") && !type.equals("string") && !type.equals("float")) {
            throw new IllegalArgumentException("Invalid data type.");
        }
        columnName.add(column);
        dataType.add(type);
        for(Row row : row) {
            row.addValue(null);
        }
        System.out.println("Column "+ column +" of type " + type +" successfully.");
    }
    public void printRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= row.size()) {
            throw new IllegalArgumentException("Invalid row index.");
        }
        Row targetRow = row.get(rowIndex);
        for (int i = 0; i < columnName.size(); i++) {
            System.out.println(columnName.get(i) + ": " + targetRow.getValues().get(i));
        }
    }
}

