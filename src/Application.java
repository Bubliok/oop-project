import handlers.CommandHandler;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {


        CommandHandler commandHandler = new CommandHandler();
        Scanner scanner = new Scanner(System.in);

        String command1 = "open database.csv";

        String command;
            commandHandler.handleCommand(command1);
        while(!(command = scanner.nextLine()).equals("exit")){
            commandHandler.handleCommand(command);
        }
        System.out.println("Exiting the program...");
        scanner.close();
    }
}