package handlers;

import models.Database;
import utils.DatabaseLoader;

public class DatabaseHandler {
    private Database database;
    private DatabaseLoader databaseLoader;

    public DatabaseHandler() {
        this.database = new Database();
        this.databaseLoader = new DatabaseLoader(database);
    }

    public void loadDatabase(String filePath) {
        databaseLoader.loadDatabase(filePath);
    }

    public Database getDatabase() {
        return database;
    }
}
