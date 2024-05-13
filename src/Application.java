import handlers.CommandHandler;
import models.Database;
import models.Table;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws IOException {
       //Database database = new Database("school");
        Table table1 = new Table("students");
        CommandHandler commandHandler = new CommandHandler(table1);
        table1.addColumn("student_id", "int");
        table1.addColumn("name", "string");
        table1.addColumn("age", "int");
        table1.addRow(List.of(1, "Alice", 20));
        table1.printRow(0);

        Scanner scanner = new Scanner(System.in);

        String command;
        while (!(command = scanner.nextLine()).equals("exit")) {
            commandHandler.handleCommand(command);
        }
        System.out.println("Exiting the program...");
        scanner.close();
    }
}