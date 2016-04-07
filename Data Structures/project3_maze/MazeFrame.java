import java.util.ArrayList;
import javax.swing.JFrame;

public class MazeFrame
{
	public static void main(String[] args) throws InterruptedException
	{
		int width = 70;
		int height = 70;
		JFrame frame = new JFrame();
		Maze maze = new Maze(width, height);
		ArrayList<Pair<Integer,Integer>> solution = new ArrayList<Pair<Integer,Integer>>();
		MazeComponent mc = new MazeComponent(maze, solution);
		frame.setSize(800,800);
		frame.setTitle("Maze");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(mc);
		frame.setVisible(true);
		
		Pair<Integer,Integer> start = new Pair<Integer,Integer>(0,0);
		solution.add(start);
		Thread.sleep(1000);
		mc.repaint();
		solveMaze(solution, mc, maze, start, new Pair<Integer,Integer>(-1,-1));
	}
	
	/** Solve Maze: recursively solve the maze
	 * 
	 * @param solution   : The array list solution is needed so that every recursive call,
	 *                     a new (or more) next position can be added or removed.
	 * @param mc         : This is the MazeComponent. We need that only for the purpose of
	 *                     animation. We need to call mc.repaint() every time a new position
	 *                     is added or removed. For example,
	 *                       :
	 *                     solution.add(...);
	 *                     mc.repaint();
	 *                     Thread.sleep(sleepTime);
	 *                       :
	 *                     solution.remove(...);
	 *                     mc.repaint();
	 *                     Thread.sleep(sleepTime);
	 *                       :
	 * @param maze       : The maze data structure to be solved. 
	 * @return a boolean value to previous call to tell the previous call whether a solution is
	 *         found.
	 * @throws InterruptedException: We need this because of our Thread.sleep(50);
	 */
	public static boolean solveMaze(ArrayList<Pair<Integer,Integer>> solution, MazeComponent mc, Maze maze, Pair<Integer,Integer> currentPosition, Pair<Integer,Integer> previousPosition) throws InterruptedException
	{
		if ((currentPosition.snd() == maze.getWidth() - 1) && (currentPosition.fst() == maze.getHeight() - 1))
		{
			return true;
		}
		Pair<Integer,Integer> nextPosition = null;
		// solution goes North
		if (!maze.isNorthWall(currentPosition.fst(),currentPosition.snd()) && (currentPosition.fst() - 1 != previousPosition.fst()))
		{
			nextPosition = new Pair<Integer,Integer>(currentPosition.fst()-1,currentPosition.snd());
			solution.add(nextPosition);
			Thread.sleep(5);
			mc.repaint();
			if (solveMaze(solution, mc, maze, nextPosition, currentPosition))
			{
				return true;
			}
			solution.remove(nextPosition);
			Thread.sleep(5);
			mc.repaint();
		}
		// solution goes East
		if (!maze.isEastWall(currentPosition.fst(), currentPosition.snd()) && (currentPosition.snd() + 1 != previousPosition.snd()))
		{
			nextPosition = new Pair<Integer,Integer>(currentPosition.fst(),currentPosition.snd()+1);
			solution.add(nextPosition);
			Thread.sleep(5);
			mc.repaint();
			if (solveMaze(solution, mc, maze, nextPosition, currentPosition))
			{
				return true;
			}
			solution.remove(nextPosition);
			Thread.sleep(5);
			mc.repaint();
		}
		// solution goes South
		if (!maze.isSouthWall(currentPosition.fst(),currentPosition.snd()) && (currentPosition.fst() + 1 != previousPosition.fst()))
		{
			nextPosition = new Pair<Integer,Integer>(currentPosition.fst()+1,currentPosition.snd());
			solution.add(nextPosition);
			Thread.sleep(5);
			mc.repaint();
			if (solveMaze(solution, mc, maze, nextPosition, currentPosition))
			{
				return true;
			}
			solution.remove(nextPosition);
			Thread.sleep(5);
			mc.repaint();
		}
		// solution goes West
		if (!maze.isWestWall(currentPosition.fst(),currentPosition.snd()) && (currentPosition.snd() - 1 != previousPosition.snd()))
		{
			nextPosition = new Pair<Integer,Integer>(currentPosition.fst(),currentPosition.snd()-1);
			solution.add(nextPosition);
			Thread.sleep(5);
			mc.repaint();
			if (solveMaze(solution, mc, maze, nextPosition, currentPosition))
			{
				return true;
			}
			solution.remove(nextPosition);
			Thread.sleep(5);
			mc.repaint();
		}
		
		return false;
	}
}
