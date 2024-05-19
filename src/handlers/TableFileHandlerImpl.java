package handlers;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class TableFileHandlerImpl implements TableFileHandler {
    private String tableFilename;

    public TableFileHandlerImpl(String tableFilename) {
        this.tableFilename = tableFilename+".xml";
    }

    public String getTableFilename() {
        return tableFilename;
    }

    public String getFilePath() {
        return Paths.get("").toAbsolutePath().toString()+ "/" + getTableFilename();
    }
    public String setFilePath(String path) {
        File file = new File(path);
        this.tableFilename = file.getName();
        return getFilePath();
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
//    public void renameTableFile(String newName) {
//        File oldFile = new File(getFilePath());
//        String parentPath = oldFile.getParent();
//        File newFile = new File(parentPath + File.separator + newName + ".xml");
//        if (oldFile.renameTo(newFile)) {
//            System.out.println("File renamed successfully");
//            this.tableFilename = newName + ".xml";
//        } else {
//            System.out.println("Failed to rename file");
//        }
//    }
}
