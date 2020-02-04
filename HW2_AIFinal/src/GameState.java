import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class GameState {
    private int size;            // The number of stones
    private boolean[] stones;    // Game state: true for available stones, false for taken ones
    private int lastMove;        // The last move

    /**
     * Class constructor specifying the number of stones.
     */
    public GameState(int size) {

        this.size = size;

        //  For convenience, we use 1-based index, and set 0 to be unavailable
        this.stones = new boolean[this.size + 1];
        this.stones[0] = false;

        // Set default state of stones to available
        for (int i = 1; i <= this.size; ++i) {
            this.stones[i] = true;
        }

        // Set the last move be -1
        this.lastMove = -1;
    }

    /**
     * Copy constructor
     */
    public GameState(GameState other) {
        this.size = other.size;
        this.stones = Arrays.copyOf(other.stones, other.stones.length);
        this.lastMove = other.lastMove;
    }


    /**
     * This method is used to compute a list of legal moves
     *
     * @return This is the list of state's moves
     */
    public List<Integer> getMoves() {
      List<Integer> legalMoveSuccessors = new ArrayList<Integer>();
      
      if (lastMove == (-1)) {
        for (int i = 1; i < (double)(this.size/2.0); i++) {
          if (i % 2 != 0) {
            legalMoveSuccessors.add(i); 
          }  
        }
        
      }
      else {
        for (int j = 1; j <= size; j++) {
          if (stones[j] == true && ((j % lastMove) == 0 || (lastMove % j) == 0 )) { // this last parenthesis!!!!!?
          
          legalMoveSuccessors.add(j);
          }
        }
      }
        
        return legalMoveSuccessors;
    }


    /**
     * This method is used to generate a list of successors
     * using the getMoves() method
     *
     * @return This is the list of state's successors
     */
    public List<GameState> getSuccessors() {
        return this.getMoves().stream().map(move -> {
          GameState state = new GameState(this);
            state.removeStone(move);
            return state;
        }).collect(Collectors.toList()); //do i have to import for this????
    }


    /**
     * This method is used to evaluate a game state based on
     * the given heuristic function
     *
     * @return int This is the static score of given state
     */
    public double evaluate() {
        int taken = 0; // number of stones taken
        
        for (int i = 0; i <= size; i++) {
          if (stones[i] == false) {
            taken = taken + 1;
          }
        }
        boolean determineMax = false;
        
        if (taken % 2 == 0) { //proves this is max
          determineMax = true; 
          
        }
        else {
          determineMax = false;
        }
       
        if (this.getMoves().size() == 0 && determineMax == true) { //player 1 wins?
          return -1.0;
        }
        else if (this.getMoves().size() == 0 && determineMax == false ) {  //else if is better //player 2 wins?
          return 1.0;
        }
        else { 
          //continue the game

        double valueReturned = 0.0;
          if (stones[1] == true) {
            valueReturned = 0.0; 
          }
          else if (lastMove == 1) {
            int number = this.getMoves().size(); 
            if (number % 2 != 0) {
              valueReturned = 0.5;
            }
            else {
              valueReturned = -0.5;
            }
            
            }
          
          else if (Helper.isPrime(this.lastMove) == true) { //if prime number
            int increment = 0;
            for (int k = 0; k < this.getMoves().size(); k++) {
              if (this.getMoves().get(k) % this.getLastMove() == 0 || this.lastMove % this.getMoves().get(k) == 0) { // check this over! issue with order and extra or
                increment = increment + 1;
              }
            }
              if (increment % 2 != 0) {
                valueReturned = 0.7;
              }
/*              if (increment % 2 != 0) {
                valueReturned = -0.7
              }*/
              else {
                valueReturned = -0.7;
              }
            }         
           
          else if (Helper.isPrime(this.lastMove) == false) { //if not prime number
            int increment = 0;
            int largestPrimeNumber = Helper.getLargestPrimeFactor(this.lastMove);
            for (int k = 0; k < this.getMoves().size(); k++) {
              if (this.getMoves().get(k) % largestPrimeNumber == 0 || largestPrimeNumber % this.getMoves().get(k) == 0) { // check this over! issue with order and extra or
                increment = increment + 1;
              }
            }
            if (increment % 2 != 0) {
              valueReturned = 0.6;
              
            }
            else {
              valueReturned = -0.6; 
              
            }
          }
            
            if (determineMax) { // might have to set to true
              return valueReturned;
            }
            else {
              return valueReturned * (-1);
            }
             
          }        
    }

    /**
     * This method is used to take a stone out
     *
     * @param idx Index of the taken stone
     */
    public void removeStone(int idx) {
        this.stones[idx] = false;
        this.lastMove = idx;
    }

    /**
     * These are get/set methods for a stone
     *
     * @param idx Index of the taken stone
     */
    public void setStone(int idx) {
        this.stones[idx] = true;
    }

    public boolean getStone(int idx) {
        return this.stones[idx];
    }

    /**
     * These are get/set methods for lastMove variable
     *
     * @param move Index of the taken stone
     */
    public void setLastMove(int move) {
        this.lastMove = move;
    }

    public int getLastMove() {
        return this.lastMove;
    }

    /**
     * This is get method for game size
     *
     * @return int the number of stones
     */
    public int getSize() {
        return this.size;
    }

}   
