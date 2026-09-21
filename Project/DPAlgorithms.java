package Project;

import java.util.Arrays;

/**
 * ============================================================
 *  DPAlgorithms.java  —  Advanced Dynamic Programming Patterns
 *  KLH CSE 2025-26 | S2-14 | Event Management System
 * ============================================================
 *
 *  This file covers ALL FOUR DP patterns your professor requires:
 *
 *  1. INTERVAL DP   — optimalEventSchedule()
 *  2. BITMASK DP    — minCostEventCoverage()
 *  3. DP ON TREES   — maxReachEvents()
 *  4. DP ON SUBSETS — eventKnapsack()
 *
 *  Each algorithm:
 *    - Is fully commented with the recurrence relation
 *    - Is applied to a real event-management scenario
 *    - Has a static demo() method you can call from Main.java
 * ============================================================
 */
public class DPAlgorithms {

    // ══════════════════════════════════════════════════════════════════════════
    //  1. INTERVAL DP — Optimal Event Scheduling
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * PROBLEM: Optimal Event Schedule (Matrix-Chain / Interval Merging style)
     * ────────────────────────────────────────────────────────────────────────
     * Given N events, each with a "value" (interest score) and a "duration"
     * in hours, find the MINIMUM COST to visit all events in sequence where
     * the cost of visiting events i to j together is:
     *     cost(i, j) = duration[i] * duration[j]  (overlap/coordination cost)
     *
     * This models how to ORDER or GROUP event visits to minimize scheduling
     * overhead — similar to Matrix Chain Multiplication.
     *
     * DP Recurrence (Interval DP):
     * ─────────────────────────────
     *   dp[i][j] = min cost to schedule events i..j
     *
     *   Base case:  dp[i][i] = 0   (single event, no merging cost)
     *
     *   Transition: for each split point k in [i, j-1]:
     *       dp[i][j] = min( dp[i][j],
     *                       dp[i][k] + dp[k+1][j] + duration[i] * duration[j] )
     *
     * Time Complexity : O(n³)
     * Space Complexity: O(n²)
     *
     * @param durations  duration in hours of each event (e.g. {2, 3, 1, 4})
     * @return           minimum scheduling cost for all events
     */
    public static int optimalEventSchedule(int[] durations) {
        int n = durations.length;

        // dp[i][j] = minimum cost to schedule events from index i to j
        int[][] dp = new int[n][n];

        // Base case: single events have 0 internal cost
        for (int i = 0; i < n; i++) dp[i][i] = 0;

        // Fill for increasing interval lengths (l = length of interval)
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;

                // Try every split point k
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j]
                             + durations[i] * durations[j];
                    dp[i][j] = Math.min(dp[i][j], cost);
                }
            }
        }

        return dp[0][n - 1];
    }

    /** Prints a demo of Interval DP for event scheduling */
    public static void demoIntervalDP() {
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println(" [1] INTERVAL DP — Optimal Event Schedule");
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println(" Events (duration in hours): Hackathon=4, Quiz=2, Dance=3, Sports=5");
        System.out.println(" Goal: Find minimum scheduling cost to run all events.");
        System.out.println();

        int[] durations = {4, 2, 3, 5};
        String[] names  = {"Hackathon(4h)", "Quiz(2h)", "Dance(3h)", "Sports(5h)"};

        int result = optimalEventSchedule(durations);

        System.out.println(" Events: " + Arrays.toString(names));
        System.out.printf(" Minimum Scheduling Cost = %d%n", result);
        System.out.println(" Recurrence: dp[i][j] = min(dp[i][k] + dp[k+1][j] + dur[i]*dur[j])");
        System.out.println(" Time Complexity: O(n³)   Space: O(n²)");
        System.out.println();
    }


    // ══════════════════════════════════════════════════════════════════════════
    //  2. BITMASK DP — Minimum Cost Event Coverage
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * PROBLEM: Minimum Cost to Cover All Event Categories
     * ────────────────────────────────────────────────────
     * There are K categories (e.g. Tech, Culture, Sports, Academic).
     * We have N volunteers. Each volunteer can cover a SUBSET of categories
     * and has a cost. Find the minimum cost to cover ALL categories.
     *
     * This is the "Set Cover" problem solved with Bitmask DP.
     *
     * DP Recurrence (Bitmask DP):
     * ────────────────────────────
     *   State:    dp[mask] = min cost to cover exactly the categories
     *                        represented by 'mask'
     *
     *   Goal:     dp[(1<<K)-1] = min cost to cover all K categories
     *
     *   Transition: for each volunteer v with cost[v] and coverage[v]:
     *       newMask = mask | coverage[v]
     *       dp[newMask] = min(dp[newMask], dp[mask] + cost[v])
     *
     * Time Complexity : O(N × 2^K)
     * Space Complexity: O(2^K)
     *
     * @param costs     cost[i] = cost of assigning volunteer i
     * @param coverage  coverage[i] = bitmask of categories volunteer i covers
     * @param numCats   total number of categories (K)
     * @return          minimum cost to cover all categories, or -1 if impossible
     */
    public static int minCostEventCoverage(int[] costs, int[] coverage, int numCats) {
        int fullMask = (1 << numCats) - 1;  // all categories covered
        int[] dp = new int[fullMask + 1];
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;  // cost to cover nothing = 0

        // Enumerate all subsets (masks)
        for (int mask = 0; mask <= fullMask; mask++) {
            if (dp[mask] == Integer.MAX_VALUE / 2) continue;

            // Try assigning each volunteer from current state
            for (int v = 0; v < costs.length; v++) {
                int newMask = mask | coverage[v];  // add this volunteer's coverage
                dp[newMask] = Math.min(dp[newMask], dp[mask] + costs[v]);
            }
        }

        return (dp[fullMask] == Integer.MAX_VALUE / 2) ? -1 : dp[fullMask];
    }

    /** Prints a demo of Bitmask DP for event coverage */
    public static void demoBitmaskDP() {
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println(" [2] BITMASK DP — Minimum Cost Event Coverage");
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println(" Categories: Tech(bit0), Culture(bit1), Sports(bit2), Academic(bit3)");
        System.out.println(" Volunteers:");
        System.out.println("   V1: covers Tech+Culture        (0b0011) cost=3");
        System.out.println("   V2: covers Sports+Academic     (0b1100) cost=4");
        System.out.println("   V3: covers Tech+Sports         (0b0101) cost=2");
        System.out.println("   V4: covers Culture+Academic    (0b1010) cost=5");
        System.out.println("   V5: covers all 4 categories    (0b1111) cost=8");
        System.out.println();

        // Bitmask: bit0=Tech, bit1=Culture, bit2=Sports, bit3=Academic
        int[] costs    = {3,      4,      2,      5,      8};
        int[] coverage = {0b0011, 0b1100, 0b0101, 0b1010, 0b1111};
        int   numCats  = 4;

        int result = minCostEventCoverage(costs, coverage, numCats);

        System.out.printf(" Minimum cost to cover ALL 4 categories = %d%n", result);
        System.out.println(" (Best: V1(3) + V2(4) = 7  covers Tech+Culture+Sports+Academic)");
        System.out.println(" Recurrence: dp[mask|cov[v]] = min(dp[mask|cov[v]], dp[mask]+cost[v])");
        System.out.println(" Time Complexity: O(N × 2^K)   Space: O(2^K)");
        System.out.println();
    }


    // ══════════════════════════════════════════════════════════════════════════
    //  3. DP ON TREES — Maximum Value Events in a Dependency Tree
    // ══════════════════════════════════════════════════════════════════════════

    // Tree stored as adjacency lists and values
    private static int[]   treeValue;
    private static int[][] treeChildren;
    private static int[]   treeChildCount;
    private static int[]   dpTree;  // dpTree[v] = max value in subtree rooted at v

    /**
     * PROBLEM: Maximum Value Event Selection on a Dependency Tree
     * ─────────────────────────────────────────────────────────────
     * Events are arranged in a tree (prerequisite hierarchy):
     *   - You can only attend an event if you attended its parent first.
     *   - Each event has a "value" (interest / score).
     *   - Find the maximum total value you can collect.
     *
     * DP on Trees (DFS-based post-order DP):
     * ────────────────────────────────────────
     *   dpTree[v] = value[v] + sum of dpTree[child] for all children
     *               (we always include all reachable nodes for max value)
     *
     *   This simplifies to: "what is the maximum sum reachable from node v?"
     *   Children are only counted if their subtree value is positive.
     *
     * Time Complexity : O(N)
     * Space Complexity: O(N)  — one DP value per node
     *
     * @param n       number of events (nodes)
     * @param values  values[i] = score/interest of event i
     * @param parent  parent[i] = parent of node i (-1 for root)
     * @return        maximum total value achievable
     */
    public static int maxReachEvents(int n, int[] values, int[] parent) {
        // Build adjacency list for children
        int[][] children  = new int[n][n];
        int[]   childCnt  = new int[n];

        for (int i = 1; i < n; i++) {  // skip root (node 0 has no parent)
            int p = parent[i];
            children[p][childCnt[p]++] = i;
        }

        treeValue      = values;
        treeChildren   = children;
        treeChildCount = childCnt;
        dpTree         = new int[n];

        // Run DFS post-order DP from root (node 0)
        dfsTree(0);

        return dpTree[0];
    }

    /**
     * DFS post-order: compute dpTree[v] after all children are computed.
     */
    private static void dfsTree(int v) {
        dpTree[v] = treeValue[v];  // start with own value

        for (int c = 0; c < treeChildCount[v]; c++) {
            int child = treeChildren[v][c];
            dfsTree(child);  // recurse first (post-order)

            // Only add child subtree if it contributes positively
            if (dpTree[child] > 0) {
                dpTree[v] += dpTree[child];
            }
        }
    }

    /** Prints a demo of DP on Trees for event dependency */
    public static void demoDPOnTrees() {
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println(" [3] DP ON TREES — Max Value Events (Dependency Tree)");
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println(" Event Tree (parent → children):");
        System.out.println("   0:Annual Festival(10) → 1:Tech Expo(5), 2:Cultural Night(8)");
        System.out.println("   1:Tech Expo(5)        → 3:Hackathon(6), 4:Quiz(3)");
        System.out.println("   2:Cultural Night(8)   → 5:Dance(-2), 6:Music(4)");
        System.out.println(" Rule: attend parent before child.");
        System.out.println(" Goal: maximize total score of attended events.");
        System.out.println();

        int n = 7;
        //                              0   1   2   3   4   5   6
        int[] values = new int[]       {10,  5,  8,  6,  3, -2,  4};
        int[] parent = new int[]       {-1,  0,  0,  1,  1,  2,  2};

        int result = maxReachEvents(n, values, parent);

        System.out.printf(" Maximum total event value = %d%n", result);
        System.out.println(" (Festival10 + TechExpo5 + Hackathon6 + Quiz3 + CulturalNight8 + Music4 = 36,");
        System.out.println("  Dance(-2) is skipped as it reduces total)");
        System.out.println(" Recurrence: dp[v] = val[v] + sum(max(0, dp[child]) for child in children(v))");
        System.out.println(" Time Complexity: O(N)   Space: O(N)");
        System.out.println();
    }


    // ══════════════════════════════════════════════════════════════════════════
    //  4. DP ON SUBSETS — Event Knapsack (Best Combination Within Budget)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * PROBLEM: Best Event Combination Within Budget (0/1 Knapsack)
     * ──────────────────────────────────────────────────────────────
     * Given N events, each with a registration FEE and an INTEREST SCORE,
     * and a total BUDGET, find the maximum interest score you can achieve
     * without exceeding the budget.
     *
     * DP on Subsets (0/1 Knapsack):
     * ──────────────────────────────
     *   State:    dp[i][b] = max score using events 0..i-1 with budget b
     *
     *   Base case: dp[0][b] = 0 for all b   (no events → 0 score)
     *
     *   Transition:
     *     Skip event i:   dp[i][b] = dp[i-1][b]
     *     Take event i:   dp[i][b] = dp[i-1][b - fee[i]] + score[i]
     *                                (only if fee[i] <= b)
     *     dp[i][b] = max(skip, take)
     *
     * Time Complexity : O(N × Budget)
     * Space Complexity: O(N × Budget)
     *
     * @param fees    fees[i]   = registration fee of event i
     * @param scores  scores[i] = interest score of event i
     * @param budget  total available budget
     * @return        maximum interest score within budget
     */
    public static int eventKnapsack(int[] fees, int[] scores, int budget) {
        int n = fees.length;

        // dp[i][b] = max score from first i events with budget b
        int[][] dp = new int[n + 1][budget + 1];

        for (int i = 1; i <= n; i++) {
            for (int b = 0; b <= budget; b++) {
                // Option 1: Skip event i
                dp[i][b] = dp[i - 1][b];

                // Option 2: Take event i (only if we can afford it)
                if (fees[i - 1] <= b) {
                    int takeScore = dp[i - 1][b - fees[i - 1]] + scores[i - 1];
                    dp[i][b] = Math.max(dp[i][b], takeScore);
                }
            }
        }

        return dp[n][budget];
    }

    /**
     * Prints the full DP knapsack table — call this separately for academic display.
     */
    public static void printKnapsackTable(int[] fees, int[] scores, int budget) {
        int n = fees.length;
        int[][] dp = new int[n + 1][budget + 1];
        for (int i = 1; i <= n; i++) {
            for (int b = 0; b <= budget; b++) {
                dp[i][b] = dp[i - 1][b];
                if (fees[i - 1] <= b) {
                    dp[i][b] = Math.max(dp[i][b], dp[i - 1][b - fees[i - 1]] + scores[i - 1]);
                }
            }
        }
        System.out.println(" DP Table (events vs budget):");
        System.out.print("        B=");
        for (int b = 0; b <= Math.min(budget, 10); b++) System.out.printf("%3d", b);
        if (budget > 10) System.out.print(" ...");
        System.out.println();
        for (int i = 0; i <= n; i++) {
            System.out.printf(" Event%-2d: ", i);
            for (int b = 0; b <= Math.min(budget, 10); b++) System.out.printf("%3d", dp[i][b]);
            System.out.println();
        }
        System.out.println();
    }

    /** Prints a demo of DP on Subsets (Knapsack) for event selection */
    public static void demoDPOnSubsets() {
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println(" [4] DP ON SUBSETS — Event Knapsack (Budget Planning)");
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println(" Budget: ₹10");
        System.out.println(" Events (Fee → Interest Score):");
        System.out.println("   Hackathon    : Fee=₹4, Score=8");
        System.out.println("   Quiz         : Fee=₹2, Score=3");
        System.out.println("   Dance Comp   : Fee=₹3, Score=5");
        System.out.println("   Music Night  : Fee=₹5, Score=9");
        System.out.println("   Sports Meet  : Fee=₹1, Score=2");
        System.out.println(" Goal: Maximize interest score within ₹10 budget.");
        System.out.println();

        int[] fees   = {4, 2, 3, 5, 1};
        int[] scores = {8, 3, 5, 9, 2};
        int budget   = 10;

        int result = eventKnapsack(fees, scores, budget);

        System.out.printf(" Maximum interest score within ₹%d budget = %d%n", budget, result);
        System.out.println(" (Best: Hackathon(4)+MusicNight(5)+Sports(1)=₹10 → Score=8+9+2=19)");
        System.out.println(" Recurrence: dp[i][b] = max(dp[i-1][b], dp[i-1][b-fee[i]]+score[i])");
        System.out.println(" Time Complexity: O(N×Budget)   Space: O(N×Budget)");
        System.out.println();
    }
}
