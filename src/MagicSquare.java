import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * The MagicSquare program generates valid magic square using branch and bound algorithm.
 *
 * @author Quentin de Longraye <https://github.com/quentinus95>
 * @author Victor Le <https://github.com/Coac>
 * @version 1.0
 */
public class MagicSquare {

    private static final int SQUARE_SIZE = 4;
    private static final int EXPECTED_SUM = (SQUARE_SIZE * (SQUARE_SIZE * SQUARE_SIZE + 1)) / 2;
    //private final List<Integer> magicSquare;
    //private final boolean[] usedValues;
    private List<List<Integer>> solutions = new ArrayList<>();

    public MagicSquare() {

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
    private boolean isValid(List<Integer> magicSquare) {
        return this.areRowsValid(magicSquare) && this.areColsValid(magicSquare) && this.isFirstDiagValid(magicSquare) && this.isSecondDiagValid(magicSquare);
    }

    private boolean areRowsValid(List<Integer> magicSquare) {
        int sum;

        for (int i = 0; i < MagicSquare.SQUARE_SIZE; ++i) {
            sum = 0;

            for (int j = 0; j < MagicSquare.SQUARE_SIZE; ++j) {
                sum += this.get(i, j, magicSquare);

                if (sum > MagicSquare.EXPECTED_SUM) {
                    return false;
                }

                if (this.get(i, j, magicSquare) == 0) {
                    return true;
                }
            }

            if (sum != MagicSquare.EXPECTED_SUM) {
                return false;
            }
        }

        return true;
    }

    private boolean areColsValid(List<Integer> magicSquare) {
        int sum;

        for (int i = 0; i < MagicSquare.SQUARE_SIZE; ++i) {
            sum = 0;

            for (int j = 0; j < MagicSquare.SQUARE_SIZE; ++j) {
                sum += this.get(j, i, magicSquare);

                if (sum > MagicSquare.EXPECTED_SUM) {
                    return false;
                }

                if (this.get(j, i, magicSquare) == 0) {
                    return true;
                }
            }

            if (sum != MagicSquare.EXPECTED_SUM) {
                return false;
            }
        }

        return true;
    }

    private boolean isFirstDiagValid(List<Integer> magicSquare) {
        int sum = 0;

        for (int i = 0; i < MagicSquare.SQUARE_SIZE; ++i) {
            sum += this.get(i, i, magicSquare);

            if (sum > MagicSquare.EXPECTED_SUM) {
                return false;
            }

            if (this.get(i, i, magicSquare) == 0) {
                return true;
            }
        }

        return sum == MagicSquare.EXPECTED_SUM;
    }

    private boolean isSecondDiagValid(List<Integer> magicSquare) {
        int sum = 0;

        for (int i = 0; i < MagicSquare.SQUARE_SIZE; ++i) {
            sum += this.get(MagicSquare.SQUARE_SIZE - 1 - i, i, magicSquare);

            if (sum > MagicSquare.EXPECTED_SUM) {
                return false;
            }

            if (this.get(MagicSquare.SQUARE_SIZE - 1 - i, i, magicSquare) == 0) {
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
    private boolean isComplete(List<Integer> magicSquare) {
        for (Integer aMagicSquare : magicSquare) {
            if (aMagicSquare == 0) {
                return false;
            }
        }

        return true;
    }

    private int get(int i, int j, List<Integer> magicSquare) {
        return magicSquare.get(i * MagicSquare.SQUARE_SIZE + j);
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
    private void generateBranchAndBound(int position, List<Integer> magicSquare,  boolean[] usedValues) {
        for (int i = 0; i < usedValues.length; ++i) {

            if (usedValues[i]) {
                continue;
            }

            magicSquare.set(position, i + 1);
            usedValues[i] = true;

            if (this.isValid(magicSquare)) {
                if (this.isComplete(magicSquare)) {
                    //this.printMagicSquare(this.magicSquare);
                    synchronized(this.solutions) {
                        this.solutions.add(new ArrayList<>(magicSquare));
                    }
                } else {
                    this.generateBranchAndBound(position + 1, magicSquare, usedValues);
                }
            }

            magicSquare.set(position, 0);
            usedValues[i] = false;
        }
    }

    private void generateBranchAndBoundParallel() {
        IntStream.range(0, SQUARE_SIZE*SQUARE_SIZE).parallel().forEach(i -> {

            List<Integer> magicSquare = new ArrayList<>();
            for (int j = 0; j < MagicSquare.SQUARE_SIZE * MagicSquare.SQUARE_SIZE; ++j) {
                magicSquare.add(0);
            }

            boolean[] usedValues = new boolean[16];
            usedValues[i] = true;

            magicSquare.set(0, i + 1);

            generateBranchAndBound(1, magicSquare, usedValues);
        });
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
        
        magic.generateBranchAndBoundParallel();

        System.out.println("Time : " + (System.nanoTime() - time) / 1000000000.0 + " seconds");
        System.out.println("Number of magicSquare : " + magic.getSolutions().size());
        System.out.println("First solution : ");
        
        printMagicSquare(magic.getSolutions().get(0));
    }
}
