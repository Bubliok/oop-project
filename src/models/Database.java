package models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<Table> tables;
    private String databaseName;

    public Database(String databaseName) {
        this.databaseName = databaseName;
        this.tables = new ArrayList<>();
    }

}
