import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrchardSize {

    // checks all trees connected to the current one and counts them
    private static int getConnectedTreesCount(char[][] grid, boolean[][] visited, int row, int col) {
        int rows = grid.length;
        int cols = grid[0].length;

        // boundary checks and if it's already visited or not a tree
        if (row < 0 || row >= rows || col < 0 || col >= cols || visited[row][col] || grid[row][col] == 'O') {
            return 0;
        }

        // mark this tree so we don't visit it twice
        visited[row][col] = true;
        int currentSize = 1;

        // check all 8 directions around the tree
        int[] rOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] cOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            currentSize += getConnectedTreesCount(grid, visited, row + rOffsets[i], col + cOffsets[i]);
        }

        return currentSize;
    }

    public static List<Integer> getOrchardSizes(char[][] grid) {
        List<Integer> sizes = new ArrayList<>();
        
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return sizes;
        }

        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];

        // scan the earth grid
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 'T' && !visited[r][c]) {
                    // found a new orchard, figure out its full size
                    int size = getConnectedTreesCount(grid, visited, r, c);
                    sizes.add(size);
                }
            }
        }
        
        // the example output was sorted descending (5, 3), so we do the same
        Collections.sort(sizes, Collections.reverseOrder());
        return sizes;
    }

    public static void main(String[] args) {
        // the earth sample input matrix
        char[][] earthMap = {
            {'O','T','O','O'},
            {'O','T','O','T'},
            {'T','T','O','T'},
            {'O','T','O','T'}
        };

        System.out.println("Input Map:");
        for (char[] r : earthMap) {
            for (char cell : r) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }

        List<Integer> sizes = getOrchardSizes(earthMap);

        // formatting loop to display output nicely
        System.out.print("\nOutput: ");
        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i < sizes.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }
}
