import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * EventLoader.java
 *
 * Loads all event .txt files from the data/ folder.
 * Each file must have:
 *   - First line: EVENT: <EventName>
 *   - A line: CHARACTERS: <char1>, <char2>, ...
 *   - A line: RELATED: <file1>, <file2>, ...
 *   - Rest of file: event description content
 */
public class EventLoader {

    /**
     * Represents a single loaded event.
     */
    public static class Event {
        public String fileName;       // e.g. "pongal"
        public String eventName;      // e.g. "Pongal"
        public String fullContent;    // entire file text
        public List<String> characters; // e.g. ["Sun God", "Surya"]
        public List<String> related;    // e.g. ["makar_sankranti", "ugadi"]

        public Event(String fileName, String eventName, String fullContent,
                     List<String> characters, List<String> related) {
            this.fileName   = fileName;
            this.eventName  = eventName;
            this.fullContent = fullContent;
            this.characters = characters;
            this.related    = related;
        }

        @Override
        public String toString() {
            return "Event[" + eventName + "]";
        }
    }

    /**
     * Loads all .txt files from the given directory.
     *
     * @param dataFolderPath  Absolute or relative path to the data/ folder
     * @return                List of loaded Event objects
     */
    public static List<Event> loadAllEvents(String dataFolderPath) {
        List<Event> events = new ArrayList<>();
        File folder = new File(dataFolderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("[EventLoader] ERROR: data folder not found: " + dataFolderPath);
            return events;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null || files.length == 0) {
            System.err.println("[EventLoader] No .txt files found in: " + dataFolderPath);
            return events;
        }

        for (File file : files) {
            try {
                Event event = parseEventFile(file);
                if (event != null) {
                    events.add(event);
                }
            } catch (IOException e) {
                System.err.println("[EventLoader] Failed to read: " + file.getName() + " - " + e.getMessage());
            }
        }

        System.out.println("[EventLoader] Loaded " + events.size() + " event(s) from: " + dataFolderPath);
        return events;
    }

    /**
     * Parses a single event .txt file into an Event object.
     */
    private static Event parseEventFile(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        if (lines.isEmpty()) return null;

        String eventName  = "Unknown";
        List<String> characters = new ArrayList<>();
        List<String> related    = new ArrayList<>();

        // Parse metadata from first few lines
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("EVENT:")) {
                eventName = trimmed.substring("EVENT:".length()).trim();
            } else if (trimmed.startsWith("CHARACTERS:")) {
                String charPart = trimmed.substring("CHARACTERS:".length()).trim();
                for (String c : charPart.split(",")) {
                    String ch = c.trim();
                    if (!ch.isEmpty()) characters.add(ch);
                }
            } else if (trimmed.startsWith("RELATED:")) {
                String relPart = trimmed.substring("RELATED:".length()).trim();
                for (String r : relPart.split(",")) {
                    String rel = r.trim();
                    if (!rel.isEmpty()) related.add(rel);
                }
            }
        }

        // Full content = entire file as a string
        String fullContent = String.join("\n", lines);

        // File name without extension (e.g., "pongal")
        String fileName = file.getName().replace(".txt", "");

        return new Event(fileName, eventName, fullContent, characters, related);
    }
}
