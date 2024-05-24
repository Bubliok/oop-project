import commands.OpenCommand;

import java.io.IOException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws IOException {
        OpenCommand openCommand = new OpenCommand();
        private String databaseFile = "csv/grades.csv";
        openCommand.execute(databaseFile);
    }
}