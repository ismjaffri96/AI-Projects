import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AlphaBetaPruning {
  int evaluatedNode = 0;
  int visitedNode = 0;
  double val = 0;
  int bestMove = 0;
  int globalDepth = 0;
  int maxDepth = 0;
  List<Double> moveOptions = new ArrayList<Double>();
  int aebf = 0;

    
    public AlphaBetaPruning() {   
    }

    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
     System.out.println("Move: " + bestMove);
     System.out.println("Value: " + (-(val)));     
     System.out.println("Number of Nodes Visited: " + visitedNode);
     System.out.println("Number of Nodes Evaluated: " + evaluatedNode);
     System.out.println("Max Depth Reached: " + maxDepth);
     System.out.println("Avg Effective Branching Factor: " + new DecimalFormat("#.0").format(aebf));
     //System.out.println("Avg Effective Branching Factor: " + ((double)(visitedNode - 1)/ (visitedNode - evaluatedNode)));
    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth) {
      int taken = 0; 
      globalDepth = depth;
      
      for (int i = 0; i <state.getSize(); i++) {
        if (state.getStone(i) == false) {
        taken = taken + 1;
      }
       
    }
      //find max player (player 1)
      boolean player1 = false; //maybe initialize to nothing!
      if (taken % 2.0 == 0) {
        player1 = true;        
      }
      
      //returns int 
      this.val = alphabeta(state, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, player1);
      int testInt = this.moveOptions.indexOf(this.val);
      this.bestMove = state.getMoves().get(testInt);

    }

    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the current game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @param maxPlayer True if player is Max Player; Otherwise, false
     * @return int This is the number indicating score of the best next move
     */
    private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) { //changed beta to double
         maxDepth = Math.max(maxDepth, globalDepth - depth); //check what this means before uncommenting!!!!!
         visitedNode = visitedNode + 1; 
           
        if (state.getSuccessors().size() == 0 || depth == 0) {            //do we need an or here?
          if (depth == globalDepth - 1) {
            moveOptions.add(state.evaluate());
        }
          //moveOptions.add(state.evaluate());
          evaluatedNode = evaluatedNode + 1;
          return state.evaluate(); //test this out          
        }   
    
    
       // double finalVal = 0.0; //initialize the value for max player, which will be either...
        if (maxPlayer == true) {
            double maxVal = Double.NEGATIVE_INFINITY; //alpha
          for (int i = 0; i < state.getSuccessors().size(); i++) {
            double val2 = alphabeta(state.getSuccessors().get(i), depth - 1, alpha, beta, !maxPlayer);
            maxVal = Math.max(maxVal, val2);
            if (beta <= maxVal) {
              break;
            }
            alpha = Math.max(alpha, maxVal);
          }
          if (depth == globalDepth - 1) {
              moveOptions.add(maxVal);

            }
          return maxVal;
        }
        
        else {
            double minVal = Double.POSITIVE_INFINITY; //beginning of search tree?
          for (int i = 0; i < state.getSuccessors().size(); i++) {
            double val2 = alphabeta(state.getSuccessors().get(i), depth - 1, alpha, beta, !maxPlayer);
            minVal = Math.min(minVal, val2);
            if (alpha >= minVal) {
              break;
            }
            beta = Math.min(beta, minVal);
          }
          
          if (depth == globalDepth - 1) {
              moveOptions.add(minVal);

            }
          return minVal;
        }
        
        
        
    
          
  /*      
        if (maximizingPlayer) {
          int best = min //min is negative infinity!
              
              for (each child of the node) {
                
              }
        }
        
    
          
        return best;
    }*/
        
}
}