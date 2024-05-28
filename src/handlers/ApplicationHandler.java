package handlers;
import java.util.Scanner;

public class ApplicationHandler {

    public void startApp() {
        CommandHandler commandHandler = new CommandHandler();

        Scanner scanner = new Scanner(System.in);

        String command;
        while (!(command = scanner.nextLine()).equals("exit")) {
            commandHandler.handleCommand(command);
        }
        System.out.println("Exiting the program...");
        scanner.close();
    }
}