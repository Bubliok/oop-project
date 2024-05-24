package commands;

import models.Row;
import models.Table;
import utils.DatabaseLoader;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenCommand implements Command {
    private Map<String, Table> tables;
    private DatabaseLoader databaseLoader;

    public OpenCommand() {
        tables = new HashMap<>();
        databaseLoader = new DatabaseLoader(tables);
    }

    @Override
    public void execute(String[] args) {
        databaseLoader.loadDatabase(args[0]);
    }
}