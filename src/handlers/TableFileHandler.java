package handlers;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

// TableFileHandler.java
public interface TableFileHandler {
    void writeRow(List<Object> values) throws IOException;
    String getFilePath();
}

