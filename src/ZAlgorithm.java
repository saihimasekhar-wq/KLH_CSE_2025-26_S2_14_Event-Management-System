public class ZAlgorithm {

    /**
     * Computes the Z-array for the given string.
     *
     * @param s The input string
     * @return  The Z-array
     */
    public static int[] buildZArray(String s) {
        int n = s.length();
        int[] z = new int[n];
        z[0] = n;           // By convention, Z[0] = length of the full string

        int left = 0, right = 0;

        for (int i = 1; i < n; i++) {
            if (i < right) {
                // We are inside the Z-box [left, right]
                // Use already computed value, but cap it at remaining window size
                z[i] = Math.min(right - i, z[i - left]);
            }
            // Try to extend the Z-box from position i
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }
            // Update the Z-box if we extended past the current right boundary
            if (i + z[i] > right) {
                left = i;
                right = i + z[i];
            }
        }
        return z;
    }

    /**
     * Searches for all occurrences of 'pattern' in 'text' using the Z-Algorithm.
     * The search is CASE-INSENSITIVE.
     *
     * @param text    The text to search in
     * @param pattern The pattern to search for
     * @return        Array of starting positions (0-indexed) where pattern is found
     */
    public static int[] search(String text, String pattern) {
        // Convert both to lowercase for case-insensitive matching
        String lowerText    = text.toLowerCase();
        String lowerPattern = pattern.toLowerCase();

        int m = lowerPattern.length();
        int n = lowerText.length();

        // Concatenate: pattern + "$" + text
        // "$" is a separator that won't appear in the Z-box extension
        String combined = lowerPattern + "$" + lowerText;
        int[] z = buildZArray(combined);

        // Count matches first to allocate exact-size array
        int count = 0;
        for (int i = m + 1; i < combined.length(); i++) {
            if (z[i] == m) count++;
        }

        int[] positions = new int[count];
        int idx = 0;
        for (int i = m + 1; i < combined.length(); i++) {
            if (z[i] == m) {
                // Position in original text = i - m - 1
                positions[idx++] = i - m - 1;
            }
        }
        return positions;
    }

    /**
     * Returns true if the pattern is found at least once in the text.
     * Case-insensitive.
     *
     * @param text    The text to search in
     * @param pattern The pattern to search for
     * @return        true if found, false otherwise
     */
    public static boolean contains(String text, String pattern) {
        return search(text, pattern).length > 0;
    }
}
