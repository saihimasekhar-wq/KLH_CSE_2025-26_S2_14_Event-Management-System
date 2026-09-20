package Project;

public class ZPatternSearch {

    private static int[] buildZArray(String str) {

        int n = str.length();
        int[] z = new int[n];

        int left = 0;
        int right = 0;

        for (int i = 1; i < n; i++) {

            if (i <= right) {
                z[i] = Math.min(right - i + 1, z[i - left]);
            }

            while (i + z[i] < n &&
                   str.charAt(z[i]) == str.charAt(i + z[i])) {

                z[i]++;
            }

            if (i + z[i] - 1 > right) {
                left = i;
                right = i + z[i] - 1;
            }
        }

        return z;
    }

    public static int countOccurrences(
            String text,
            String pattern) {

        if (text == null || pattern == null) {
            return 0;
        }

        pattern = pattern.trim();

        if (pattern.isEmpty()) {
            return 0;
        }

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        String combined = pattern + "$" + text;

        int[] z = buildZArray(combined);

        int patternLength = pattern.length();

        int count = 0;

        for (int i = patternLength + 1;
             i < combined.length();
             i++) {

            if (z[i] == patternLength) {
                count++;
            }
        }

        return count;
    }
}