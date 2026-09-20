package Project;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EventSearchEngine {

    public List<SearchResult> searchFiles(String searchText) {

        List<SearchResult> results = new ArrayList<>();

        File dataFolder = new File("data");

        if (!dataFolder.exists() || !dataFolder.isDirectory()) {
            return results;
        }

        File[] files = dataFolder.listFiles();

        if (files == null) {
            return results;
        }

        for (File file : files) {

            if (!file.isFile()) {
                continue;
            }

            if (!file.getName().toLowerCase().endsWith(".txt")) {
                continue;
            }

            try {

                String content =
                        Files.readString(file.toPath());

                int count =
                        ZPatternSearch.countOccurrences(
                                content,
                                searchText
                        );

                if (count > 0) {

                    results.add(
                            new SearchResult(file, count)
                    );
                }

            } catch (IOException e) {

                System.out.println(
                        "Could not read: "
                        + file.getName()
                );
            }
        }

        // Highest occurrence count first
        results.sort(
                Comparator
                        .comparingInt(
                                SearchResult::getOccurrenceCount
                        )
                        .reversed()
                        .thenComparing(
                                result ->
                                        result.getFile().getName()
                        )
        );

        return results;
    }


    public void readFile(SearchResult result) {

        File file = result.getFile();

        System.out.println();
        System.out.println(
                "============================================================"
        );

        System.out.println(
                "FILE: " + file.getName()
        );

        System.out.println(
                "============================================================"
        );

        try {

            String content =
                    Files.readString(file.toPath());

            System.out.println();
            System.out.println(content);

        } catch (IOException e) {

            System.out.println(
                    "Error reading file: "
                    + file.getName()
            );
        }

        System.out.println(
                "============================================================"
        );
    }
}