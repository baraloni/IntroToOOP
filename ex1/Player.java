import java.util.Random;
import java.util.Scanner;

/**
 * The Player class represents a player in the Nim game, producing Moves as a response to a Board state.
 * Each player is initialized with a type, either human or one of several computer strategies, which
 * defines the move he produces when given a board in some state. The heuristic strategy of the player is
 * already implemented.  You are required to implement the rest of the player types according to the exercise
 * description.
 * @author OOP course staff
 */
public class Player {

	//Constants that represent the different players.
	/** The constant integer representing the Random player type. */
	public static final int RANDOM = 1;
	/** The constant integer representing the Heuristic player type. */
	public static final int HEURISTIC = 2;
	/** The constant integer representing the Smart player type. */
	public static final int SMART = 3;
	/** The constant integer representing the Human player type. */
	public static final int HUMAN = 4;
	/**Used by produceHeuristicMove() for binary representation of board rows. */
	private static final int BINARY_LENGTH = 4;

	//Constants that represent messages in human player involving competitions
	/**msg asking the human player to choose between seeing the current status of the board or making a move*/
	private static final String BOARD_MOVE_MSG = "Press 1 to display the board. Press 2 to make a move:";
	/** msg requesting the human player to enter a row number for a Move */
	private static final String ROW_MSG = "Enter the row number:";
	/** msg requesting the human player to enter a stick number for a Move's left bound */
	private static final String LEFT_STICK_MSG = "Enter the index of the leftmost stick:";
	/** msg requesting the human player to enter a stick number for a Move's right bound */
	private static final String RIGHT_STICK_MSG = "Enter the index of the rightmost stick:";
	/** msg informing the human player that the choice it made is un valid */
	private static final String UNSUPPORTED_MSG = "Unsupported command";

	/** int representing the player's type */
	private final int playerType;
	/** int representing the player's ID */
	private final int playerId;
	/** Scanner object to support Human Player communication */
	private Scanner scanner;
	/** Random object to support the Random player's strategy */
	private Random random = new Random();

	/**
	 * Initializes a new player of the given type and the given id, and an initialized scanner.
	 * @param type The type of the player to create.
	 * @param id The id of the player (either 1 or 2).
	 * @param inputScanner The Scanner object through which to get user input
	 * for the Human player type. 
	 */
	public Player(int type, int id, Scanner inputScanner){		
		// Check for legal player type
		if (type != RANDOM && type != HEURISTIC 
				&& type != SMART && type != HUMAN){
			System.out.println("Received an unknown player type as a parameter"
					+ " in Player constructor. Terminating.");
			System.exit(-1);
		}		
		playerType = type;	
		playerId = id;
		scanner = inputScanner;
	}
	
	/**
	 * @return an integer matching the player type.
	 */	
	public int getPlayerType(){
		return playerType;
	}
	
	/**
	 * @return the players id number.
	 */	
	public int getPlayerId(){
		return playerId;
	}
	
	
	/**
	 * @return a String matching the player type.
	 */
	public String getTypeName(){
		switch(playerType){
			
			case RANDOM:
				return "Random";			    
	
			case SMART: 
				return "Smart";	
				
			case HEURISTIC:
				return "Heuristic";
				
			case HUMAN:			
				return "Human";
		}
		//Because we checked for legal player types in the
		//constructor, this line shouldn't be reachable.
		return "UnknownPlayerType";
	}
	
	/**
	 * This method encapsulates all the reasoning of the player about the game. The player is given the 
	 * board object, and is required to return his next move on the board. The choice of the move depends
	 * on the type of the player: a human player chooses his move manually; the random player should 
	 * return some random move; the Smart player can represent any reasonable strategy; the Heuristic 
	 * player uses a strong heuristic to choose a move. 
	 * @param board - a Board object representing the current state of the game.
	 * @return a Move object representing the move that the current player will play according to
	 * his strategy
	 */
	public Move produceMove(Board board){
		
		switch(playerType){
		
			case RANDOM:
				return produceRandomMove(board);				
				    
			case SMART: 
				return produceSmartMove(board);
				
			case HEURISTIC:
				return produceHeuristicMove(board);
				
			case HUMAN:
				return produceHumanMove(board);

			//Because we checked for legal player types in the
			//constructor, this line shouldn't be reachable.
			default: 
				return null;			
		}
	}
	
