import commands.OpenCommand;

import java.io.IOException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws IOException {
        OpenCommand openCommand = new OpenCommand();
        String[] path = new String[]{"/Users/bubliok/oop-project/csv/database.csv"};
        openCommand.execute(path);
    }
}