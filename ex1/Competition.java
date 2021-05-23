
import java.util.Scanner;

/**
 * The Competition class represents a Nim competition between two players, consisting of a given number of
 * rounds. It also keeps track of the number of victories of each player.
 */
public class Competition {
	//Constants that represent messages in a Nim Game competition
	/**msg declaring a new competition*/
	private static final String[] OPENING_LIST =
			{"Starting a Nim competition of ", " rounds between a ", " player and a "," player."};
	/**msg that presents this competition's score */
	private static final String END_MSG = "The results are ";

	//Constants that represent messages in human player involving competitions
	/**welcoming msg declaring new round */
	private static final String ROUND_DECLARATION_MSG = "Welcome to the sticks game!";
	/**msg declaring which is the player who plays next*/
	private static final String TURN_DECLARATION_MSG = "Player X, it is now your turn!";
	/** msg declaring an un valid move, and asking the player to chose another move*/
	private static final String INVALID_MOVE_MSG = "Invalid move. Enter another:";
	/**msg declaring the last move made, and by which player*/
	private static final String PRODUCED_MOVE_MSG = "Player X made the move: ";
	/**msg declaring the winning of the current round*/
	private static final String WINNING_MSG = "Player X won!";
	/** final constant String which is a temporary replacement to the current player's ID */
	private static final String CHAR_REPLACEMENT  = "X";

	/** Player with ID 1 */
	private Player player1;
	/** player1 scores in this competition*/
	private int player1Score = 0;
	/** Player with ID 2*/
	private Player player2;
	/** player2 scores in this competition*/
	private int player2Score = 0;
	/** boolean "flag" which indicates whether or not to present messages directed to human players
	 * (if player1 or player2 are of HUMAN type) */
	private boolean displayMsg;

	/**
	 * The class constructor, which receives two Player objects representing the two competing
	 * opponents and a ﬂag indicating whether game play messages should be printed to screen.
	 */
	public Competition(Player player1, Player player2, boolean displayMessage) {
		this.player1 = player1;
		this.player2 = player2;
		this.displayMsg = displayMessage;
	}

	/**
	 * @param playerPosition 1 or 2, corresponding to the first or the second player in the competition.
	 * @return  Returns the number of victories of a player.
	 */
	public int getPlayerScore(int playerPosition) {
		int score =	this.player1Score;
		switch (playerPosition){
			case 2:
				score = this.player2Score;
				break;
		}
		return score;
	}

	/**
	 * Runs the competition for ”numRounds” rounds.
	 * @param numRounds number of rounds to run the competition by
	 */
	public void playMultiple(int numRounds){
		String openingMsg = OPENING_LIST[0] + Integer.toString(numRounds) + OPENING_LIST[1] + player1.getTypeName()
				+ OPENING_LIST[2] + player2.getTypeName() + OPENING_LIST[3];
		System.out.println(openingMsg);
		for (int times = 0; times < numRounds; times ++){
			this.playRound();
		}
		System.out.println(END_MSG+Integer.toString(player1Score)+":"+Integer.toString(player2Score));
	}

	/**
	 * conducts a Nim game single round
	 */
	private void playRound(){
		Board board = new Board();
		if (displayMsg) {
			System.out.println(ROUND_DECLARATION_MSG);
		}
		Player currentPlayer= player1;
		while (board.getNumberOfUnmarkedSticks() != 0) {
			this.turn(currentPlayer, board);
			currentPlayer = this.switchTurn(currentPlayer);
		}
		this.updatePlayerScore(currentPlayer);
		if (displayMsg){
			System.out.println(WINNING_MSG.replace
					(CHAR_REPLACEMENT, Integer.toString(currentPlayer.getPlayerId())));
		}
	}

	/**
	 * conducts a Nim game single turn
	 * @param currentPlayer the player which will play now
	 * @param board the board object
	 */
	private void turn(Player currentPlayer , Board board) {
		String playerIdStr = Integer.toString(currentPlayer.getPlayerId());
		if (displayMsg) {
			System.out.println(TURN_DECLARATION_MSG.replace(CHAR_REPLACEMENT, playerIdStr));
		}
		int moveReturnedVal = -1;
		while (moveReturnedVal != 0) {
			Move move = currentPlayer.produceMove(board);
			moveReturnedVal = board.markStickSequence(move);
			if (displayMsg) {
				switch (moveReturnedVal) {
					case (-1):
					case (-2):
						System.out.println(INVALID_MOVE_MSG);
						break;
					case (0):
						System.out.println(PRODUCED_MOVE_MSG.replace
								(CHAR_REPLACEMENT, playerIdStr)+move.toString());
				}
			}
		}
	}

	/**
	 *  returns the player who hadn't make the last move
	 * @param currentPlayer the player who made the latest move
	 * @return the other player
	 */
	private Player switchTurn(Player currentPlayer){
		if (currentPlayer == player1) {
			currentPlayer = player2;
		}else{
			currentPlayer = player1;
		}
	return currentPlayer;
	}

	/**
	 * updates the given player's score, when he wins a round at Nim game
	 * @param player a given nim competition player
	 */
	private void updatePlayerScore(Player player){
		if (player == player1){
			player1Score ++;
		}else{
			player2Score ++;
		}
	}
	/**
	 * The method runs a Nim competition between two players according to the three user-specified arguments.
	 * (1) The type of the first player, which is a positive integer between 1 and 4: 1 for a Random computer
	 *     player, 2 for a Heuristic computer player, 3 for a Smart computer player and 4 for a human player.
	 * (2) The type of the second player, which is a positive integer between 1 and 4.
	 * (3) The number of rounds to be played in the competition.
	 * @param args an array of string representations of the three input arguments, as detailed above.
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int p1Type = Integer.parseInt(args[0]);
		Player player1 = new Player(p1Type,1, scanner);
		int p2Type = Integer.parseInt(args[1]);
		Player player2 = new Player(p2Type,2, scanner);
		int numGames = Integer.parseInt(args[2]);
		Competition competition =
				new Competition(player1, player2, ((player1.getPlayerType() == Player.HUMAN)|
						(player2.getPlayerType() == Player.HUMAN)));
		competition.playMultiple(numGames);
	}
}
