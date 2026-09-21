package Project;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 *  FuzzySearch.java  —  Fuzzy / Typo-Tolerant Search
 *  KLH CSE 2025-26 | S2-14 | Event Management System
 * ============================================================
 *
 *  ALGORITHM: Edit Distance (Levenshtein Distance) using 2-D DP
 *
 *  What is Edit Distance?
 *  ─────────────────────
 *  The minimum number of single-character operations needed
 *  to transform string A into string B.
 *
 *  Allowed operations:
 *    1. Insert  a character
 *    2. Delete  a character
 *    3. Replace a character
 *
 *  Example  →  "holo"  to  "holi"
 *  ────────────────────────────────
 *       ""  h  o  l  i
 *  ""  [ 0  1  2  3  4 ]
 *  h   [ 1  0  1  2  3 ]
 *  o   [ 2  1  0  1  2 ]
 *  l   [ 3  2  1  0  1 ]
 *  o   [ 4  3  2  1  1 ]  ← answer = 1 (replace last 'o' with 'i')
 *
 *  DP Recurrence:
 *  ─────────────
 *  if s1[i-1] == s2[j-1]:
 *      dp[i][j] = dp[i-1][j-1]          (no cost — chars match)
 *  else:
 *      dp[i][j] = 1 + min(
 *          dp[i-1][j],    // delete from s1
 *          dp[i][j-1],    // insert into s1
 *          dp[i-1][j-1]   // replace in s1
 *      )
 *
 *  Time Complexity : O(m × n)
 *  Space Complexity: O(m × n)  where m, n = lengths of the two strings
 * ============================================================
 */
public class FuzzySearch {

    // ── Inner class to hold one fuzzy suggestion ──────────────────────────────
    public static class Suggestion {
        public String  eventName;   // the matched event name
        public int     editDist;    // how many edits away from the query

        public Suggestion(String eventName, int editDist) {
            this.eventName = eventName;
            this.editDist  = editDist;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Core DP: Compute Edit Distance between two strings (case-insensitive)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Computes the Edit Distance (Levenshtein Distance) between s1 and s2
     * using classic 2-D Dynamic Programming.
     *
     * @param s1 First string
     * @param s2 Second string
     * @return   Minimum number of insert/delete/replace operations
     */
    public static int editDistance(String s1, String s2) {
        // Case-insensitive comparison
        s1 = s1.toLowerCase().trim();
        s2 = s2.toLowerCase().trim();

        int m = s1.length();
        int n = s2.length();

        // dp[i][j] = edit distance between s1[0..i-1] and s2[0..j-1]
        int[][] dp = new int[m + 1][n + 1];

        // Base cases:
        // Converting s1[0..i-1] to empty string requires i deletions
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        // Converting empty string to s2[0..j-1] requires j insertions
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        // Fill the DP table bottom-up
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {

                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    // Characters match — no extra cost
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Take the cheapest of the three operations + 1
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j - 1],   // replace
                            Math.min(
                                    dp[i - 1][j],   // delete from s1
                                    dp[i][j - 1]    // insert into s1
                            )
                    );
                }
            }
        }

        return dp[m][n];
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Fuzzy Suggestion Engine
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Given a possibly-misspelled query, compares it against a list of
     * candidate event names and returns those within the edit-distance threshold.
     *
     * Suggestions are sorted by edit distance (closest first).
     *
     * @param query       The user's search term (possibly with typos)
     * @param eventNames  All event names/keywords to compare against
     * @param threshold   Maximum allowed edit distance (e.g. 2 means within 2 edits)
     * @return            List of Suggestion objects sorted by closeness
     */
    public static List<Suggestion> getFuzzySuggestions(
            String       query,
            List<String> eventNames,
            int          threshold) {

        List<Suggestion> suggestions = new ArrayList<>();

        for (String name : eventNames) {
            int dist = editDistance(query, name);
            if (dist <= threshold && dist > 0) {  // dist > 0 → not an exact match
                suggestions.add(new Suggestion(name, dist));
            }
        }

        // Sort by edit distance ascending (closest match first)
        suggestions.sort((a, b) -> a.editDist - b.editDist);

        return suggestions;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Pretty-Print the DP table (for academic report / explanation)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Prints the full DP table for editDistance(s1, s2).
     * Useful for understanding how the algorithm works step-by-step.
     *
     * Example output for ("holo", "holi"):
     *
     *      ""  h   o   l   i
     * ""  [ 0   1   2   3   4 ]
     * h   [ 1   0   1   2   3 ]
     * o   [ 2   1   0   1   2 ]
     * l   [ 3   2   1   0   1 ]
     * o   [ 4   3   2   1   1 ]
     */
    public static void printDPTable(String s1, String s2) {
        s1 = s1.toLowerCase().trim();
        s2 = s2.toLowerCase().trim();

        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                                   Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }

        // Header row
        System.out.print("       \"\"");
        for (char c : s2.toCharArray()) System.out.printf("   %c", c);
        System.out.println();

        // Data rows
        for (int i = 0; i <= m; i++) {
            if (i == 0) System.out.printf("\"\"  ");
            else        System.out.printf("%-3c ", s1.charAt(i - 1));

            System.out.print("[");
            for (int j = 0; j <= n; j++) {
                System.out.printf(" %-3d", dp[i][j]);
            }
            System.out.println("]");
        }

        System.out.printf("%nEdit Distance(\"%s\", \"%s\") = %d%n", s1, s2, dp[m][n]);
    }
}
