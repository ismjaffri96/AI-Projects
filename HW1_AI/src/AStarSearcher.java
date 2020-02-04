import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */

// Helper method for calculating fValue for A-Star
public class AStarSearcher extends Searcher {

  public double fValueCalculator(State state, Square goalSquare) {
    return Math.sqrt(
        Math.pow((state.getX() - goalSquare.X), 2) + Math.pow((state.getY() - goalSquare.Y), 2))
        + state.getGValue();

  }

  /**
   * Calls the parent class constructor.
   * 
   * @see Searcher
   * @param maze initial maze.
   */
  public AStarSearcher(Maze maze) {
    super(maze);
  }

  /**
   * Main a-star search algorithm.
   * 
   * @return true if the search finds a solution, false otherwise.
   */
  public boolean search() {
    // Create a new Linked List of queues
    PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();

    // explored list is a 2D Boolean array that indicates if a state associated with a given
    // position in the maze has already been explored.
    boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

    // Create the starting point in your queue for your maze
    State startPos = new State(maze.getPlayerSquare(), null, 0, 0);
    StateFValuePair startStateFValuePair =
        new StateFValuePair(startPos, fValueCalculator(startPos, maze.getGoalSquare()));
    frontier.add(startStateFValuePair);


    // Beginning of A* algorithm
    while (!frontier.isEmpty()) {
      maxSizeOfFrontier = Math.max(frontier.size(), maxSizeOfFrontier);
      // Pop the top point of priority queue, then move to it
      startPos = frontier.poll().getState();
      noOfNodesExpanded += 1;
      explored[startPos.getX()][startPos.getY()] = true;
      maxDepthSearched = Math.max(maxDepthSearched, startPos.getDepth());

      // Update the player square for generating different successor
      if (startPos.isGoal(maze)) {

        cost = startPos.getDepth();

        maxDepthSearched = Math.max(maxDepthSearched, startPos.getDepth());

        // set your path into the actual trail with a period
        State finalState = startPos;

        while (finalState.getParent() != null) {
          finalState = finalState.getParent();
          maze.setOneSquare(finalState.getSquare(), '.');
        }
        // sets your final state square as S
        maze.setOneSquare(finalState.getSquare(), 'S');
        return true;
      }
      

      for (State successor : startPos.getSuccessors(explored, maze)) {

        boolean flag = true;
        StateFValuePair successorFVal =
            new StateFValuePair(successor, fValueCalculator(successor, maze.getGoalSquare()));

        for (StateFValuePair j : frontier) {

          State temporaryState = j.getState();
          if (temporaryState.getX() == successor.getX()
              && temporaryState.getY() == successor.getY()) {
            flag = false;

            if (temporaryState.getGValue() > successor.getGValue()) {
              frontier.add(successorFVal);
              frontier.remove(j);
              flag = false;
            }
          }

        }
        if (flag) {
          frontier.add(successorFVal);
        }

      }


    }

    // return false if no solution
    return false;
  }

}
