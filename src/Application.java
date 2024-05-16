import handlers.CommandHandler;
import models.Database;
import models.Table;

import java.io.IOException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws IOException {
//        Database database = new Database("school");
//        Table table1 = new Table("students");
//        Table table2 = new Table("teachers");
//        database.addTable(table1);
//        database.addTable(table2);
//
        CommandHandler commandHandler = new CommandHandler();
//        table1.addColumn("student_id", "int");
//        table1.addColumn("name", "string");
//        table1.addColumn("age", "int");
       // table1.addRow(List.of(1, "Alice", 20));
//
//        table2.addColumn("teacher_id", "int");
//        table2.addColumn("name", "string");
//        table2.addColumn("subject", "string");
//        table2.addRow(List.of(1, "Bob", "Math"));

        Scanner scanner = new Scanner(System.in);


        String command;
        while (!(command = scanner.nextLine()).equals("exit")) {
            commandHandler.handleCommand(command);
        }
        System.out.println("Exiting the program...");
        scanner.close();
    }
}