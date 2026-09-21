package Project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ============================================================
 *  Main.java  —  Event Management System (Project Package)
 *  KLH CSE 2025-26 | S2-14
 * ============================================================
 *
 *  FLOW:
 *    1. User types event name or description
 *    2. System tries EXACT search using Z-Algorithm (O(n+m))
 *    3. If exact match → shows full event description from data/
 *    4. If no exact match → Fuzzy Search using Edit Distance DP
 *       → shows "Did you mean: X?" and auto-displays that event
 *    5. DP Algorithm results shown after every search
 *
 *  Compile: javac -encoding UTF-8 -d out Project/*.java
 *  Run:     java -cp out Project.Main
 * ============================================================
 */
public class Main {

    // ── ANSI Colors ────────────────────────────────────────────────────────
    static final String RESET   = "\u001B[0m";
    static final String BOLD    = "\u001B[1m";
    static final String CYAN    = "\u001B[36m";
    static final String YELLOW  = "\u001B[33m";
    static final String GREEN   = "\u001B[32m";
    static final String MAGENTA = "\u001B[35m";
    static final String RED     = "\u001B[31m";
    static final String BLUE    = "\u001B[34m";

    static final String DATA_FOLDER    = "data";
    static final int    FUZZY_THRESHOLD = 3;   // max typo tolerance

    // ──────────────────────────────────────────────────────────────────────
    public static void main(String[] args) throws IOException {

        printBanner();

        // Load all events from data/ folder
        List<String> eventNames = new ArrayList<>();
        List<File>   eventFiles = new ArrayList<>();
        loadEvents(eventNames, eventFiles);

        if (eventNames.isEmpty()) {
            System.out.println(RED + "No .txt files found in '" + DATA_FOLDER + "/' folder." + RESET);
            return;
        }

        System.out.println(GREEN + "  " + eventFiles.size() + " events loaded from '"
                + DATA_FOLDER + "/'." + RESET);
        System.out.println(CYAN + "  Algorithms ready: Z-Search | Edit-Distance DP | "
                + "Interval DP | Bitmask DP | Tree DP | Subset DP" + RESET);

        // ── Search Loop ────────────────────────────────────────────────────
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println(CYAN + "  " + "─".repeat(58) + RESET);
            System.out.print(BOLD + CYAN
                    + "  Enter event name or describe the event (or 'exit' to quit):\n  > "
                    + RESET);

            String query = scanner.nextLine().trim();

            if (query.equalsIgnoreCase("exit") || query.equalsIgnoreCase("quit")) {
                System.out.println(YELLOW + "\n  Goodbye!\n" + RESET);
                break;
            }
            if (query.isEmpty()) {
                System.out.println(RED + "  Please type something." + RESET);
                continue;
            }

            // ── STEP 1: Exact Z-Algorithm search ───────────────────────────
            File matched = exactSearch(query, eventFiles);

            if (matched != null) {
                // Found exact match → display it
                System.out.println();
                System.out.println(GREEN + BOLD
                        + "  [Z-Algorithm] Exact match found!" + RESET);
                displayEventFile(matched);

            } else {
                // ── STEP 2: No exact match → Fuzzy Search (Edit Distance DP) ──
                System.out.println();
                System.out.println(RED
                        + "  [Z-Algorithm] No exact match for \"" + query + "\"." + RESET);
                System.out.println(YELLOW
                        + "  Trying Fuzzy Search using Edit Distance DP..." + RESET);

                List<FuzzySearch.Suggestion> suggestions =
                        FuzzySearch.getFuzzySuggestions(query, eventNames, FUZZY_THRESHOLD);

                if (suggestions.isEmpty()) {
                    System.out.println(RED
                            + "  No close matches found. Try a different keyword." + RESET);
                } else {
                    // Best fuzzy match → auto-display
                    FuzzySearch.Suggestion best = suggestions.get(0);

                    System.out.println(BOLD + YELLOW
                            + "  [Fuzzy Search] Did you mean: \""
                            + best.eventName + "\"? "
                            + "(edit distance = " + best.editDist + ")" + RESET);

                    // Find the file for this suggestion
                    File fuzzyFile = findFileByName(best.eventName, eventFiles);
                    if (fuzzyFile != null) {
                        System.out.println(YELLOW
                                + "  Auto-loading \"" + best.eventName + "\"..." + RESET);
                        displayEventFile(fuzzyFile);
                    }

                    // Show other close suggestions if any
                    if (suggestions.size() > 1) {
                        System.out.println(YELLOW + BOLD
                                + "  Other close matches:" + RESET);
                        for (int i = 1; i < Math.min(suggestions.size(), 4); i++) {
                            FuzzySearch.Suggestion s = suggestions.get(i);
                            System.out.printf(YELLOW
                                    + "    %d. %-35s (edit distance = %d)%n" + RESET,
                                    i + 1, s.eventName, s.editDist);
                        }
                    }
                }
            }

            // ── STEP 3: DP Algorithm showcase after every search ────────────
            showDPHighlights(query);
        }

