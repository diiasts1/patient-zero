import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */

    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        boolean[] tested = new boolean[n];

        if (n == 0) {
            return tested;
        }

        int[][] dp = new int[n][2];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        ArrayList<Integer> order = new ArrayList<>();
        order.add(0);
        parent[0] = 0;

        for (int i = 0; i < order.size(); i++) {
            int v = order.get(i);

            for (int u : nb[v]) {
                if (u == parent[v]) {
                    continue;
                }

                parent[u] = v;
                order.add(u);
            }
        }

        for (int i = order.size() - 1; i >= 0; i--) {
            int v = order.get(i);

            dp[v][1] = cost[v];
            dp[v][0] = 0;

            for (int u : nb[v]) {
                if (u == parent[v]) {
                    continue;
                }

                dp[v][0] += dp[u][1];
                dp[v][1] += Math.min(dp[u][0], dp[u][1]);
            }
        }

        tested[0] = dp[0][1] <= dp[0][0];

        for (int i = 1; i < order.size(); i++) {
            int v = order.get(i);
            int p = parent[v];

            if (!tested[p]) {
                tested[v] = true;
            } else {
                tested[v] = dp[v][1] <= dp[v][0];
            }
        }

        return tested;
    }


    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */

    public static int longestChain(int n, int[][] nb) {
        if (n == 0) {
            return 0;
        }

        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        ArrayList<Integer> order = new ArrayList<>();
        order.add(0);
        parent[0] = 0;

        for (int i = 0; i < order.size(); i++) {
            int v = order.get(i);

            for (int u : nb[v]) {
                if (u == parent[v]) {
                    continue;
                }

                parent[u] = v;
                order.add(u);
            }
        }

        int[] depth = new int[n];
        int longest = 0;

        for (int i = order.size() - 1; i >= 0; i--) {
            int v = order.get(i);

            int first = 0;
            int second = 0;

            for (int u : nb[v]) {
                if (u == parent[v]) {
                    continue;
                }

                int length = depth[u] + 1;

                if (length > first) {
                    second = first;
                    first = length;
                } else if (length > second) {
                    second = length;
                }
            }

            depth[v] = first;
            longest = Math.max(longest, first + second);
        }

        return longest;
    }

}
