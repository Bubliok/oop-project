import commands.OpenCommand;
import handlers.CommandHandler;
import utils.TableLoader;

import java.io.IOException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws IOException {
//        OpenCommand openCommand = new OpenCommand();
//        String[] path = new String[]{"/Users/bubliok/oop-project/csv/database.csv"};
//        openCommand.execute(path);

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