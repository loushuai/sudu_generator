import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class Sudoku {
    private static final Logger LOG = LoggerFactory.getLogger( Sudoku.class );

    private static List<Integer>[][] grid = new List[9][];
    private static int[][] gridIndex = new int[9][];
    private static HashSet<Integer>[] rowSets = new HashSet[9];
    private static HashSet<Integer>[] colSets = new HashSet[9];;
    private static HashSet<Integer>[] blockSets = new HashSet[9];;

    private static List<Integer> randomValues() {
        List<Integer> result = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Collections.shuffle(result);

        return result;
    }

    private static int blockIndex(int row, int col) {
        return (row / 3) * 3 + (col / 3);
    }

    private static boolean check(int row, int col) {
        if (row == 9 || col == 9) {
            return true;
        }

        while (gridIndex[row][col] < 9) {
            Integer value = grid[row][col].get(gridIndex[row][col]);

            if (rowSets[row].contains(value)) {
                gridIndex[row][col]++;
                continue;
            }

            if (colSets[col].contains(value)) {
                gridIndex[row][col]++;
                continue;
            }

            if (blockSets[blockIndex(row, col)].contains(value)) {
                gridIndex[row][col]++;
                continue;
            }

            // the current value looks good, set maps
            rowSets[row].add(value);
            colSets[col].add(value);
            blockSets[blockIndex(row, col)].add(value);

            // check next point
            int nextCol = (col + 1) % 9;
            int nextRow = row + (col + 1) / 9;
            if (check(nextRow, nextCol) == false) {
                // rollback
                rowSets[row].remove(value);
                colSets[col].remove(value);
                blockSets[blockIndex(row, col)].remove(value);

                gridIndex[row][col]++;
                continue;
            }

            return true;
        }

        gridIndex[row][col] = 0;

        return false;
    }

    private static void printResult() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.printf("%d ", grid[i][j].get(gridIndex[i][j]));
            }
            System.out.println("");
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < grid.length; i++) {
            grid[i] = new List[9];
            gridIndex[i] = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = randomValues();
            }
        }

        for (int i = 0; i < 9; i++) {
            rowSets[i] = new HashSet<>();
            colSets[i] = new HashSet<>();
            blockSets[i] = new HashSet<>();
        }

        check(0, 0);

        printResult();
    }
}
