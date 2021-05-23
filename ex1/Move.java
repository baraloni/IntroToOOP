/**
 The Move class represents a move in the Nim game.
 A move consists of the row on which it is applied and the left and right bounds (inclusive)
 of the sequence of sticks to mark.
 */
public class Move {
    private int row, left, right;

    /**
     * The class constructor, which receives the parameters deﬁning the move.
     * @param inRow The row in which the move will be done
     * @param inLeft The move's left bound
     * @param inRight the Move's right bound
     */
    public Move(int inRow, int inLeft, int inRight){
        this.row = inRow;
        this.left = inLeft;
        this.right = inRight;
    }

    /**
     * Returns a string representation of the move.
     * ex.: returns "2:3-5" for a move taken in the 2 row, sticks 3 ro 5.
     */
    public String toString(){
        return Integer.toString(this.row)+':'+Integer.toString(this.left)+"-"+Integer.toString(this.right);
    }

    /**
     * Returns the row on which the move is performed.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns the left bound of the stick sequence to mark.
     */
    public int getLeftBound() {
        return this.left;
    }

    /**
     * Returns right bound of the stick sequence to mark.
     */
    public int getRightBound() {
        return this.right;
    }
}
