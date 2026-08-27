import java.util.*;

/**
 * RecommendationEngine.java
 *
 * Suggests related events based on:
 *   1. RELATED: tags in the event file (direct links defined by the author)
 *   2. Shared CHARACTERS: between the matched event and all other events
 *      (e.g., Rama appears in ramanavami.txt AND dasara.txt AND diwali.txt
 *       → searching "ramanavami" also recommends dasara and diwali)
 *
 * The final recommendation list is de-duplicated and sorted by relevance score.
 */
public class RecommendationEngine {

    private List<EventLoader.Event> allEvents;

    /**
     * Constructor.
     * @param allEvents  Full list of all loaded events
     */
    public RecommendationEngine(List<EventLoader.Event> allEvents) {
        this.allEvents = allEvents;
    }

    /**
     * Represents a single recommendation with a reason and relevance score.
     */
    public static class Recommendation {
        public EventLoader.Event event;
        public String reason;
        public int score;   // higher = more relevant

        public Recommendation(EventLoader.Event event, String reason, int score) {
            this.event  = event;
            this.reason = reason;
            this.score  = score;
        }
    }

    /**
     * Generates recommendations for a list of matched search results.
     * Excludes the events that already appeared in search results.
     *
     * @param searchResults  Events that matched the search keyword
     * @return               Sorted list of recommendations
     */
    public List<Recommendation> recommend(List<EventSearchEngine.SearchResult> searchResults) {
        // Build set of already-matched event file names (to exclude from recommendations)
        Set<String> matchedFiles = new HashSet<>();
        for (EventSearchEngine.SearchResult result : searchResults) {
            matchedFiles.add(result.event.fileName);
        }

        // Map: fileName → Recommendation (for de-duplication with score accumulation)
        Map<String, Recommendation> recommendationMap = new LinkedHashMap<>();

        for (EventSearchEngine.SearchResult result : searchResults) {
            EventLoader.Event matchedEvent = result.event;

            // --- Strategy 1: Direct RELATED: links ---
            for (String relatedFile : matchedEvent.related) {
                if (!matchedFiles.contains(relatedFile)) {
                    EventLoader.Event relatedEvent = findEventByFileName(relatedFile);
                    if (relatedEvent != null) {
                        String reason = "Directly related to \"" + matchedEvent.eventName + "\"";
                        addOrUpdate(recommendationMap, relatedEvent, reason, 10);
                    }
                }
            }

            // --- Strategy 2: Shared characters ---
            for (String character : matchedEvent.characters) {
                for (EventLoader.Event otherEvent : allEvents) {
                    if (matchedFiles.contains(otherEvent.fileName)) continue;
                    if (otherEvent.fileName.equals(matchedEvent.fileName)) continue;

                    // Check if this character appears in the other event's CHARACTERS list
                    // or in the file content (using Z-Algorithm for extra thoroughness)
                    boolean sharedChar = false;
                    for (String otherChar : otherEvent.characters) {
                        if (otherChar.equalsIgnoreCase(character)) {
                            sharedChar = true;
                            break;
                        }
                    }
                    if (!sharedChar) {
                        // Also check if character name appears anywhere in the other event's content
                        sharedChar = ZAlgorithm.contains(otherEvent.fullContent, character);
                    }

                    if (sharedChar) {
                        String reason = "Shares the character \"" + character
                                + "\" with \"" + matchedEvent.eventName + "\"";
                        addOrUpdate(recommendationMap, otherEvent, reason, 5);
                    }
                }
            }
        }

        // Convert map to sorted list (highest score first)
        List<Recommendation> recommendations = new ArrayList<>(recommendationMap.values());
        recommendations.sort((a, b) -> b.score - a.score);

        return recommendations;
    }

    /**
     * Finds an event by its file name (without .txt extension).
     */
    private EventLoader.Event findEventByFileName(String fileName) {
        for (EventLoader.Event event : allEvents) {
            if (event.fileName.equalsIgnoreCase(fileName)) {
                return event;
            }
        }
        return null;
    }

    /**
     * Adds or updates a recommendation in the map, accumulating scores for duplicates.
     */
    private void addOrUpdate(Map<String, Recommendation> map,
                             EventLoader.Event event,
                             String reason,
                             int score) {
        if (map.containsKey(event.fileName)) {
            Recommendation existing = map.get(event.fileName);
            existing.score += score;
            // Append reason if different
            if (!existing.reason.contains(reason)) {
                existing.reason += " | " + reason;
            }
        } else {
            map.put(event.fileName, new Recommendation(event, reason, score));
        }
    }
}
