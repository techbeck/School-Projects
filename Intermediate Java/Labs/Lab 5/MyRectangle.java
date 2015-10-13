public class MyRectangle
{
	private int startX;
	private int startY;
	private int width;
	private int height;

	public MyRectangle()
	{
		startX = 0;
		startY = 0;
		width = 0;
		height = 0;
	}

	public MyRectangle(int x, int y, int w, int h)
	{
		startX = x;
		startY = y;
		width = w;
		height = h;
	}

	public int area()
	{
		return width*height;
	}

	public String toString()
	{
		StringBuilder S = new StringBuilder();
		S.append("Width: " + width);
		S.append(" Height: " + height);
		S.append(" X: " + startX);
		S.append(" Y: " + startY);
		return S.toString();
	}

	public boolean isInside(int x, int y)
	{
		if ((x>=startX)&&(x<=startX+width)&&(y>=startY)&&y<=startY+height) {
			return true;
		}
		return false;
	}

	public void setSize(int w, int h)
	{
		width = w;
		height = h;
	}

	public void setPosition(int x, int y)
	{
		startX = x;
		startY = y;
	}

}