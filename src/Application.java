import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.List;

public class Application {
    public static void main(String[] args) throws IOException {
        Database database = new Database("school");
        Table table1 = new Table("students");
        table1.addColumn("student_id", "int");
        table1.addColumn("name", "string");
        table1.addColumn("age", "int");
        table1.addRow(List.of(1, "Alice", 20));
        table1.printRow(0);
    }
}
