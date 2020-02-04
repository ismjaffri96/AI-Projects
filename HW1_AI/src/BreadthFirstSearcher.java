import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {
  /**
   * Calls the parent class constructor.
   * 
   * @see Searcher
   * @param maze initial maze.
   */
  public BreadthFirstSearcher(Maze maze) {
    super(maze);
  }

  /**
   * Main breadth first search algorithm.
   * 
   * @return true if the search finds a solution, false otherwise.
   */
  public boolean search() {
    // Create a new Linked List of queues
    LinkedList<State> queue = new LinkedList<State>();

    // explored list is a 2D Boolean array that indicates if a state associated with a given
    // position in the maze has already been explored.
    boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

    // Create the starting point in your queue for your maze
    State startPos = new State(maze.getPlayerSquare(), null, 0, 0);
    queue.add(startPos);

    // Beginning of BFS algorithm
    while (!queue.isEmpty()) {
      maxSizeOfFrontier = Math.max(queue.size(), maxSizeOfFrontier);
      // Pop the top point of stack, then move to it
      startPos = queue.pop();
      noOfNodesExpanded += 1;
      explored[startPos.getX()][startPos.getY()] = true;
      maxDepthSearched = Math.max(maxDepthSearched, startPos.getDepth());

      // maze = new Maze(maze.getMazeMatrix(), init.getSquare(), maze.getGoalSquare());
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

      // check if this for with parenthesis actually does anything
      for (State successor : startPos.getSuccessors(explored, maze)) {
        boolean flag = true;
        for (State j : queue) {
          if (j.getX() == successor.getX() && j.getY() == successor.getY()) {
            flag = false;
          }
        }
        if (flag) {
          queue.add(successor);
        }
      }


    }

    //return false if no solution
    return false;
  }
}