	/*
	 * Produces a random move.
	 */
	private Move produceRandomMove(Board board){
		Move move = null;
		while (move == null) {
			int row = random.nextInt(board.getNumberOfRows()) + 1;
			int stickNum = random.nextInt(board.getRowLength(row)) + 1;
			for (int leftStick = 0; leftStick <= board.getRowLength(row) - stickNum; leftStick++) {
				int leftBound = stickNum + leftStick;
				if (board.isStickUnmarked(row, leftBound)) {
					int rightBound = leftBound;
					int seqSize = random.nextInt(board.getRowLength(row) - leftBound + 1) + 1;
					for (int rightStick = 1; rightStick < seqSize; rightStick++){
						rightBound = leftBound + rightStick;
						if (!board.isStickUnmarked(row, rightBound)) {
							rightBound = rightBound - 1;
							break;
						}
					}
					move = new Move(row, leftBound, rightBound);
					break;
				}
			}
		}
		return move;
	}
	
	/*
	 * Produce some intelligent strategy to produce a move
	 */
	private Move produceSmartMove(Board board){
		int unmarkedNum = board.getNumberOfUnmarkedSticks();
		int rowsNum = board.getNumberOfRows();
		//* if a valid and optimal move is not found: saves the last valid move to return
		Move secondaryMove = null;
		int leftBound, rightBound;
		for(int row = rowsNum; row >= 1; row --) {
			int sticksNum = board.getRowLength(row);
			for (int leftStick = 1; leftStick <= sticksNum; leftStick++) {
				if (board.isStickUnmarked(row, leftStick)) {
					leftBound = rightBound = leftStick;
					while ((rightBound < sticksNum)&&(board.isStickUnmarked(row, rightBound+1))){
						rightBound ++;
					}
					int newRightBound = boundAssessment(leftBound, rightBound, unmarkedNum);
					if (newRightBound != -1) {
						return new Move(row, leftBound, newRightBound);
					}else{
						secondaryMove = new Move(row, leftBound, leftBound);
					}
				}
			}
		}//* reached only if an optimal move is not found due to "return" line 175
		return secondaryMove;
	}

	/**
	 * re-assess the rightBound according to the equality of unmarkedNum.
	 * returns the number the right stick that will create the maximal sequence which is equal or un-equal
	 * (opposite to the equality of unmarkedNum) if can't- returns -1;
	 * @param leftBound the move's left stick
	 * @param rightBound the move's right stick
	 * @param unmarkedNum the number of unmarked stick in the game
	 * @return the right bound
	 */
	private int boundAssessment(int leftBound, int rightBound, int unmarkedNum){
		int alternatedRight = rightBound;
		int seqSize = rightBound-leftBound + 1;
		//*sequence is equal, so seq>=2 and we can subtract -1 to make seq un-equal while unmarked is equal
		if ((unmarkedNum%2 == 0) && (seqSize%2 == 0)){
			alternatedRight--;
		}//* sequence is un-equal, so seq>=1 and we can subtract -1 iff seq>1 to make seq equal while
		// unmarked is un-equal (if seq=1: the algorithm will return -1 : meaning- failing)
		if((unmarkedNum%2 != 0)&&(seqSize%2 != 0)){
			if (seqSize != 1) {
				alternatedRight--;
			}else{
				alternatedRight = -1;
			}
		}return alternatedRight;
	}
	
	/*
	 * Interact with the user to produce his move.
	 */
	private Move produceHumanMove(Board board){
		Move move = null;
		while (move == null){
			System.out.println(BOARD_MOVE_MSG);
			int userInput = scanner.nextInt();
			switch (userInput) {
				case 1:
					System.out.println(board.toString());
					break;
				case 2:
					System.out.println(ROW_MSG);
					int rowInput = scanner.nextInt();
					System.out.println(LEFT_STICK_MSG);
					int leftStickInput = scanner.nextInt();
					System.out.println(RIGHT_STICK_MSG);
					int rightStickInput = scanner.nextInt();
					move = new Move(rowInput, leftStickInput, rightStickInput);
					break;
				default:
					System.out.println(UNSUPPORTED_MSG);
					break;
			}
		}
		return move;
	}

