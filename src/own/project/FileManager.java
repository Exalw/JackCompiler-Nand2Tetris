package own.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileManager {
    private String fileName;
    private String finalname;
    private Path p;
    private Path fP;
    private Path fP2;

    public FileManager(String fileName) {
        this.fileName = fileName;
        this.p = Paths.get("").toAbsolutePath().resolve(fileName);
        this.finalname = fileName.substring(0, fileName.length() - 5) + ".xml";
        String finalname2 = fileName.substring(0, fileName.length() - 5) + ".token";
        this.fP = Paths.get("").toAbsolutePath().resolve(this.finalname);
        this.fP2 = Paths.get("").toAbsolutePath().resolve(finalname2);
    }

    public FileManager(String fileDir, String choosenName) {
        fileName = fileDir;
        this.p = Paths.get("").toAbsolutePath().resolve(fileDir);
        String ogName = fileDir.substring(0, fileDir.length() - 5);
        String[] file = fileDir.split("/");
        String fN = file[file.length-1].substring(0, file[file.length-1].length() - 5);
        finalname = ogName.substring(0, ogName.length()-fN.length()) + choosenName + ".xml";
        this.fP = Paths.get("").toAbsolutePath().resolve(finalname);
    }

    public List<String> getStringArray() {
        try {
            List<String> zeilListe = Files.readAllLines(this.p);
            return zeilListe;
        } catch (IOException var3) {
            var3.printStackTrace();
            System.err.println(var3);
            return null;
        }
    }

    public void saveFile(List<String> binCode) {
        if (Files.notExists(this.fP, new LinkOption[0])) {
            try {
                Files.createFile(this.fP);
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }
        try {
            Files.write(this.fP, binCode);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }

    public void saveFile2(List<String> binCode) {
        if (Files.notExists(this.fP2, new LinkOption[0])) {
            try {
                Files.createFile(this.fP2);
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }
        try {
            Files.write(this.fP2, binCode);
        } catch (IOException var3) {
            var3.printStackTrace();
        }
    }
}