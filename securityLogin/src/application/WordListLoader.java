package application;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordListLoader {
    public static Set<String> loadWords(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        return new HashSet<>(lines);
    }
}