	/*
	 * Uses a winning heuristic for the Nim game to produce a move.
	 */
	private Move produceHeuristicMove(Board board){

		int numRows = board.getNumberOfRows();
		int[][] bins = new int[numRows][BINARY_LENGTH];
		int[] binarySum = new int[BINARY_LENGTH];
		int bitIndex,higherThenOne=0,totalOnes=0,lastRow=0,lastLeft=0,lastSize=0,lastOneRow=0,lastOneLeft=0;
		
		for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
			binarySum[bitIndex] = 0;
		}
		
		for(int k=0;k<numRows;k++){
			
			int curRowLength = board.getRowLength(k+1);
			int i = 0;
			int numOnes = 0;
			
			for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
				bins[k][bitIndex] = 0;
			}
			
			do {
				if(i<curRowLength && board.isStickUnmarked(k+1,i+1) ){
					numOnes++;
				} else {
					
					if(numOnes>0){
						
						String curNum = Integer.toBinaryString(numOnes);
						while(curNum.length()<BINARY_LENGTH){
							curNum = "0" + curNum;
						}
						for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
							bins[k][bitIndex] += curNum.charAt(bitIndex)-'0'; //Convert from char to int
						}
						
						if(numOnes>1){
							higherThenOne++;
							lastRow = k +1;
							lastLeft = i - numOnes + 1;
							lastSize = numOnes;
						} else {
							totalOnes++;
						}
						lastOneRow = k+1;
						lastOneLeft = i;
						
						numOnes = 0;
					}
				}
				i++;
			}while(i<=curRowLength);
			
			for(bitIndex = 0;bitIndex<BINARY_LENGTH;bitIndex++){
				binarySum[bitIndex] = (binarySum[bitIndex]+bins[k][bitIndex])%2;
			}
		}
		
		
		//We only have single sticks
		if(higherThenOne==0){
			return new Move(lastOneRow,lastOneLeft,lastOneLeft);
		}
		
		//We are at a finishing state				
		if(higherThenOne<=1){
			
			if(totalOnes == 0){
				return new Move(lastRow,lastLeft,lastLeft+(lastSize-1) - 1);
			} else {
				return new Move(lastRow,lastLeft,lastLeft+(lastSize-1)-(1-totalOnes%2));
			}
			
		}
		
		for(bitIndex = 0;bitIndex<BINARY_LENGTH-1;bitIndex++){
			
			if(binarySum[bitIndex]>0){
				
				int finalSum = 0,eraseRow = 0,eraseSize = 0,numRemove = 0;
				for(int k=0;k<numRows;k++){
					
					if(bins[k][bitIndex]>0){
						eraseRow = k+1;
						eraseSize = (int)Math.pow(2,BINARY_LENGTH-bitIndex-1);
						
						for(int b2 = bitIndex+1;b2<BINARY_LENGTH;b2++){
							
							if(binarySum[b2]>0){
								
								if(bins[k][b2]==0){
									finalSum = finalSum + (int)Math.pow(2,BINARY_LENGTH-b2-1);
								} else {
									finalSum = finalSum - (int)Math.pow(2,BINARY_LENGTH-b2-1);
								}
								
							}
							
						}
						break;
					}
				}
				
				numRemove = eraseSize - finalSum;
				
				//Now we find that part and remove from it the required piece
				int numOnes=0,i=0;
				while(numOnes<eraseSize){

					if(board.isStickUnmarked(eraseRow,i+1)){
						numOnes++;
					} else {
						numOnes=0;
					}
					i++;
					
				}
				return new Move(eraseRow,i-numOnes+1,i-numOnes+numRemove);
			}
		}
		
		//If we reached here, and the board is not symmetric, then we only need to erase a single stick
		if(binarySum[BINARY_LENGTH-1]>0){
			return new Move(lastOneRow,lastOneLeft,lastOneLeft);
		}
		
		//If we reached here, it means that the board is already symmetric, and then we simply mark one stick
		// from the last sequence we saw:
		return new Move(lastRow,lastLeft,lastLeft);		
	}


}
