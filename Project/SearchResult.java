package Project;

import java.io.File;

public class SearchResult {

    private File file;
    private int occurrenceCount;

    public SearchResult(File file, int occurrenceCount) {
        this.file = file;
        this.occurrenceCount = occurrenceCount;
    }

    public File getFile() {
        return file;
    }

    public int getOccurrenceCount() {
        return occurrenceCount;
    }
}