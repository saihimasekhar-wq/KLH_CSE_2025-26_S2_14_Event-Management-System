import java.util.*;

/**
 * EventSearchEngine.java
 *
 * Uses the Z-Algorithm to search for a keyword across ALL loaded event files.
 *
 * For each file where the keyword is found:
 *   1. Reports WHICH file matched and WHERE (positions)
 *   2. Returns the full content of that event file
 */
public class EventSearchEngine {

    private List<EventLoader.Event> events;

    /**
     * SearchResult stores the result for one matched event file.
     */
    public static class SearchResult {
        public EventLoader.Event event;      // The matched event
        public int[]  matchPositions;        // All positions (0-indexed) in the text
        public int    matchCount;            // Total number of matches

        public SearchResult(EventLoader.Event event, int[] positions) {
            this.event          = event;
            this.matchPositions = positions;
            this.matchCount     = positions.length;
        }
    }

    /**
     * Constructor.
     * @param events  List of pre-loaded Event objects
     */
    public EventSearchEngine(List<EventLoader.Event> events) {
        this.events = events;
    }

    /**
     * Searches for the given keyword in all event files using the Z-Algorithm.
     *
     * @param keyword  The search term (case-insensitive)
     * @return         List of SearchResult, one per matching event file
     */
    public List<SearchResult> search(String keyword) {
        List<SearchResult> results = new ArrayList<>();

        if (keyword == null || keyword.trim().isEmpty()) {
            System.out.println("[SearchEngine] Please enter a valid keyword.");
            return results;
        }

        String trimmedKeyword = keyword.trim();

        for (EventLoader.Event event : events) {
            // Use Z-Algorithm to find all occurrences of keyword in this event's content
            int[] positions = ZAlgorithm.search(event.fullContent, trimmedKeyword);

            if (positions.length > 0) {
                results.add(new SearchResult(event, positions));
            }
        }

        return results;
    }
}
