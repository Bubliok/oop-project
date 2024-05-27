package handlers;

import commands.Command;
import commands.HelpCommand;
import commands.databases.*;
import commands.tables.*;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandHandler {
    private Map<String, Command> commands;
    private File currentFile;
    private DatabaseHandler databaseHandler;

    public CommandHandler() {
        databaseHandler = new DatabaseHandler();
        commands = new HashMap<>();
        commands.put("open", new OpenCommand(this, databaseHandler));
        commands.put("close", new CloseCommand(this));
        commands.put("save", new SaveCommand(this, databaseHandler));
        commands.put("saveas", new SaveAsCommand(databaseHandler));
        commands.put("help", new HelpCommand());

        commands.put("import", new ImportCommand(databaseHandler));
        commands.put("showtables", new ShowTablesCommand(databaseHandler));
        commands.put("describe", new DescribeCommand(databaseHandler));
        commands.put("print", new PrintCommand(databaseHandler));
        commands.put("export", new ExportCommand(databaseHandler));
        commands.put("select", new SelectCommand(databaseHandler));
        commands.put("addcolumn", new AddColumnCommand(databaseHandler));
        commands.put("update", new UpdateCommand(databaseHandler));
        commands.put("delete", new DeleteCommand(databaseHandler));
        commands.put("insert", new InsertCommand(databaseHandler));
        commands.put("rename", new RenameCommand(this,databaseHandler));
        commands.put("count", new CountCommand(databaseHandler));
        commands.put("innerjoin", new InnerJoinCommand(databaseHandler));
        commands.put("aggregate", new AggregateCommand(databaseHandler));
    }

    public void handleCommand(String command) {
        if(currentFile == null && !command.startsWith("open") && !command.startsWith("help")) {
            System.out.println("No file is currently open.");
            return;
        }
        try {
            Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
            Matcher matcher = pattern.matcher(command);
            //StringBuilder sb = new StringBuilder();
            List<String> partsList = new ArrayList<>();

            while (matcher.find()) {
                if (matcher.group(1) != null) {
                    //sb.append(matcher.group(1));
                    partsList.add(matcher.group(1));//
                } else {
//                sb.append(matcher.group(2));
                    partsList.add(matcher.group(2));
                }
            }

            String[] parts = partsList.toArray(new String[0]);
            if (parts.length > 0 && commands.containsKey(parts[0])) {
                commands.get(parts[0]).execute(parts);
            } else {
                System.out.println("Unknown command. Type 'help' for a list of commands.");
            }
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return;
        }
//        String[] parts = command.split(" ");
//        if (commands.containsKey(parts[0])) {
//            commands.get(parts[0]).execute(parts);
//        } else {
//            System.out.println("Unknown command. Type 'help' for a list of commands.");
//        }
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

}
