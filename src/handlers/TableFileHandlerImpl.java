package handlers;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TableFileHandlerImpl implements TableFileHandler {
    private String tableFilename;

    public TableFileHandlerImpl(String tableFilename) {
        this.tableFilename = tableFilename+".xml";
    }

    public String getTableFilename() {
        return tableFilename;
    }

    @Override
    public void writeRow(List<Object> values) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tableFilename, true))) {
            for (Object value : values) {
                writer.write(value.toString() + ",");
            }
            writer.newLine();
        }
    }
}
