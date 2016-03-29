public class Tester {
	public static void main(String[] args)
	{
		Maze m = new Maze(9,9);
		boolean w = m.isWestWall(6,4);
		boolean e = m.isEastWall(7,4);
		System.out.println("w " + w + " e " + e);
	}
}