        scanner.close();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  EXACT SEARCH — Z-Algorithm
    //  Searches the keyword in every .txt file in data/.
    //  Returns the first file that contains the keyword.
    // ══════════════════════════════════════════════════════════════════════
    private static File exactSearch(String keyword, List<File> files) throws IOException {
        // Split multi-word query into individual words
        // Any file that contains ANY word from the query counts as a match
        String[] words = keyword.toLowerCase().split("\\s+");

        File bestFile  = null;
        int  bestCount = 0;

        for (File file : files) {
            String content = Files.readString(file.toPath()).toLowerCase();
            int total = 0;
            for (String word : words) {
                if (word.length() < 3) continue; // skip very short words like "a", "or"
                total += ZPatternSearch.countOccurrences(content, word);
            }
            if (total > bestCount) {
                bestCount = total;
                bestFile  = file;
            }
        }

        return (bestCount > 0) ? bestFile : null;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  Display full content of a matched event file
    // ══════════════════════════════════════════════════════════════════════
    private static void displayEventFile(File file) throws IOException {
        String content = Files.readString(file.toPath());

        System.out.println();
        System.out.println(BOLD + MAGENTA
                + "  ╔══════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD + MAGENTA
                + "  ║  FILE : " + padRight(file.getName(), 46) + "║" + RESET);
        System.out.println(BOLD + MAGENTA
                + "  ╚══════════════════════════════════════════════════════╝" + RESET);
        System.out.println();

        // Print each line of the event file with indentation
        for (String line : content.split("\\r?\\n")) {
            System.out.println(BLUE + "  " + line + RESET);
        }

        System.out.println();
        System.out.println(MAGENTA + "  " + "─".repeat(58) + RESET);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  DP HIGHLIGHTS — Shown after every search
    //  Demonstrates the 4 DP patterns with compact one-line results
    // ══════════════════════════════════════════════════════════════════════
    private static void showDPHighlights(String query) {
        System.out.println();
        System.out.println(BOLD + MAGENTA + "  ┌─── Advanced DP Algorithm Results ──────────────────┐" + RESET);

        // 1. Interval DP — event scheduling cost
        int[] durations   = {4, 2, 3, 5};
        int   schedCost   = DPAlgorithms.optimalEventSchedule(durations);
        System.out.println(MAGENTA
                + "  │ [Interval DP]  Optimal schedule cost     = " + schedCost + RESET);

        // 2. Bitmask DP — min cost coverage
        int[] costs    = {3, 4, 2, 5, 8};
        int[] coverage = {0b0011, 0b1100, 0b0101, 0b1010, 0b1111};
        int   covCost  = DPAlgorithms.minCostEventCoverage(costs, coverage, 4);
        System.out.println(MAGENTA
                + "  │ [Bitmask DP]   Min cost to cover all categories = " + covCost + RESET);

        // 3. DP on Trees — max reachable event value
        int[] values = {10, 5, 8, 6, 3, -2, 4};
        int[] parent = {-1, 0, 0, 1, 1, 2, 2};
        int   maxVal = DPAlgorithms.maxReachEvents(7, values, parent);
        System.out.println(MAGENTA
                + "  │ [Tree DP]      Max event value in tree  = " + maxVal + RESET);

        // 4. DP on Subsets — best events within budget
        int[] fees   = {4, 2, 3, 5, 1};
        int[] scores = {8, 3, 5, 9, 2};
        int   maxScore = DPAlgorithms.eventKnapsack(fees, scores, 10);
        System.out.println(MAGENTA
                + "  │ [Subset DP]    Best score within budget Rs.10 = " + maxScore + RESET);

        System.out.println(BOLD + MAGENTA
                + "  └─────────────────────────────────────────────────────┘" + RESET);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  Load all .txt files from data/ and extract clean event names
    // ══════════════════════════════════════════════════════════════════════
    private static void loadEvents(List<String> names, List<File> files) {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists() || !folder.isDirectory()) return;

        File[] all = folder.listFiles();
        if (all == null) return;

        for (File f : all) {
            if (f.isFile() && f.getName().toLowerCase().endsWith(".txt")) {
                files.add(f);
                // Clean name: "001_annual_cultural_festival_2026.txt" → "annual cultural festival"
                String name = f.getName()
                        .replaceFirst("^\\d+_", "")
                        .replaceAll("_\\d{4}\\.txt$", "")
                        .replaceAll("\\.txt$", "")
                        .replace("_", " ")
                        .trim();
                names.add(name);
            }
        }
    }

    // ── Find event file whose clean name matches a suggestion ─────────────
    private static File findFileByName(String cleanName, List<File> files) {
        for (File f : files) {
            String fn = f.getName()
                    .replaceFirst("^\\d+_", "")
                    .replaceAll("_\\d{4}\\.txt$", "")
                    .replaceAll("\\.txt$", "")
                    .replace("_", " ")
                    .trim();
            if (fn.equalsIgnoreCase(cleanName)) return f;
        }
        return null;
    }

    // ── Pad a string to fixed width ───────────────────────────────────────
    private static String padRight(String s, int width) {
        if (s.length() >= width) return s.substring(0, width);
        return s + " ".repeat(width - s.length());
    }

    // ── Banner ─────────────────────────────────────────────────────────────
    private static void printBanner() {
        System.out.println();
        System.out.println(BOLD + CYAN
                + "  ╔══════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD + CYAN
                + "  ║        EVENT MANAGEMENT SYSTEM  --  KLH CSE S2-14       ║" + RESET);
        System.out.println(BOLD + CYAN
                + "  ╠══════════════════════════════════════════════════════════╣" + RESET);
        System.out.println(BOLD + CYAN
                + "  ║  Exact Search  : Z-Algorithm              O(n + m)       ║" + RESET);
        System.out.println(BOLD + CYAN
                + "  ║  Fuzzy Search  : Edit Distance DP         O(m x n)       ║" + RESET);
        System.out.println(BOLD + CYAN
                + "  ║  Interval DP   : Event Scheduling         O(n^3)         ║" + RESET);
        System.out.println(BOLD + CYAN
                + "  ║  Bitmask DP    : Category Coverage        O(N x 2^K)     ║" + RESET);
        System.out.println(BOLD + CYAN
                + "  ║  DP on Trees   : Dependency Traversal     O(N)           ║" + RESET);
        System.out.println(BOLD + CYAN
                + "  ║  DP on Subsets : Budget Knapsack          O(N x W)       ║" + RESET);
        System.out.println(BOLD + CYAN
                + "  ╚══════════════════════════════════════════════════════════╝" + RESET);
    }
}
