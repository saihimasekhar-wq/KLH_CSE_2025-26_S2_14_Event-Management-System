import java.util.*;

/**
 * Main.java — Entry Point
 *
 * Event Management System with Z-Algorithm Search and Recommendation Engine
 *
 * Usage:
 *   Compile:  javac -d out src/*.java
 *   Run:      java -cp out Main
 *
 * The program will prompt you to enter an event name or keyword.
 * It will:
 *   1. Search all event .txt files using the Z-Algorithm
 *   2. Print the full content of each matched event file
 *   3. Show recommendations for related events
 */
public class Main {

    // ─── ANSI Color Codes (for colored terminal output) ───────────────────────
    static final String RESET   = "\u001B[0m";
    static final String BOLD    = "\u001B[1m";
    static final String CYAN    = "\u001B[36m";
    static final String YELLOW  = "\u001B[33m";
    static final String GREEN   = "\u001B[32m";
    static final String MAGENTA = "\u001B[35m";
    static final String RED     = "\u001B[31m";
    static final String BLUE    = "\u001B[34m";

    // ─── Path to the data folder (relative to project root) ──────────────────
    static final String DATA_FOLDER = "data";

    public static void main(String[] args) {
        printBanner();

        // Step 1: Load all event files
        List<EventLoader.Event> events = EventLoader.loadAllEvents(DATA_FOLDER);
        if (events.isEmpty()) {
            System.out.println(RED + "No events loaded. Please check the 'data/' folder." + RESET);
            return;
        }

        // Step 2: Initialize search engine and recommendation engine
        EventSearchEngine    searchEngine = new EventSearchEngine(events);
        RecommendationEngine recommender  = new RecommendationEngine(events);

        // Step 3: Interactive search loop
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println(CYAN + "-".repeat(60) + RESET);
            System.out.print(BOLD + CYAN + "Enter event name or keyword to search (or 'exit' to quit): " + RESET);
            String query = scanner.nextLine().trim();

            if (query.equalsIgnoreCase("exit") || query.equalsIgnoreCase("quit")) {
                System.out.println(YELLOW + "\nGoodbye! Thank you for using the Event Management System." + RESET);
                break;
            }

            if (query.isEmpty()) {
                System.out.println(RED + "Please enter a keyword." + RESET);
                continue;
            }

            // Step 4: Perform Z-Algorithm search
            List<EventSearchEngine.SearchResult> results = searchEngine.search(query);

            if (results.isEmpty()) {
                System.out.println();
                System.out.println(RED + "[X] No events found matching: \"" + query + "\"" + RESET);
                System.out.println(YELLOW + "  Tip: Try event names like pongal, dasara, ramanavami, diwali, holi, ugadi, krishna, sankranti" + RESET);
            } else {
                // Step 5: Print search results
                System.out.println();
                System.out.println(GREEN + "[OK] Found " + results.size() + " event file(s) matching: \"" + query + "\"" + RESET);

                for (EventSearchEngine.SearchResult result : results) {
                    printSearchResult(result, query);
                }

                // Step 6: Generate and print recommendations
                List<RecommendationEngine.Recommendation> recommendations =
                        recommender.recommend(results);

                if (!recommendations.isEmpty()) {
                    printRecommendations(recommendations);
                } else {
                    System.out.println(YELLOW + "\nNo related event recommendations found." + RESET);
                }
            }
        }

        scanner.close();
    }

    // ─── Print Methods ────────────────────────────────────────────────────────

    private static void printBanner() {
        System.out.println();
        System.out.println(BOLD + CYAN + "+----------------------------------------------------------+" + RESET);
        System.out.println(BOLD + CYAN + "|       EVENT MANAGEMENT SYSTEM - Z-ALGORITHM SEARCH       |" + RESET);
        System.out.println(BOLD + CYAN + "|    KLH CSE 2025-26 | S2-14 | String Pattern Matching     |" + RESET);
        System.out.println(BOLD + CYAN + "+----------------------------------------------------------+" + RESET);
        System.out.println(YELLOW + "  Algorithm: Z-Algorithm  |  Time: O(n+m)  |  Case-Insensitive" + RESET);
    }

    private static void printSearchResult(EventSearchEngine.SearchResult result, String query) {
        System.out.println();
        System.out.println(BOLD + MAGENTA + "=== EVENT FOUND: " + result.event.eventName.toUpperCase()
                + " (" + result.event.fileName + ".txt) ===" + RESET);
        System.out.println(MAGENTA + "    Keyword \"" + query + "\" found at "
                + result.matchCount + " position(s): " + formatPositions(result.matchPositions) + RESET);
        System.out.println(MAGENTA + "-".repeat(60) + RESET);
        System.out.println();

        // Print the full event content
        System.out.println(result.event.fullContent);

        System.out.println();
        System.out.println(MAGENTA + "-".repeat(60) + RESET);
    }

    private static void printRecommendations(List<RecommendationEngine.Recommendation> recs) {
        System.out.println();
        System.out.println(BOLD + YELLOW + "*** RECOMMENDED RELATED EVENTS:" + RESET);
        System.out.println(YELLOW + "-".repeat(60) + RESET);

        int rank = 1;
        for (RecommendationEngine.Recommendation rec : recs) {
            System.out.println(BOLD + BLUE + "  " + rank + ". " + rec.event.eventName
                    + " (" + rec.event.fileName + ".txt)" + RESET);
            System.out.println(BLUE + "     Reason : " + rec.reason + RESET);
            System.out.println(BLUE + "     Score  : " + rec.score + RESET);
            System.out.println();
            rank++;
        }

        System.out.println(YELLOW + "-".repeat(60) + RESET);
        System.out.println(YELLOW + "  Type any recommended event name to search for more details!" + RESET);
    }

    /**
     * Formats an array of positions for display. Shows first 5 positions max.
     */
    private static String formatPositions(int[] positions) {
        StringBuilder sb = new StringBuilder("[");
        int limit = Math.min(positions.length, 5);
        for (int i = 0; i < limit; i++) {
            sb.append(positions[i]);
            if (i < limit - 1) sb.append(", ");
        }
        if (positions.length > 5) sb.append(", ... (" + (positions.length - 5) + " more)");
        sb.append("]");
        return sb.toString();
    }
}
