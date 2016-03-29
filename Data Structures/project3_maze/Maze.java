import java.util.Random;
// No other import statement is allowed

public class Maze
{
	// TO DO: Instance Variables
	private int mazeWidth;
	private int mazeHeight;
	private Chamber[][] maze;
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
		maze = new Chamber[mazeHeight][mazeWidth];
		for (int i = 0; i < mazeHeight; i++)
		{
			for (int j = 0; j < mazeWidth; j++)
			{
				maze[i][j] = new Chamber();
			}
		}
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
		if ((right - left) <= 1 || (bottom - top) <= 1)
		{
			return;
		}
		
		int randomX = left + random.nextInt(right-left);
		int randomY = top + random.nextInt(bottom-top);
		
		for (int i = left; i < right; i++)
		{
			maze[randomY][i].hasSouth = true;
			if (randomY + 1 <= right)
			{
				maze[randomY+1][i].hasNorth = true;
			}
		}
		for (int i = top; i < bottom; i++)
		{
			maze[i][randomX].hasEast = true;
			if (randomX + 1 <= right)
			{
				maze[i][randomX+1].hasWest = true;
			}
		}

		int tempValue = randomY-top;
		if (tempValue <= 0) tempValue = 1;
		int randomNorth = top + random.nextInt(tempValue);
		maze[randomNorth][randomX].hasEast = false;
		maze[randomNorth][randomX+1].hasWest = false;
		tempValue = right-randomX;
		if (tempValue <= 0) tempValue = 1;
		int randomEast = randomX + random.nextInt(tempValue);
		maze[randomY][randomEast].hasSouth = false;
		maze[randomY+1][randomEast].hasNorth = false;
		tempValue = bottom-randomY;
		if (tempValue <= 0) tempValue = 1;
		int randomSouth = randomY + random.nextInt(tempValue);
		maze[randomSouth][randomX].hasEast = false;
		maze[randomSouth][randomX+1].hasWest = false;
		tempValue = randomX-left;
		if (tempValue <= 0) tempValue = 1;
		int randomWest = left + random.nextInt(tempValue);
		maze[randomY][randomWest].hasSouth = false;
		maze[randomY+1][randomWest].hasNorth = false;
		
		int noHoleDirection = random.nextInt(4);
		if (noHoleDirection == 0)		// North
		{
			maze[randomNorth][randomX].hasEast = true;
			maze[randomNorth][randomX+1].hasWest = true;
		}
		else if (noHoleDirection == 1)	// East
		{
			maze[randomY][randomEast].hasSouth = true;
			maze[randomY+1][randomEast].hasNorth = true;
		}
		else if (noHoleDirection == 2)	// South
		{
			maze[randomSouth][randomX].hasEast = true;
			maze[randomSouth][randomX+1].hasWest = true;
		}
		else							// West
		{
			maze[randomY][randomWest].hasSouth = true;
			maze[randomY+1][randomWest].hasNorth = true;
		}
		createMaze(left,randomX,top,randomY);
		createMaze(randomX,right,top,randomY);
		createMaze(randomX,right,randomY,bottom);
		createMaze(left,randomX,randomY,bottom);
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

	private class Chamber
	{
		private boolean hasNorth;
		private boolean hasEast;
		private boolean hasSouth;
		private boolean hasWest;

		private Chamber()
		{
			hasNorth = false;
			hasEast = false;
			hasSouth = false;
			hasWest = false;
		}
	}
}
