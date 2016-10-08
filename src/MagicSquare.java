import java.util.ArrayList;
import java.util.List;

/**
 * The MagicSquare program generates valid magic square using branch and bound algorithm.
 *
 * @author Quentin de Longraye <https://github.com/quentinus95>
 * @author Victor Le <https://github.com/SuperCoac>
 * @version 1.0
 */
public class MagicSquare {

    private static final int SQUARE_SIZE = 4;
    private static final int EXPECTED_SUM = (SQUARE_SIZE * (SQUARE_SIZE * SQUARE_SIZE + 1)) / 2;
    private final List<Integer> magicSquare;
    private final boolean[] usedValues;
    private List<List<Integer>> solutions = new ArrayList<>();

    public MagicSquare() {
        this.magicSquare = new ArrayList<>();
        for (int i = 0; i < MagicSquare.SQUARE_SIZE * MagicSquare.SQUARE_SIZE; ++i) {
            this.magicSquare.add(0);
        }

        this.usedValues = new boolean[16];
        this.solutions = new ArrayList<>();
    }

    /**
     * Check if the magic square is valid.
     * Each rows, cols, diagonals sums must be equal to {@link #EXPECTED_SUM}.
     * 
     * The structure of each method to check validity is the same :
     * 	1. Iterate through the values
     * 	2. If the expected sum is exceeded, return that the magic square is invalid
     *  3a. If one of the checked values equals 0, return true as the magic square is not finished
     *  3b. If no value equals 0, check that the actual sum is the same that expected
     *
     * @see {@link #areRowsValid} to the how the rows are checked
     * @see {@link #areColsValid} to the how the columns are checked
     * @see {@link #isFirstDiagValid} to the how the first diagonal is checked
     * @see {@link #isSecondDiagValid} to the how the second diagonal is checked
     * 
     * @return true if valid, false otherwise
     */
    private boolean isValid() {
        return this.areRowsValid() && this.areColsValid() && this.isFirstDiagValid() && this.isSecondDiagValid();
    }

    private boolean areRowsValid() {
        int sum;

        for (int i = 0; i < MagicSquare.SQUARE_SIZE; ++i) {
            sum = 0;

            for (int j = 0; j < MagicSquare.SQUARE_SIZE; ++j) {
                sum += this.get(i, j);

                if (sum > MagicSquare.EXPECTED_SUM) {
                    return false;
                }

                if (this.get(i, j) == 0) {
                    return true;
                }
            }

            if (sum != MagicSquare.EXPECTED_SUM) {
                return false;
            }
        }

        return true;
    }

    private boolean areColsValid() {
        int sum;

        for (int i = 0; i < MagicSquare.SQUARE_SIZE; ++i) {
            sum = 0;

            for (int j = 0; j < MagicSquare.SQUARE_SIZE; ++j) {
                sum += this.get(j, i);

                if (sum > MagicSquare.EXPECTED_SUM) {
                    return false;
                }

                if (this.get(j, i) == 0) {
                    return true;
                }
            }

            if (sum != MagicSquare.EXPECTED_SUM) {
                return false;
            }
        }

        return true;
    }

    private boolean isFirstDiagValid() {
        int sum = 0;

        for (int i = 0; i < MagicSquare.SQUARE_SIZE; ++i) {
            sum += this.get(i, i);

            if (sum > MagicSquare.EXPECTED_SUM) {
                return false;
            }

            if (this.get(i, i) == 0) {
                return true;
            }
        }

        return sum == MagicSquare.EXPECTED_SUM;
    }

    private boolean isSecondDiagValid() {
        int sum = 0;

        for (int i = 0; i < MagicSquare.SQUARE_SIZE; ++i) {
            sum += this.get(MagicSquare.SQUARE_SIZE - 1 - i, i);

            if (sum > MagicSquare.EXPECTED_SUM) {
                return false;
            }

            if (this.get(MagicSquare.SQUARE_SIZE - 1 - i, i) == 0) {
                return true;
            }
        }

        return sum == MagicSquare.EXPECTED_SUM;
    }

    /**
     * Check if the magic square is complete; all cells are fill by a number.
     *
     * @return true if complete, false otherwise
     */
    private boolean isComplete() {
        for (Integer aMagicSquare : this.magicSquare) {
            if (aMagicSquare == 0) {
                return false;
            }
        }

        return true;
    }

    private int get(int i, int j) {
        return this.magicSquare.get(i * MagicSquare.SQUARE_SIZE + j);
    }

    /**
     * Get all the correct magic square solutions generated with {@link #generateBranchAndBound}.
     *
     * @return the solutions
     */
    private List<List<Integer>> getSolutions() {
        return this.solutions;
    }

    /**
     * Generate all the correct magic squares through a branch and bound algorithm.
     *
     * @param position position in the square
     */
    private void generateBranchAndBound(int position) {
        for (int i = 0; i < this.usedValues.length; ++i) {

            if (this.usedValues[i]) {
                continue;
            }

            this.magicSquare.set(position, i + 1);
            this.usedValues[i] = true;

            if (this.isValid()) {
                if (this.isComplete()) {
                    //this.printMagicSquare(this.magicSquare);
                    this.solutions.add(new ArrayList<>(this.magicSquare));
                } else {
                    this.generateBranchAndBound(position + 1);
                }
            }

            this.magicSquare.set(position, 0);
            this.usedValues[i] = false;
        }
    }

    /**
     * Print the magic square.
     *  
     * @param magicSquare
     */
    public static void printMagicSquare(List<Integer> magicSquare) {
        String str = "";

        for (int i = 0; i < SQUARE_SIZE; i++) {
            str += "----";
        }

        str += "\n|";
        for (int i = 0; i < magicSquare.size(); i++) {
            if(i%SQUARE_SIZE == 0 && i!=0) {
                str += "\n|";
            }
            str += magicSquare.get(i) + "\t";
        }

        System.out.println(str);
    }

    public static void main(String[] args) {
        MagicSquare magic = new MagicSquare();
        long time = System.nanoTime();
        
        magic.generateBranchAndBound(0);

        System.out.println("Time : " + (System.nanoTime() - time) / 1000000000.0 + " seconds");
        System.out.println("Number of magicSquare : " + magic.getSolutions().size());
        System.out.println("First solution : ");
        
        printMagicSquare(magic.getSolutions().get(0));
    }
}
