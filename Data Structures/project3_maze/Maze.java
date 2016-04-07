import java.util.Random;
// No other import statement is allowed

public class Maze
{
	// TO DO: Instance Variables
	private int mazeWidth;
	private int mazeHeight;
	private Cell[][] maze;
	private Random random;
	
	/**
	 * Constructor
	 * @param aWidth the number of chambers in each row
	 * @param aHeight the number of chamber in each column
	 */
	public Maze(int aWidth, int aHeight)
	{
		random = new Random();
		mazeWidth = aWidth;
		mazeHeight = aHeight;
		maze = new Cell[mazeHeight][mazeWidth];
		for (int i = 0; i < mazeHeight; i++)
		{
			for (int j = 0; j < mazeWidth; j++)
			{
				maze[i][j] = new Cell();
			}
		}
		// Create outer walls
		for (int i = 0; i < mazeWidth; i++)
		{
			maze[0][i].hasNorth = true;
			maze[mazeHeight-1][i].hasSouth = true;
		}
		for (int i = 0; i < mazeHeight; i++)
		{
			maze[i][0].hasWest = true;
			maze[i][mazeWidth-1].hasEast = true;
		}
		createMaze(0, mazeWidth-1, 0, mazeHeight-1);
	}

	private void createMaze(int left, int right, int top, int bottom)
	{
		// stop recursing when chamber has width and/or height of 1
		if ((right - left) < 1 || (bottom - top) < 1)
		{
			return;
		}
		
		// choose random point in chamber
		int randomX = left + random.nextInt(right-left);
		int randomY = top + random.nextInt(bottom-top);

		// create walls intersecting at that point
		for (int i = left; i <= right; i++)
		{
			maze[randomY][i].hasSouth = true;
			if (randomY + 1 <= bottom)
			{
				maze[randomY+1][i].hasNorth = true;
			}
		}
		for (int i = top; i <= bottom; i++)
		{
			maze[i][randomX].hasEast = true;
			if (randomX + 1 <= right)
			{
				maze[i][randomX+1].hasWest = true;
			}
		}
		
		// make holes in 3 walls
		
		//FOR TESTING PURPOSES, ALWAYS NORTH NO HOLE & holes 1 off point
		/*maze[randomY][left].hasSouth = false;
		maze[randomY+1][left].hasNorth = false;
		maze[randomY][randomX+1].hasSouth = false;
		maze[randomY+1][randomX+1].hasNorth = false;
		maze[randomY+1][randomX].hasEast = false;
		maze[randomY+1][randomX+1].hasWest = false;*/

		int randomNorth = top;
		int tempValue = randomY-top-1;
		if (tempValue > 0 && randomY > 1)
			randomNorth = top + random.nextInt(tempValue) + 1;
		int randomEast = randomX + 1;
		tempValue = right-randomX-1;
		if (tempValue > 0)
			randomEast = randomX + random.nextInt(tempValue) + 1;
		int randomSouth = randomY + 1;
		tempValue = bottom-randomY-1;
		if (tempValue > 0)
			randomSouth = randomY + random.nextInt(tempValue) + 1;
		int randomWest = left;
		tempValue = randomX-left-1;
		if (tempValue > 0 && (right > 1))
			randomWest = left + random.nextInt(tempValue) + 1;

		int noHoleDirection = 0;//random.nextInt(4);
		if (noHoleDirection == 0)		// North
		{
			maze[randomY][randomEast].hasSouth = false;
			maze[randomY+1][randomEast].hasNorth = false;
			maze[randomSouth][randomX].hasEast = false;
			maze[randomSouth][randomX+1].hasWest = false;
			maze[randomY][randomWest].hasSouth = false;
			maze[randomY+1][randomWest].hasNorth = false;
		}
		else if (noHoleDirection == 1)	// East
		{
			maze[randomNorth][randomX].hasEast = false;
			maze[randomNorth][randomX+1].hasWest = false;
			maze[randomSouth][randomX].hasEast = false;
			maze[randomSouth][randomX+1].hasWest = false;
			maze[randomY][randomWest].hasSouth = false;
			maze[randomY+1][randomWest].hasNorth = false;
		}
		else if (noHoleDirection == 2)	// South
		{
			maze[randomNorth][randomX].hasEast = false;
			maze[randomNorth][randomX+1].hasWest = false;
			maze[randomY][randomEast].hasSouth = false;
			maze[randomY+1][randomEast].hasNorth = false;
			maze[randomY][randomWest].hasSouth = false;
			maze[randomY+1][randomWest].hasNorth = false;
		}
		else							// West
		{
			maze[randomNorth][randomX].hasEast = false;
			maze[randomNorth][randomX+1].hasWest = false;
			maze[randomY][randomEast].hasSouth = false;
			maze[randomY+1][randomEast].hasNorth = false;
			maze[randomSouth][randomX].hasEast = false;
			maze[randomSouth][randomX+1].hasWest = false;
		}
		
		// recursively create maze in each new chamber
		createMaze(left,randomX,top,randomY);
		createMaze(randomX+1,right,top,randomY);
		createMaze(randomX+1,right,randomY+1,bottom);
		createMaze(left,randomX,randomY+1,bottom);
	}

	/**
	 * getWidth
	 * @return the width of this maze
	 */
	public int getWidth()
	{
		return mazeWidth;
	}
	
	/**
	 * getHeight
	 * @return the height of this maze
	 */
	public int getHeight()
	{
		return mazeHeight;
	}
	
	/**
	 * isNorthWall
	 * @param row the row identifier of a chamber
	 * @param column the column identifier of a chamber
	 * @return true if the chamber at row row and column column
	 * contain a north wall. Otherwise, return false
	 */
	public boolean isNorthWall(int row, int column)
	{
		return maze[row][column].hasNorth;
	}
	
	/**
	 * isEastWall
	 * @param row the row identifier of a chamber
	 * @param column the column identifier of a chamber
	 * @return true if the chamber at row row and column column
	 * contain an east wall. Otherwise, return false
	 */
	public boolean isEastWall(int row, int column)
	{
		return maze[row][column].hasEast;
	}
	
	/**
	 * isSouthWall
	 * @param row the row identifier of a chamber
	 * @param column the column identifier of a chamber
	 * @return true if the chamber at row row and column column
	 * contain a south wall. Otherwise, return false
	 */
	public boolean isSouthWall(int row, int column)
	{
		return maze[row][column].hasSouth;
	}
	
	/**
	 * isWestWall
	 * @param row the row identifier of a chamber
	 * @param column the column identifier of a chamber
	 * @return true if the chamber at row row and column column
	 * contain a west wall. Otherwise, return false
	 */
	public boolean isWestWall(int row, int column)
	{
		return maze[row][column].hasWest;
	}

	private class Cell
	{
		private boolean hasNorth;
		private boolean hasEast;
		private boolean hasSouth;
		private boolean hasWest;

		private Cell()
		{
			hasNorth = false;
			hasEast = false;
			hasSouth = false;
			hasWest = false;
		}
	}
}
