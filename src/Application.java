//TODO  maybe add a database handler so to remove the need for passing it in the command handler as an argument
import handlers.CommandHandler;
import java.io.IOException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws IOException {


        CommandHandler commandHandler = new CommandHandler();
        Scanner scanner = new Scanner(System.in);
        
        String command;
        while(!(command = scanner.nextLine()).equals("exit")){
            commandHandler.handleCommand(command);
        }
        System.out.println("Exiting the program...");
        scanner.close();
    }
}